package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.BuildConfig
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingConfig
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingEngine
import com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService
import com.meetingcoach.leadershipconversationcoach.data.speech.LocalSpeechToTextService
import com.meetingcoach.leadershipconversationcoach.data.speech.SpeechToTextService
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.domain.models.Priority
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.meetingcoach.leadershipconversationcoach.data.preferences.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val sessionRepository: com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
) : ViewModel() {

    companion object {
        private const val TAG = "SessionViewModel"
    }

    // State
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    // Services
    private var sttService: SpeechToTextService? = null
    private var geminiService: GeminiApiService? = null
    private var coachingEngine: CoachingEngine? = null

    // Timer
    private var timerJob: Job? = null

    // Settings
    private var currentAnalysisInterval = 60_000L

    init {
        // Initialize Gemini service
        initializeGeminiService()

        // Observe settings
        viewModelScope.launch {
            userPreferencesRepository.analysisIntervalFlow.collect { interval ->
                currentAnalysisInterval = interval
                coachingEngine?.let { engine ->
                    val newConfig = engine.getConfig().copy(analysisIntervalMs = interval)
                    engine.updateConfig(newConfig)
                }
            }
        }
    }

    // ============================================================
    // INITIALIZATION
    // ============================================================

    private fun initializeGeminiService() {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY

            if (apiKey.isEmpty()) {
                Log.e(TAG, "GEMINI_API_KEY is empty! Check local.properties")
                return
            }

            geminiService = GeminiApiService(context, apiKey)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Gemini service", e)
        }
    }



    // ============================================================
    // SESSION MANAGEMENT
    // ============================================================

    fun startSession(mode: SessionMode, hasPermission: Boolean) {

        if (!hasPermission) {
            Log.e(TAG, "Microphone permission not granted")
            // Show error in UI
            val errorMessage = ChatMessage(
                type = MessageType.CONTEXT,
                content = "⚠️ Microphone permission is required to start a session",
                priority = Priority.URGENT
            )
            addMessage(errorMessage)
            return
        }

        _sessionState.update {
            it.copy(
                isRecording = true,
                mode = mode,
                startTime = System.currentTimeMillis()
            )
        }

        startTimer()
        startSpeechRecognition()
        startCoachingEngine(mode)
    }

    fun stopSession() {
        timerJob?.cancel()

        // Stop STT
        sttService?.stopListening()
        sttService?.release()
        sttService = null

        // Stop coaching engine but keep reference for analysis
        val engine = coachingEngine
        engine?.stopSession()
        coachingEngine = null

        // Show analyzing state
        val analyzingMessage = ChatMessage(
            type = MessageType.CONTEXT,
            content = "🧠 Analyzing conversation with AI...",
            priority = Priority.INFO
        )
        addMessage(analyzingMessage)

        viewModelScope.launch {
            // Calculate basic metrics
            updateMetrics()
            var currentState = _sessionState.value
            var metrics = currentState.metrics ?: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics()

            // Perform AI Analysis
            try {
                val aiMetrics = engine?.analyzeSession(currentState.messages)
                if (aiMetrics != null) {
                    // Merge AI metrics with calculated metrics
                    metrics = metrics.copy(
                        empathyScore = aiMetrics.empathyScore,
                        clarityScore = aiMetrics.clarityScore,
                        listeningScore = aiMetrics.listeningScore,
                        summary = aiMetrics.summary,
                        paceAnalysis = aiMetrics.paceAnalysis,
                        wordingAnalysis = aiMetrics.wordingAnalysis,
                        improvements = aiMetrics.improvements
                    )
                    Log.d(TAG, "AI Analysis complete: Empathy=${metrics.empathyScore}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "AI Analysis failed", e)
            }

            // Save session to database
            try {
                val startTime = currentState.startTime ?: System.currentTimeMillis()
                val endTime = System.currentTimeMillis()
                val duration = ((endTime - startTime) / 1000).toInt()

                sessionRepository.saveSession(
                    mode = currentState.mode ?: SessionMode.ONE_ON_ONE,
                    startedAt = java.time.Instant.ofEpochMilli(startTime),
                    endedAt = java.time.Instant.ofEpochMilli(endTime),
                    durationSeconds = duration,
                    messages = currentState.messages,
                    metrics = metrics
                )
                Log.d(TAG, "Session saved successfully")
                
                // Notify UI
                val savedMessage = ChatMessage(
                    type = MessageType.CONTEXT,
                    content = "✅ Session saved with AI Insights",
                    priority = Priority.INFO
                )
                addMessage(savedMessage)

            } catch (e: Exception) {
                Log.e(TAG, "Failed to save session", e)
                val errorMessage = ChatMessage(
                    type = MessageType.CONTEXT,
                    content = "⚠️ Failed to save session: ${e.message}",
                    priority = Priority.URGENT
                )
                addMessage(errorMessage)
            }
            
            _sessionState.update { it.copy(isRecording = false) }
        }
    }

    // ============================================================
    // SPEECH-TO-TEXT
    // ============================================================

    private fun startSpeechRecognition() {

        // Initialize STT service
        sttService = LocalSpeechToTextService(context)

        // Start listening
        sttService?.startListening(
            onTranscriptReceived = { chunk ->

                // Only process final results for cleaner transcript
                if (!chunk.isPartial) {
                    // Add to transcript messages
                    val message = ChatMessage(
                        type = MessageType.TRANSCRIPT,
                        content = chunk.text,
                        speaker = chunk.speaker,
                        metadata = com.meetingcoach.leadershipconversationcoach.domain.models.MessageMetadata(
                            emotion = chunk.emotion,
                            confidence = chunk.confidence
                        )
                    )

                    addMessage(message)

                    // Feed to coaching engine for AI analysis
                    coachingEngine?.addTranscript(chunk)

                    // Update metrics
                    updateMetrics()
                }
            },
            onError = { error ->
                // Log error but don't crash
                Log.e(TAG, "STT error: $error")

                // Show error in UI only for critical errors
                if (error.contains("permission", ignoreCase = true)) {
                    val errorMessage = ChatMessage(
                        type = MessageType.CONTEXT,
                        content = "⚠️ $error",
                        priority = Priority.INFO
                    )
                    addMessage(errorMessage)
                }
            }
        )
    }

    // ============================================================
    // AI COACHING
    // ============================================================

    private fun startCoachingEngine(mode: SessionMode) {
        val gemini = geminiService
        if (gemini == null) {
            Log.e(TAG, "Cannot start coaching - Gemini service not available")
            Log.e(TAG, "This usually means the API key is not set correctly")

            // Show warning in UI
            val warningMessage = ChatMessage(
                type = MessageType.CONTEXT,
                content = "⚠️ AI coaching unavailable. Check API key in local.properties:\nGEMINI_API_KEY=your_key_here",
                priority = Priority.INFO
            )
            addMessage(warningMessage)
            return
        }



        // Create coaching engine
        coachingEngine = CoachingEngine(context, gemini)

        // Apply current settings
        val config = coachingEngine!!.getConfig().copy(analysisIntervalMs = currentAnalysisInterval)
        coachingEngine!!.updateConfig(config)

        // Start coaching session
        coachingEngine?.startSession(mode = mode) { nudge ->
            // Coaching nudge generated by AI
            addMessage(nudge)
        }
    }

    private fun updateMetrics() {
        val messages = _sessionState.value.messages
        val transcripts = messages.filter { it.type == MessageType.TRANSCRIPT }

        if (transcripts.isEmpty()) return

        // Calculate metrics
        val userTranscripts = transcripts.filter {
            it.speaker == com.meetingcoach.leadershipconversationcoach.domain.models.Speaker.USER
        }
        val talkRatio = ((userTranscripts.size.toFloat() / transcripts.size) * 100).toInt()

        val questionCount = transcripts.count {
            it.content.trim().endsWith("?")
        }

        val metrics = com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics(
            talkRatio = talkRatio,
            questionCount = questionCount,
            // Other metrics can be calculated here
        )

        _sessionState.update { it.copy(metrics = metrics) }

        // Update coaching engine with new metrics
        coachingEngine?.updateMetrics(metrics)
    }

    // ============================================================
    // USER INTERACTIONS
    // ============================================================

    fun addUserMessage(content: String) {
        val message = ChatMessage(
            type = MessageType.USER_QUESTION,
            content = content,
            priority = Priority.INFO
        )
        addMessage(message)
    }

    fun getAIResponse(question: String): String {

        // This will be called synchronously from UI
        // Launch coroutine to get actual AI response
        viewModelScope.launch {
            try {
                val answer = coachingEngine?.answerQuestion(question)
                if (answer != null) {
                    addMessage(answer)
                } else {
                    Log.e(TAG, "AI response was null")
                    // Add error message
                    val errorMessage = ChatMessage(
                        type = MessageType.AI_RESPONSE,
                        content = "I'm having trouble connecting. Please check your internet connection and API key.",
                        priority = Priority.INFO
                    )
                    addMessage(errorMessage)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting AI response", e)
                val errorMessage = ChatMessage(
                    type = MessageType.AI_RESPONSE,
                    content = "Error: ${e.message}",
                    priority = Priority.INFO
                )
                addMessage(errorMessage)
            }
        }

        // Return placeholder immediately
        return "Thinking..."
    }

    fun addAIResponse(content: String) {
        val message = ChatMessage(
            type = MessageType.AI_RESPONSE,
            content = content,
            priority = Priority.INFO
        )
        addMessage(message)
    }

    // ============================================================
    // MESSAGE MANAGEMENT
    // ============================================================

    fun addMessage(message: ChatMessage) {
        _sessionState.update { state ->
            state.copy(
                messages = state.messages + message
            )
        }
    }

    fun removeMessage(messageId: String) {
        _sessionState.update { state ->
            state.copy(
                messages = state.messages.filter { it.id != messageId }
            )
        }
    }

    // ============================================================
    // TIMER
    // ============================================================

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val elapsed = System.currentTimeMillis() - (_sessionState.value.startTime ?: 0)
                val minutes = (elapsed / 60000).toInt()
                val seconds = ((elapsed % 60000) / 1000).toInt()
                val duration = String.format("%02d:%02d", minutes, seconds)

                _sessionState.update { it.copy(duration = duration) }
            }
        }
    }

    // ============================================================
    // CLEANUP
    // ============================================================

    override fun onCleared() {
        super.onCleared()
        stopSession()
    }
}