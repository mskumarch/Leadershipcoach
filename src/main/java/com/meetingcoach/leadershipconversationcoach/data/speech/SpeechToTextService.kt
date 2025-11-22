package com.meetingcoach.leadershipconversationcoach.data.speech

import com.meetingcoach.leadershipconversationcoach.domain.models.TranscriptChunk

/**
 * SpeechToTextService - Interface for Speech Recognition Services
 *
 * This interface defines the contract that both Local and Cloud STT services must implement.
 * This allows easy switching between FREE (Android) and PREMIUM (Google Cloud) services.
 *
 * Implementations:
 * - LocalSpeechToTextService: FREE - Uses Android SpeechRecognizer
 * - CloudSpeechToTextService: PAID - Uses Google Cloud Speech-to-Text API
 *
 * Architecture Benefits:
 * - Clean abstraction
 * - Easy to swap services
 * - Toggle in settings
 * - Future-proof design
 *
 * Created for: Hybrid STT Architecture
 * Last Updated: November 2025
 */
interface SpeechToTextService {

    /**
     * Start listening for speech
     *
     * Begins continuous speech recognition and calls onTranscriptReceived
     * for each recognized speech segment.
     *
     * @param onTranscriptReceived Callback invoked when speech is recognized
     * @param onError Callback invoked when an error occurs
     */
    fun startListening(
        onTranscriptReceived: (TranscriptChunk) -> Unit,
        onError: (String) -> Unit
    )

    /**
     * Stop listening for speech
     *
     * Stops speech recognition and releases resources.
     * Any pending transcription will be finalized.
     */
    fun stopListening()

    /**
     * Pause listening temporarily
     *
     * Pauses speech recognition without releasing resources.
     * Can be resumed with resumeListening().
     *
     * Note: Not all implementations may support pausing.
     * Default implementation calls stopListening().
     */
    fun pauseListening() {
        stopListening()
    }

    /**
     * Resume listening after pause
     *
     * Resumes speech recognition from paused state.
     *
     * Note: Not all implementations may support resuming.
     * Default implementation calls startListening().
     */
    fun resumeListening(
        onTranscriptReceived: (TranscriptChunk) -> Unit,
        onError: (String) -> Unit
    ) {
        startListening(onTranscriptReceived, onError)
    }

    /**
     * Check if service is currently listening
     *
     * @return true if actively listening for speech
     */
    fun isListening(): Boolean

    /**
     * Check if this service is available on the device
     *
     * Some services may not be available:
     * - Local: May not be available on all devices
     * - Cloud: Requires internet connection and API key
     *
     * @return true if service can be used
     */
    fun isAvailable(): Boolean

    /**
     * Get the service name for display/logging
     *
     * @return Human-readable service name (e.g., "Android STT", "Google Cloud STT")
     */
    fun getServiceName(): String

    /**
     * Check if this is a free service
     *
     * @return true if free, false if paid/premium
     */
    fun isFree(): Boolean

    /**
     * Check if this service requires internet
     *
     * @return true if internet required, false if works offline
     */
    fun requiresInternet(): Boolean

    /**
     * Release all resources
     *
     * Should be called when service is no longer needed.
     * Cleanup any listeners, connections, or allocated resources.
     */
    fun release()
}

/**
 * Result wrapper for STT operations
 *
 * Useful for error handling and state management
 */
sealed class SpeechRecognitionResult {
    /** Successfully recognized speech */
    data class Success(val chunk: TranscriptChunk) : SpeechRecognitionResult()

    /** Partial result (still processing) */
    data class Partial(val text: String) : SpeechRecognitionResult()

    /** Error occurred */
    data class Error(val message: String, val code: Int? = null) : SpeechRecognitionResult()

    /** Recognition ended */
    object Ended : SpeechRecognitionResult()
}

/**
 * STT Service State
 */
enum class SpeechServiceState {
    /** Service not initialized */
    IDLE,

    /** Service ready to listen */
    READY,

    /** Currently listening for speech */
    LISTENING,

    /** Temporarily paused */
    PAUSED,

    /** Processing recognized speech */
    PROCESSING,

    /** Error occurred */
    ERROR
}