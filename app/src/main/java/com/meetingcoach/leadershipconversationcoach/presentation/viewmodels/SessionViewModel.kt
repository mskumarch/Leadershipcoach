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
import com.meetingcoach.leadershipconversationcoach.data.repository.GamificationRepository
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
import com.meetingcoach.leadershipconversationcoach.data.local.SessionEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMessageEntity
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val sessionRepository: SessionRepository,
    private val geminiService: GeminiApiService,
    private val gamificationRepository: GamificationRepository,
    private val coachingOrchestrator: com.meetingcoach.leadershipconversationcoach.data.ai.CoachingOrchestrator
) : ViewModel() {

    companion object {
        private const val TAG = "SessionViewModel"
    }

    // State
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _audioLevel = MutableStateFlow(0f)
    val audioLevel: StateFlow<Float> = _audioLevel.asStateFlow()

    // Orchestrator State (GROW, Questions, Nudges)
    val currentGrowStage: StateFlow<String> = coachingOrchestrator.currentStage
    val suggestedQuestion: StateFlow<com.meetingcoach.leadershipconversationcoach.data.ai.agents.WhispererResult?> = coachingOrchestrator.suggestedQuestion
    val activeNudge: StateFlow<com.meetingcoach.leadershipconversationcoach.data.ai.agents.GuardianResult?> = coachingOrchestrator.activeNudge

    // Services
    private var sttService: LocalSpeechToTextService? = null
    // private var geminiService: GeminiApiService? = null // Removed, using injected instance
    private var coachingEngine: CoachingEngine? = null
    private var audioRecorder: com.meetingcoach.leadershipconversationcoach.data.audio.AudioRecorder? = null
    private var recordedAudioFile: java.io.File? = null

    // Timer
    private var timerJob: Job? = null
    
    // Session Data
    private var lastSavedSessionId: Long? = null

    init {
        // Initialize Gemini service - REMOVED (Injected)
        // initializeGeminiService()

        viewModelScope.launch {
            gamificationRepository.initializeDefaults()
        }

        // Initialize AudioRecorder
        audioRecorder = com.meetingcoach.leadershipconversationcoach.data.audio.AudioRecorder(context)

        // Observe settings
        viewModelScope.launch {
            userPreferencesRepository.analysisIntervalFlow.collect { interval ->
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

    // private fun initializeGeminiService() {
    //     try {
    //         val apiKey = BuildConfig.GEMINI_API_KEY

    //         if (apiKey.isEmpty()) {
    //             Log.e(TAG, "GEMINI_API_KEY is empty! Check local.properties")
    //             return
    //         }

    //         geminiService = GeminiApiService(context, apiKey)

    //     } catch (e: Exception) {
    //         Log.e(TAG, "Failed to initialize Gemini service", e)
    //     }
    // }



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
        recordedAudioFile = audioRecorder?.startRecording()
        startCoachingEngine(mode)
        
        // Start Multi-Agent Orchestrator for 1:1 sessions
        if (mode == SessionMode.ONE_ON_ONE) {
            startOrchestrator(mode)
        }
        
        // Start Foreground Service
        val serviceIntent = android.content.Intent(context, com.meetingcoach.leadershipconversationcoach.services.SessionService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }



    fun pauseSession() {
        val currentState = _sessionState.value
        if (!currentState.isRecording || currentState.isPaused) return

        _sessionState.update { it.copy(isPaused = true) }
        
        // Pause STT
        sttService?.pauseListening()
        
        // Pause Audio Recording
        audioRecorder?.pauseRecording()
        
        // Pause Timer (handled in loop)
    }

    fun resumeSession() {
        val currentState = _sessionState.value
        if (!currentState.isRecording || !currentState.isPaused) return

        // Adjust start time to account for pause duration
        // This is a simplification; for precision, we should track total pause time
        // But for now, we'll just let the timer loop resume
        
        _sessionState.update { it.copy(isPaused = false) }
        
        // Resume STT
        sttService?.resumeListening(
            onTranscriptReceived = { chunk ->
                // Same logic as startListening
                if (!chunk.isPartial) {
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
                    coachingEngine?.addTranscript(chunk)
                    updateMetrics()
                }
            },
            onError = { error -> Log.e(TAG, "STT Error: $error") },
            onAudioLevelChanged = { level -> _audioLevel.value = level }
        )
        
        // Resume Audio Recording
        audioRecorder?.resumeRecording()
    }

    fun stopSession() {
        val currentState = _sessionState.value
        if (!currentState.isRecording) return

        // Mark as not recording immediately to prevent double-save
        _sessionState.update { it.copy(isRecording = false) }

        timerJob?.cancel()
        
        // Stop Orchestrator
        coachingOrchestrator.stopSession()
        
        // Stop Foreground Service
        val serviceIntent = android.content.Intent(context, com.meetingcoach.leadershipconversationcoach.services.SessionService::class.java)
        serviceIntent.action = com.meetingcoach.leadershipconversationcoach.services.SessionService.ACTION_STOP_SESSION
        context.startService(serviceIntent)

        // Stop STT
        sttService?.stopListening()
        sttService?.release()
        sttService?.release()
        sttService = null

        // Stop Audio Recording
        recordedAudioFile = audioRecorder?.stopRecording()

        // Stop coaching engine but keep reference for analysis
        val engine = coachingEngine
        engine?.stopSession()
        coachingEngine = null

        viewModelScope.launch {
            // Calculate basic metrics
            updateMetrics()
            var metrics = currentState.metrics ?: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics()

            // Perform AI Analysis
            var cleanedMessages: List<ChatMessage> = emptyList() // Initialize here for scope
            try {
                // 1. Try Audio Analysis first (Higher Fidelity)
                var aiMetrics: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics? = null
                val audioFile = recordedAudioFile
                
                if (audioFile != null && audioFile.exists()) {
                    try {
                        aiMetrics = engine?.analyzeAudioSession(audioFile)
                    } catch (e: Exception) {
                        Log.e(TAG, "Audio analysis failed, falling back to text", e)
                    }
                }

                // 2. Fallback to Text Analysis if Audio failed or unavailable
                if (aiMetrics == null) {
                    // Clean up Transcript (This comment is misleading, it's just getting raw transcript)
                    val rawTranscript = currentState.messages
                        .filter { it.type == MessageType.TRANSCRIPT }
                        .joinToString("\n") { it.content }

                    if (rawTranscript.isNotBlank()) {
                         aiMetrics = engine?.analyzeSession(currentState.messages)
                    }
                }

                if (aiMetrics != null) {
                    // Merge AI metrics with calculated metrics
                    metrics = metrics.copy(
                        empathyScore = aiMetrics.empathyScore,
                        clarityScore = aiMetrics.clarityScore,
                        listeningScore = aiMetrics.listeningScore,
                        summary = aiMetrics.summary,
                        paceAnalysis = aiMetrics.paceAnalysis,
                        wordingAnalysis = aiMetrics.wordingAnalysis,
                        improvements = aiMetrics.improvements,
                        aiTranscriptJson = aiMetrics.aiTranscriptJson
                    )
                    Log.d(TAG, "AI Analysis complete: Empathy=${metrics.empathyScore}")
                    
                    // Parse cleaned transcript if available
                    val json = metrics.aiTranscriptJson
                    if (!json.isNullOrBlank()) {
                         cleanedMessages = parseAiTranscript(json, emptyList())
                    } else {
                        // Fallback: Try to clean up raw transcript explicitly if audio analysis didn't provide it
                        val rawTranscript = currentState.messages
                            .filter { it.type == MessageType.TRANSCRIPT }
                            .joinToString("\n") { it.content }
                            
                        if (rawTranscript.isNotBlank()) {
                            try {
                                val cleanedJson = geminiService.cleanUpTranscript(rawTranscript)
                                if (cleanedJson != null) {
                                    cleanedMessages = parseAiTranscript(cleanedJson, emptyList())
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Transcript cleanup failed", e)
                            }
                        }
                    }
                } else {
                    // Analysis failed (likely network), queue for later
                    if (audioFile != null && audioFile.exists() && lastSavedSessionId != null) {
                        sessionRepository.queuePendingAnalysis(
                            sessionId = lastSavedSessionId!!,
                            audioFilePath = audioFile.absolutePath,
                            mode = currentState.mode?.name ?: SessionMode.ONE_ON_ONE.name
                        )
                        Log.d(TAG, "Analysis queued for offline processing")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "AI Analysis failed", e)
                // Queue for later
                if (recordedAudioFile != null && recordedAudioFile!!.exists() && lastSavedSessionId != null) {
                     sessionRepository.queuePendingAnalysis(
                        sessionId = lastSavedSessionId!!,
                        audioFilePath = recordedAudioFile!!.absolutePath,
                        mode = currentState.mode?.name ?: SessionMode.ONE_ON_ONE.name
                    )
                }
            }
                
                // 3. Merge Messages (Replace raw transcript with cleaned one)
                val nonTranscriptMessages = currentState.messages.filter { it.type != MessageType.TRANSCRIPT }
                val finalMessagesList = if (cleanedMessages.isNotEmpty()) {
                    (nonTranscriptMessages + cleanedMessages).sortedBy { it.timestamp }
                } else {
                    currentState.messages
                }
                
                // Update currentState for saving
                // We don't update _sessionState here to avoid UI flicker, just use local var for saving
                
                // Save session to database
                try {
                    val startTime = currentState.startTime ?: System.currentTimeMillis()
                    val endTime = System.currentTimeMillis()
                    val duration = ((endTime - startTime) / 1000).toInt()

                    val result = sessionRepository.saveSession(
                        mode = currentState.mode ?: SessionMode.ONE_ON_ONE,
                        startedAt = java.time.Instant.ofEpochMilli(startTime),
                        endedAt = java.time.Instant.ofEpochMilli(endTime),
                        durationSeconds = duration,
                        messages = finalMessagesList,
                        metrics = metrics
                    )
                    val sessionId = result.getOrThrow()
                    lastSavedSessionId = sessionId
                    Log.d(TAG, "Session saved successfully")
                    
                    // Notify UI
                    val savedMessage = ChatMessage(
                        type = MessageType.CONTEXT,
                        content = "✅ Session saved with AI Insights",
                        priority = Priority.INFO
                    )
                    addMessage(savedMessage)
                    
                    // Check Achievements
                    val metricsEntity = com.meetingcoach.leadershipconversationcoach.data.local.SessionMetricsEntity(
                        sessionId = sessionId,
                        talkRatioUser = metrics.talkRatio,
                        questionCount = metrics.questionCount,
                        openQuestionCount = metrics.openQuestionCount,
                        empathyScore = metrics.empathyScore,
                        listeningScore = metrics.listeningScore,
                        clarityScore = metrics.clarityScore,
                        interruptionCount = metrics.interruptionCount,
                        sentiment = metrics.sentiment.name,
                        temperature = metrics.temperature,
                        summary = metrics.summary,
                        paceAnalysis = metrics.paceAnalysis,
                        wordingAnalysis = metrics.wordingAnalysis,
                        improvements = metrics.improvements
                    )
                    gamificationRepository.checkAchievements(metricsEntity)

                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save session", e)
                    val errorMessage = ChatMessage(
                        type = MessageType.CONTEXT,
                        content = "⚠️ Failed to save session: ${e.message}",
                        priority = Priority.URGENT
                    )
                    addMessage(errorMessage)
                }


            
            // Reset Session State for next time
            _sessionState.update { 
                SessionState(
                    mode = it.mode, // Keep the selected mode
                    isRecording = false,
                    messages = emptyList(), // Clear messages
                    metrics = com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics() // Clear metrics
                ) 
            }
        }
    }

    fun updateLastSessionTitle(title: String) {
        val sessionId = lastSavedSessionId ?: return
        viewModelScope.launch {
            sessionRepository.updateSessionTitle(sessionId, title)
        }
    }

    // ============================================================
    // SPEECH-TO-TEXT
    // ============================================================

    private fun startSpeechRecognition() {

        // Start Audio Recording - DISABLED to fix STT conflict
        // audioRecorder = com.meetingcoach.leadershipconversationcoach.data.audio.AudioRecorder(context)
        // recordedAudioFile = audioRecorder?.startRecording()

        // Start STT
        sttService = LocalSpeechToTextService(context)
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
                Log.e(TAG, "STT Error: $error")
            },
            onAudioLevelChanged = { level ->
                _audioLevel.value = level
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

        // Config will be applied by preferences observer
        // No need to set analysisIntervalMs here

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

    /**
     * Get suggested questions for a session mode
     * 
     * **FALLBACK ONLY**: This method provides static questions as a fallback
     * when the Whisperer Agent is unavailable. In normal operation, the
     * Whisperer Agent generates context-aware questions dynamically.
     * 
     * @deprecated Use Whisperer Agent for dynamic, context-aware questions
     */
    @Deprecated("Use Whisperer Agent for dynamic questions", ReplaceWith("coachingOrchestrator.requestQuestion()"))
    fun getSuggestedQuestions(mode: SessionMode?): List<String> {
        val currentMode = mode ?: SessionMode.ONE_ON_ONE
        return when (currentMode) {
            SessionMode.ONE_ON_ONE -> listOf(
                "How can I support you better?",
                "What are your top priorities this week?",
                "What roadblocks are you facing?",
                "How are you feeling about your workload?",
                "Is there anything else on your mind?"
            )
            SessionMode.TEAM_MEETING -> listOf(
                "Does anyone have a different perspective?",
                "What are the next steps?",
                "Are we aligned on this decision?",
                "Who will own this action item?",
                "Let's recap what we've agreed on."
            )
            SessionMode.DIFFICULT_CONVERSATION -> listOf(
                "I want to understand your perspective.",
                "What I'm hearing is...",
                "How can we move forward together?",
                "What impact do you think this has?",
                "Let's focus on the solution."
            )
            SessionMode.ROLEPLAY -> listOf(
                "Tell me more about the situation.",
                "How would you handle this objection?",
                "Let's try a different approach.",
                "What was the outcome?",
                "Give me some feedback on my response."
            )
            SessionMode.OFFICE_POLITICS -> listOf(
                "Who are the key stakeholders here?",
                "What is the underlying motivation?",
                "How does this align with our goals?",
                "What are the risks?",
                "How can we influence this outcome?"
            )
        }
    }

    // ============================================================
    // TIMER
    // ============================================================

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            var pausedDuration = 0L
            var lastPauseTime = 0L
            
            while (true) {
                delay(1000)
                
                val currentState = _sessionState.value
                
                if (currentState.isPaused) {
                    if (lastPauseTime == 0L) {
                        lastPauseTime = System.currentTimeMillis()
                    }
                    continue
                } else {
                    if (lastPauseTime != 0L) {
                        pausedDuration += (System.currentTimeMillis() - lastPauseTime)
                        lastPauseTime = 0L
                    }
                }
                
                if (currentState.isRecording) {
                    val elapsed = System.currentTimeMillis() - (currentState.startTime ?: 0) - pausedDuration
                    val minutes = (elapsed / 60000).toInt()
                    val seconds = ((elapsed % 60000) / 1000).toInt()
                    val duration = String.format("%02d:%02d", minutes, seconds)

                    _sessionState.update { it.copy(duration = duration) }
                }
            }
        }
    }

    // ============================================================
    // CLEANUP
    // ============================================================

    override fun onCleared() {
        super.onCleared()
        stopSession()
        audioRecorder?.release()
    }

    // ============================================================
    // MULTI-AGENT ORCHESTRATOR
    // ============================================================

    private fun startOrchestrator(mode: SessionMode) {
        try {
            // Create strategy based on session mode
            val strategy = when (mode) {
                SessionMode.ONE_ON_ONE -> {
                    val geminiModel = com.google.ai.client.generativeai.GenerativeModel(
                        modelName = "gemini-flash-latest",
                        apiKey = com.meetingcoach.leadershipconversationcoach.BuildConfig.GEMINI_API_KEY
                    )
                    com.meetingcoach.leadershipconversationcoach.data.ai.strategies.OneOnOneStrategy(geminiModel)
                }
                else -> return // Only 1:1 supported for now
            }

            // Provide transcript access to orchestrator
            coachingOrchestrator.setTranscriptProvider {
                _sessionState.value.messages
            }

            // Start the orchestrator
            coachingOrchestrator.startSession(strategy)
            
            Log.d(TAG, "Orchestrator started for $mode")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start orchestrator", e)
        }
    }

    fun requestContextualQuestion() {
        viewModelScope.launch {
            coachingOrchestrator.requestQuestion(_sessionState.value.messages)
        }
    }

    // ============================================================
    // HELPERS
    // ============================================================
    
    private fun parseAiTranscript(json: String, originalMessages: List<ChatMessage>): List<ChatMessage> {
        val messages = mutableListOf<ChatMessage>()
        
        // Keep non-transcript messages (e.g., AI Nudges, Context)
        messages.addAll(originalMessages.filter { it.type != MessageType.TRANSCRIPT })
        
        try {
            val jsonArray = org.json.JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val speaker = item.optString("speaker", "Unknown")
                val text = item.optString("text", "")
                
                if (text.isNotBlank()) {
                    // Prepend speaker label to text since our Speaker enum is limited
                    val contentWithSpeaker = "[$speaker] $text"
                    
                    messages.add(
                        ChatMessage(
                            type = MessageType.TRANSCRIPT,
                            content = contentWithSpeaker,
                            speaker = com.meetingcoach.leadershipconversationcoach.domain.models.Speaker.OTHER, // Default to OTHER
                            timestamp = System.currentTimeMillis() 
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "JSON Parsing error", e)
            return originalMessages
        }
        
        return messages.sortedBy { it.timestamp }
    }
}