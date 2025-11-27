package com.meetingcoach.leadershipconversationcoach.data.manager

import android.content.Context
import android.util.Log
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingEngine
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingOrchestrator
import com.meetingcoach.leadershipconversationcoach.data.audio.AudioRecorder
import com.meetingcoach.leadershipconversationcoach.data.speech.LocalSpeechToTextService
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.domain.models.TranscriptChunk
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionManager - Manages the lifecycle of a coaching session.
 * 
 * Coordinates:
 * - Audio Recording
 * - Speech-to-Text
 * - Coaching Engine (Real-time analysis)
 * - Coaching Orchestrator (Multi-Agent System)
 */
@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRecorder: AudioRecorder,
    private val sttService: LocalSpeechToTextService,
    private val coachingEngine: CoachingEngine,
    private val coachingOrchestrator: CoachingOrchestrator
) {

    private val TAG = "SessionManager"
    private val scope = CoroutineScope(Dispatchers.Main)

    // Event Flows
    private val _transcripts = MutableSharedFlow<TranscriptChunk>()
    val transcripts: SharedFlow<TranscriptChunk> = _transcripts.asSharedFlow()

    private val _audioLevel = MutableStateFlow(0f)
    val audioLevel: StateFlow<Float> = _audioLevel.asStateFlow()

    private val _coachingNudges = MutableSharedFlow<ChatMessage>()
    val coachingNudges: SharedFlow<ChatMessage> = _coachingNudges.asSharedFlow()

    // State
    private var recordedFile: File? = null
    private var isRecording = false
    private var isPaused = false

    /**
     * Start a new session
     */
    fun startSession(mode: SessionMode) {
        if (isRecording) {
            Log.w(TAG, "Session already in progress")
            return
        }

        Log.d(TAG, "Starting session in mode: $mode")
        isRecording = true
        isPaused = false

        // 1. Start Audio Recording
        recordedFile = audioRecorder.startRecording()

        // 2. Start Speech Recognition
        sttService.startListening(
            onTranscriptReceived = { chunk ->
                scope.launch {
                    _transcripts.emit(chunk)
                    // Feed to Coaching Engine
                    coachingEngine.addTranscript(chunk)
                    
                    // Feed to Orchestrator (if not partial)
                    if (!chunk.isPartial) {
                         val message = ChatMessage(
                            type = com.meetingcoach.leadershipconversationcoach.domain.models.MessageType.TRANSCRIPT,
                            content = chunk.text,
                            speaker = chunk.speaker,
                            metadata = com.meetingcoach.leadershipconversationcoach.domain.models.MessageMetadata(
                                emotion = chunk.emotion,
                                confidence = chunk.confidence
                            )
                        )
                        coachingOrchestrator.addMessage(message)
                    }
                }
            },
            onError = { error ->
                Log.e(TAG, "STT Error: $error")
            },
            onAudioLevelChanged = { level ->
                _audioLevel.value = level
            }
        )

        // 3. Start Coaching Engine (Real-time)
        coachingEngine.startSession(mode) { nudge ->
            scope.launch {
                _coachingNudges.emit(nudge)
            }
        }

        // 4. Start Multi-Agent Orchestrator
        if (mode == SessionMode.ONE_ON_ONE) {
            try {
                val geminiModel = com.google.ai.client.generativeai.GenerativeModel(
                    modelName = "gemini-flash-latest",
                    apiKey = com.meetingcoach.leadershipconversationcoach.BuildConfig.GEMINI_API_KEY
                )
                val strategy = com.meetingcoach.leadershipconversationcoach.data.ai.strategies.OneOnOneStrategy(geminiModel)
                
                coachingOrchestrator.clearMessages()
                coachingOrchestrator.startSession(strategy)
                Log.d(TAG, "Orchestrator started for $mode")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start orchestrator", e)
            }
        }
    }

    /**
     * Pause the current session
     */
    fun pauseSession() {
        if (!isRecording || isPaused) return
        
        Log.d(TAG, "Pausing session")
        isPaused = true
        
        audioRecorder.pauseRecording()
        sttService.pauseListening()
    }

    /**
     * Resume the current session
     */
    fun resumeSession() {
        if (!isRecording || !isPaused) return
        
        Log.d(TAG, "Resuming session")
        isPaused = false
        
        audioRecorder.resumeRecording()
        
        // Resume STT with same callbacks
        sttService.resumeListening(
            onTranscriptReceived = { chunk ->
                scope.launch {
                    _transcripts.emit(chunk)
                    coachingEngine.addTranscript(chunk)
                }
            },
            onError = { error -> Log.e(TAG, "STT Error: $error") },
            onAudioLevelChanged = { level -> _audioLevel.value = level }
        )
    }

    /**
     * Stop the session and return the recorded audio file
     */
    fun stopSession(): File? {
        if (!isRecording) return null

        Log.d(TAG, "Stopping session")
        isRecording = false
        isPaused = false

        // Stop all components
        val file = audioRecorder.stopRecording()
        sttService.stopListening()
        sttService.release() // Important: Release resources
        coachingEngine.stopSession()
        coachingOrchestrator.stopSession()

        return file
    }
    
    /**
     * Clean up resources
     */
    fun release() {
        sttService.release()
    }
}
