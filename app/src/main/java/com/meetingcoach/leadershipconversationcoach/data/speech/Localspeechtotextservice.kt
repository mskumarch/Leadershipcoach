package com.meetingcoach.leadershipconversationcoach.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.meetingcoach.leadershipconversationcoach.domain.models.Speaker
import com.meetingcoach.leadershipconversationcoach.domain.models.TranscriptChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * LocalSpeechToTextService - FREE Android Speech Recognition
 *
 * Uses Android's built-in SpeechRecognizer API for real-time speech transcription.
 * This is a FREE service that works offline (on compatible devices like Pixel 9 Pro).
 *
 * Features:
 * - Continuous recognition (auto-restart)
 * - Partial results (see text as it's being spoken)
 * - Automatic punctuation (on newer Android versions)
 * - Low latency (<100ms)
 * - No cost
 * - Good accuracy (85-90% in quiet environments)
 *
 * Limitations:
 * - No speaker diarization (can't identify who's speaking)
 * - Lower accuracy than Google Cloud STT
 * - May struggle with noise/accents
 * - Requires RECORD_AUDIO permission
 *
 * Optimized for: Pixel 9 Pro with Gemini Nano
 *
 * Architecture: Data Layer - STT Implementation
 *
 * Created for: FREE Speech Recognition
 * Last Updated: November 2025
 */
class LocalSpeechToTextService(
    private val context: Context
) : SpeechToTextService {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isCurrentlyListening = false
    private var shouldContinueListening = false

    // Callbacks
    private var onTranscriptCallback: ((TranscriptChunk) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null

    // Auto-restart management
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var restartJob: Job? = null

    // State tracking
    private var lastPartialResult: String = ""
    private var sessionStartTime: Long = 0L

    // Throttling variables
    private var lastFinalResultTime = 0L
    private var lastPartialResultTime = 0L
    private val MIN_TIME_BETWEEN_FINALS = 1500L // 1.5 seconds between final results
    private val MIN_TIME_BETWEEN_PARTIALS = 500L // 0.5 seconds between partial updates

    companion object {
        private const val TAG = "LocalSTT"
        private const val RESTART_DELAY_MS = 100L // Restart quickly for continuous mode
        private const val MAX_RESTART_ATTEMPTS = 3
        private var restartAttempts = 0
    }

    init {
        // Initialize speech recognizer if available
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            Log.e(TAG, "Speech recognition not available on this device")
        }
    }

    // ============================================================
    // SpeechToTextService Interface Implementation
    // ============================================================

    override fun startListening(
        onTranscriptReceived: (TranscriptChunk) -> Unit,
        onError: (String) -> Unit
    ) {

        if (!isAvailable()) {
            onError("Speech recognition not available on this device")
            return
        }

        // Store callbacks
        onTranscriptCallback = onTranscriptReceived
        onErrorCallback = onError

        // Mark session start time
        if (sessionStartTime == 0L) {
            sessionStartTime = System.currentTimeMillis()
        }

        // Enable continuous listening
        shouldContinueListening = true
        restartAttempts = 0

        // Start recognition
        startRecognition()
    }

    override fun stopListening() {

        shouldContinueListening = false
        isCurrentlyListening = false

        // Cancel any pending restart
        restartJob?.cancel()
        restartJob = null

        // Stop recognizer
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            // Ignore errors on stop
        }

        // Reset session
        sessionStartTime = 0L
        lastPartialResult = ""
        restartAttempts = 0
        lastFinalResultTime = 0L
        lastPartialResultTime = 0L
    }

    override fun pauseListening() {
        shouldContinueListening = false
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            // Ignore errors
        }
        isCurrentlyListening = false
    }

    override fun resumeListening(
        onTranscriptReceived: (TranscriptChunk) -> Unit,
        onError: (String) -> Unit
    ) {
        if (isCurrentlyListening) return

        // Update callbacks
        onTranscriptCallback = onTranscriptReceived
        onErrorCallback = onError

        // Resume listening
        shouldContinueListening = true
        startRecognition()
    }

    override fun isListening(): Boolean = isCurrentlyListening

    override fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }

    override fun getServiceName(): String = "Android Speech Recognition"

    override fun isFree(): Boolean = true

    override fun requiresInternet(): Boolean = false // Works offline on most devices

    override fun release() {
        stopListening()

        try {
            speechRecognizer?.destroy()
        } catch (e: Exception) {
            // Ignore errors
        }

        speechRecognizer = null
        onTranscriptCallback = null
        onErrorCallback = null
    }

    // ============================================================
    // Private Recognition Methods
    // ============================================================

    /**
     * Start speech recognition
     */
    private fun startRecognition() {
        if (isCurrentlyListening) return

        val recognizer = speechRecognizer ?: run {
            onErrorCallback?.invoke("Speech recognizer not initialized")
            return
        }

        // Create recognition intent
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // TODO: Make configurable
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Enable partial results
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 5000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 5000L)
        }

        // Set recognition listener
        recognizer.setRecognitionListener(createRecognitionListener())

        // Start listening
        try {
            recognizer.startListening(intent)
            isCurrentlyListening = true
            restartAttempts = 0 // Reset on successful start
        } catch (e: Exception) {
            isCurrentlyListening = false
            onErrorCallback?.invoke("Failed to start recognition: ${e.message}")
            Log.e(TAG, "Failed to start recognition", e)

            // Try to restart if continuous mode
            if (shouldContinueListening) {
                scheduleRestart()
            }
        }
    }

    /**
     * Create recognition listener to handle callbacks
     */
    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {

            override fun onReadyForSpeech(params: Bundle?) {
                // Ready to listen
                isCurrentlyListening = true
            }

            override fun onBeginningOfSpeech() {
                // User started speaking
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Audio level changed (useful for UI visualization)
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Audio buffer received (not commonly used)
            }

            override fun onEndOfSpeech() {
                // User stopped speaking
                isCurrentlyListening = false
            }

            override fun onError(error: Int) {
                isCurrentlyListening = false

                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                    else -> "Unknown error ($error)"
                }



                // Don't report "no match" or "speech timeout" as errors - these are normal
                if (error != SpeechRecognizer.ERROR_NO_MATCH &&
                    error != SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                    onErrorCallback?.invoke(errorMessage)
                }

                // Auto-restart for continuous listening
                if (shouldContinueListening) {
                    scheduleRestart()
                }
            }

            override fun onResults(results: Bundle?) {
                val currentTime = System.currentTimeMillis()

                // Throttle final results - REMOVED to prevent data loss
                // if (currentTime - lastFinalResultTime < MIN_TIME_BETWEEN_FINALS) {
                //    // Too soon, schedule restart but skip this result
                //    if (shouldContinueListening) {
                //        scheduleRestart()
                //    }
                //    return
                // }

                lastFinalResultTime = currentTime
                isCurrentlyListening = false

                // Extract recognized text
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull()

                if (!text.isNullOrBlank()) {

                    // Create transcript chunk
                    val chunk = TranscriptChunk(
                        text = text.trim(),
                        speaker = Speaker.UNKNOWN, // No speaker diarization in local STT
                        timestamp = System.currentTimeMillis(),
                        confidence = getConfidenceScore(results),
                        isPartial = false,
                        duration = 0L // Duration not available in Android STT
                    )

                    // Deliver to callback
                    onTranscriptCallback?.invoke(chunk)

                    // Reset partial result
                    lastPartialResult = ""
                }

                // Auto-restart for continuous listening
                if (shouldContinueListening) {
                    scheduleRestart()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val currentTime = System.currentTimeMillis()

                // Throttle partial results
                if (currentTime - lastPartialResultTime < MIN_TIME_BETWEEN_PARTIALS) {
                    return // Skip this partial result
                }

                // Also skip if we just had a final result
                if (currentTime - lastFinalResultTime < 1000L) {
                    return
                }

                lastPartialResultTime = currentTime

                // Extract partial text
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val partialText = matches?.firstOrNull()

                if (!partialText.isNullOrBlank() && partialText != lastPartialResult) {
                    lastPartialResult = partialText

                    // Create partial transcript chunk
                    val chunk = TranscriptChunk(
                        text = partialText.trim(),
                        speaker = Speaker.UNKNOWN,
                        timestamp = System.currentTimeMillis(),
                        confidence = 0.5f, // Lower confidence for partial results
                        isPartial = true,
                        duration = 0L
                    )

                    // Deliver to callback
                    onTranscriptCallback?.invoke(chunk)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Custom events (not commonly used)
            }
        }
    }

    /**
     * Schedule automatic restart for continuous listening
     */
    private fun scheduleRestart() {
        if (!shouldContinueListening) return

        // Check restart attempts
        if (restartAttempts >= MAX_RESTART_ATTEMPTS) {
            onErrorCallback?.invoke("Failed to restart recognition after $MAX_RESTART_ATTEMPTS attempts")
            shouldContinueListening = false
            return
        }

        restartAttempts++

        // Cancel any existing restart job
        restartJob?.cancel()

        // Schedule restart
        restartJob = serviceScope.launch {
            delay(RESTART_DELAY_MS)
            if (shouldContinueListening && !isCurrentlyListening) {
                startRecognition()
            }
        }
    }

    /**
     * Extract confidence score from recognition results
     */
    private fun getConfidenceScore(results: Bundle?): Float {
        val confidenceScores = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
        return confidenceScores?.firstOrNull() ?: 0.85f // Default confidence
    }

    /**
     * Get current recognition state for debugging
     */
    fun getStateDescription(): String {
        return when {
            !isAvailable() -> "Not available"
            isCurrentlyListening -> "Listening"
            shouldContinueListening -> "Starting..."
            else -> "Idle"
        }
    }
}