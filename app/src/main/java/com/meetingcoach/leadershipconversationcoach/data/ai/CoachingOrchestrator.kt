package com.meetingcoach.leadershipconversationcoach.data.ai

import com.meetingcoach.leadershipconversationcoach.data.ai.agents.GuardianResult
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.NavigatorResult
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.WhispererResult
import com.meetingcoach.leadershipconversationcoach.data.ai.strategies.CoachingStrategy
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoachingOrchestrator @Inject constructor() {

    private var currentStrategy: CoachingStrategy? = null
    private var orchestratorScope: CoroutineScope? = null

    // State Flows for UI
    private val _currentStage = MutableStateFlow("START")
    val currentStage: StateFlow<String> = _currentStage.asStateFlow()

    private val _suggestedQuestion = MutableStateFlow<WhispererResult?>(null)
    val suggestedQuestion: StateFlow<WhispererResult?> = _suggestedQuestion.asStateFlow()

    private val _activeNudge = MutableStateFlow<GuardianResult?>(null)
    val activeNudge: StateFlow<GuardianResult?> = _activeNudge.asStateFlow()

    fun startSession(strategy: CoachingStrategy) {
        currentStrategy = strategy
        orchestratorScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        
        startNavigatorLoop()
        startGuardianLoop()
    }

    fun stopSession() {
        orchestratorScope?.cancel()
        orchestratorScope = null
        currentStrategy = null
    }

    fun requestQuestion(transcript: List<ChatMessage>) {
        orchestratorScope?.launch {
            val strategy = currentStrategy ?: return@launch
            val context = mapOf("stage" to _currentStage.value)
            
            val result = strategy.whisperer.process(transcript, context)
            if (result != null) {
                _suggestedQuestion.emit(result)
            }
        }
    }

    private fun startNavigatorLoop() {
        orchestratorScope?.launch {
            val strategy = currentStrategy ?: return@launch
            while (isActive) {
                delay(strategy.navigatorIntervalMs)
                runNavigatorNow()
            }
        }
    }

    private fun startGuardianLoop() {
        orchestratorScope?.launch {
            val strategy = currentStrategy ?: return@launch
            while (isActive) {
                delay(strategy.guardianIntervalMs)
                runGuardianNow()
            }
        }
    }
    
    // Local message buffer
    private val messages = java.util.concurrent.CopyOnWriteArrayList<ChatMessage>()
    
    fun addMessage(message: ChatMessage) {
        messages.add(message)
    }
    
    fun clearMessages() {
        messages.clear()
    }
    
    suspend fun runNavigatorNow() {
        val strategy = currentStrategy ?: return
        if (messages.isEmpty()) return

        val result = strategy.navigator.process(messages.toList(), emptyMap())
        if (result != null) {
            _currentStage.emit(result.currentStage)
        }
    }
    
    suspend fun runGuardianNow() {
        val strategy = currentStrategy ?: return
        if (messages.isEmpty()) return

        val result = strategy.guardian.process(messages.toList(), mapOf("stage" to _currentStage.value))
        if (result != null) {
            _activeNudge.emit(result)
        }
    }
}
