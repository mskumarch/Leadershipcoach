package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingOrchestrator
import com.meetingcoach.leadershipconversationcoach.data.manager.SessionManager
import com.meetingcoach.leadershipconversationcoach.data.repository.GamificationRepository
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
import com.meetingcoach.leadershipconversationcoach.data.preferences.UserPreferencesRepository
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.domain.models.Priority
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionState
import com.meetingcoach.leadershipconversationcoach.domain.usecase.AnalyzeSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val sessionRepository: SessionRepository,
    private val gamificationRepository: GamificationRepository,
    private val coachingOrchestrator: CoachingOrchestrator,
    private val sessionManager: SessionManager,
    private val analyzeSessionUseCase: AnalyzeSessionUseCase,
    private val analyzeDynamicsUseCase: com.meetingcoach.leadershipconversationcoach.domain.usecase.AnalyzeDynamicsUseCase,
    private val stakeholderRepository: com.meetingcoach.leadershipconversationcoach.data.repository.StakeholderRepository,
    private val geminiApiService: com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService
) : ViewModel() {

    companion object {
        private const val TAG = "SessionViewModel"
    }

    // State
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    // Dynamics Analysis State
    private val _dynamicsAnalysis = MutableStateFlow<com.meetingcoach.leadershipconversationcoach.domain.models.DynamicsAnalysis?>(null)
    val dynamicsAnalysis: StateFlow<com.meetingcoach.leadershipconversationcoach.domain.models.DynamicsAnalysis?> = _dynamicsAnalysis.asStateFlow()

    // Stakeholders State
    private val _stakeholders = MutableStateFlow<List<com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder>>(emptyList())
    val stakeholders: StateFlow<List<com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder>> = _stakeholders.asStateFlow()

    private val _selectedStakeholder = MutableStateFlow<com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder?>(null)
    val selectedStakeholder: StateFlow<com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder?> = _selectedStakeholder.asStateFlow()

    // Daily Tip State
    private val _dailyTip = MutableStateFlow<String>("Loading daily wisdom...")
    val dailyTip: StateFlow<String> = _dailyTip.asStateFlow()

    // Audio level from SessionManager
    val audioLevel: StateFlow<Float> = sessionManager.audioLevel

    // Orchestrator State (GROW, Questions, Nudges)
    val currentGrowStage: StateFlow<String> = coachingOrchestrator.currentStage
    val suggestedQuestion = coachingOrchestrator.suggestedQuestion
    val activeNudge = coachingOrchestrator.activeNudge

    // Timer
    private var timerJob: Job? = null
    
    // Session Data
    private var lastSavedSessionId: Long? = null

    init {
        viewModelScope.launch {
            gamificationRepository.initializeDefaults()
        }

        // Observe SessionManager events
        // Observe SessionManager events
        viewModelScope.launch {
            sessionManager.transcripts.collect { chunk ->
                if (chunk.isPartial) {
                    // Update partial transcript for live UI
                    _sessionState.update { it.copy(partialTranscript = chunk.text) }
                    
                    // Trigger Dynamics Analysis on partials for "Live" feel
                    // Only analyze if text is substantial (> 20 chars) to avoid noise
                    if (_sessionState.value.mode == SessionMode.DYNAMICS && chunk.text.length > 20) {
                        val analysis = analyzeDynamicsUseCase(chunk.text)
                        _dynamicsAnalysis.value = analysis
                    }
                } else {
                    // Final result
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
                    updateMetrics()
                    
                    // Clear partial transcript
                    _sessionState.update { it.copy(partialTranscript = "") }

                    // Trigger Dynamics Analysis on final result
                    if (_sessionState.value.mode == SessionMode.DYNAMICS) {
                        val analysis = analyzeDynamicsUseCase(chunk.text)
                        _dynamicsAnalysis.value = analysis
                        
                        // If there is strategic advice, show it as a nudge
                        analysis.strategicAdvice?.let { advice ->
                             addMessage(ChatMessage(
                                type = MessageType.URGENT_NUDGE,
                                content = advice,
                                priority = Priority.URGENT
                            ))
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            sessionManager.coachingNudges.collect { nudge ->
                addMessage(nudge)
            }
        }

        loadStakeholders()
        fetchDailyTip()
    }

    fun fetchDailyTip() {
        viewModelScope.launch {
            _dailyTip.value = "Thinking of a new tip..."
            val tip = geminiApiService.generateDailyTip()
            if (tip != null) {
                _dailyTip.value = tip
            } else {
                // Fallback tips if AI fails or offline
                val fallbackTips = listOf(
                    "Listen more than you speak.",
                    "Ask 'What do you think?' before giving answers.",
                    "Clear expectations prevent future conflicts.",
                    "Feedback is a gift, even when it's wrapped poorly.",
                    "Trust is earned in drops and lost in buckets."
                )
                _dailyTip.value = fallbackTips.random()
            }
        }
    }

    private fun loadStakeholders() {
        viewModelScope.launch {
            stakeholderRepository.getAllStakeholders().collect { list ->
                if (list.isEmpty()) {
                    seedStakeholders()
                } else {
                    _stakeholders.value = list
                }
            }
        }
    }

    private suspend fun seedStakeholders() {
        val defaultStakeholders = listOf(
            com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder(
                name = "Sarah (CFO)",
                role = "Chief Financial Officer",
                relationship = "Peer",
                tendencies = listOf(
                    com.meetingcoach.leadershipconversationcoach.domain.models.BehavioralTendency("Deflection", "High", "Ask for specific numbers."),
                    com.meetingcoach.leadershipconversationcoach.domain.models.BehavioralTendency("Risk Aversion", "Medium", "Highlight ROI and safety.")
                )
            ),
            com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder(
                name = "John (Eng Lead)",
                role = "Engineering Manager",
                relationship = "Direct Report",
                tendencies = emptyList()
            )
        )
        defaultStakeholders.forEach { stakeholderRepository.addStakeholder(it) }
    }

    fun setSelectedStakeholder(stakeholder: com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder?) {
        _selectedStakeholder.value = stakeholder
    }

    // ============================================================
    // SESSION MANAGEMENT
    // ============================================================

    fun startSession(mode: SessionMode, hasPermission: Boolean) {
        if (!hasPermission) {
            Log.e(TAG, "Microphone permission not granted")
            addMessage(ChatMessage(
                type = MessageType.CONTEXT,
                content = context.getString(com.meetingcoach.leadershipconversationcoach.R.string.permission_mic_required),
                priority = Priority.URGENT
            ))
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
        sessionManager.startSession(mode)
        
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
        sessionManager.pauseSession()
    }

    fun resumeSession() {
        val currentState = _sessionState.value
        if (!currentState.isRecording || !currentState.isPaused) return

        _sessionState.update { it.copy(isPaused = false) }
        sessionManager.resumeSession()
    }

    fun stopSession() {
        val currentState = _sessionState.value
        if (!currentState.isRecording) return

        _sessionState.update { it.copy(isRecording = false) }
        timerJob?.cancel()

        // Stop SessionManager
        val audioFile = sessionManager.stopSession()
        
        // Stop Foreground Service
        val serviceIntent = android.content.Intent(context, com.meetingcoach.leadershipconversationcoach.services.SessionService::class.java)
        serviceIntent.action = com.meetingcoach.leadershipconversationcoach.services.SessionService.ACTION_STOP_SESSION
        context.startService(serviceIntent)

        // Analyze Session
        viewModelScope.launch {
            updateMetrics() // Calculate basic metrics first
            val metrics = currentState.metrics ?: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics()
            
            val result = analyzeSessionUseCase(audioFile, currentState.messages, metrics)
            
            val finalMetrics = when (result) {
                is com.meetingcoach.leadershipconversationcoach.utils.Result.Success -> {
                    val m = result.data
                    _sessionState.update { it.copy(metrics = m) }
                    m
                }
                is com.meetingcoach.leadershipconversationcoach.utils.Result.Error -> {
                    Log.e(TAG, "Analysis failed: ${result.message}")
                    metrics
                }
                else -> metrics
            }
            
            saveSessionToDb(currentState, finalMetrics)
            
            // Reset Session State
            _sessionState.update { 
                SessionState(
                    mode = it.mode,
                    isRecording = false,
                    messages = emptyList(),
                    metrics = com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics()
                ) 
            }
        }
    }

    private suspend fun saveSessionToDb(state: SessionState, metrics: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics) {
        try {
            val startTime = state.startTime ?: System.currentTimeMillis()
            val endTime = System.currentTimeMillis()
            val duration = ((endTime - startTime) / 1000).toInt()

            val result = sessionRepository.saveSession(
                mode = state.mode ?: SessionMode.ONE_ON_ONE,
                startedAt = java.time.Instant.ofEpochMilli(startTime),
                endedAt = java.time.Instant.ofEpochMilli(endTime),
                durationSeconds = duration,
                messages = state.messages,
                metrics = metrics
            )
            val sessionId = result.getOrThrow()
            lastSavedSessionId = sessionId
            Log.d(TAG, "Session saved successfully")
            
            addMessage(ChatMessage(
                type = MessageType.CONTEXT,
                content = context.getString(com.meetingcoach.leadershipconversationcoach.R.string.msg_session_saved),
                priority = Priority.INFO
            ))
            
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
            addMessage(ChatMessage(
                type = MessageType.CONTEXT,
                content = context.getString(com.meetingcoach.leadershipconversationcoach.R.string.msg_session_save_failed, e.message),
                priority = Priority.URGENT
            ))
        }
    }

    fun updateLastSessionTitle(title: String) {
        val sessionId = lastSavedSessionId ?: return
        viewModelScope.launch {
            sessionRepository.updateSessionTitle(sessionId, title)
        }
    }

    // ============================================================
    // METRICS
    // ============================================================

    private fun updateMetrics() {
        val messages = _sessionState.value.messages
        val transcripts = messages.filter { it.type == MessageType.TRANSCRIPT }

        if (transcripts.isEmpty()) return

        // Calculate metrics
        val userTranscripts = transcripts.filter {
            it.speaker == com.meetingcoach.leadershipconversationcoach.domain.models.Speaker.USER
        }
        val talkRatio = if (transcripts.isNotEmpty()) {
            ((userTranscripts.size.toFloat() / transcripts.size) * 100).toInt()
        } else 0

        val questionCount = transcripts.count {
            it.content.trim().endsWith("?")
        }

        val metrics = com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics(
            talkRatio = talkRatio,
            questionCount = questionCount
        )

        _sessionState.update { it.copy(metrics = metrics) }
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
        // Placeholder for synchronous UI call
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
     * when the Whisperer Agent is unavailable.
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
            SessionMode.DYNAMICS -> listOf(
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
        sessionManager.release()
    }

    fun requestContextualQuestion() {
        viewModelScope.launch {
            coachingOrchestrator.requestQuestion(_sessionState.value.messages)
        }
    }
}