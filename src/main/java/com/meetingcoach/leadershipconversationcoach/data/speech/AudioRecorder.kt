package com.meetingcoach.leadershipconversationcoach.data.speech

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat

/**
 * AudioRecorder - Microphone Handler for Speech Recognition
 *
 * Manages audio recording for speech-to-text services:
 * - Checks and requests RECORD_AUDIO permission
 * - Configures audio source and format
 * - Starts/stops audio capture
 * - Provides audio data to STT services
 * - Error handling and cleanup
 *
 * Note: This class handles RAW audio capture. The actual speech recognition
 * is done by SpeechToTextService implementations.
 *
 * Architecture: Data Layer - Audio Input
 *
 * Created for: STT Backend Integration
 * Last Updated: November 2025
 */
class AudioRecorder(private val context: Context) {

    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    // Audio configuration
    private val sampleRate = 16000 // 16kHz - Standard for speech
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        channelConfig,
        audioFormat
    )

    /**
     * Check if RECORD_AUDIO permission is granted
     *
     * @return true if permission granted, false otherwise
     */
    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Initialize audio recorder
     *
     * Creates and configures AudioRecord instance.
     * Must be called before startRecording().
     *
     * @return true if initialization successful, false otherwise
     */
    fun initialize(): Boolean {
        if (!hasPermission()) {
            return false
        }

        return try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.VOICE_RECOGNITION, // Optimized for speech
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            // Verify initialization
            audioRecord?.state == AudioRecord.STATE_INITIALIZED
        } catch (e: SecurityException) {
            // Permission not granted
            false
        } catch (e: IllegalArgumentException) {
            // Invalid audio configuration
            false
        }
    }

    /**
     * Start recording audio
     *
     * Begins capturing audio from microphone.
     * Audio data can be read using readAudioData().
     *
     * @return true if recording started successfully, false otherwise
     */
    fun startRecording(): Boolean {
        if (audioRecord == null) {
            if (!initialize()) {
                return false
            }
        }

        return try {
            audioRecord?.startRecording()
            isRecording = true
            true
        } catch (e: IllegalStateException) {
            isRecording = false
            false
        }
    }

    /**
     * Stop recording audio
     *
     * Stops audio capture but keeps AudioRecord instance.
     * Can be restarted with startRecording().
     */
    fun stopRecording() {
        if (isRecording) {
            try {
                audioRecord?.stop()
            } catch (e: IllegalStateException) {
                // Already stopped
            }
            isRecording = false
        }
    }

    /**
     * Read audio data from microphone
     *
     * This method blocks until audio data is available.
     * Should be called from a background thread.
     *
     * @param buffer Buffer to store audio data
     * @return Number of bytes read, or negative on error
     */
    fun readAudioData(buffer: ByteArray): Int {
        return audioRecord?.read(buffer, 0, buffer.size) ?: -1
    }

    /**
     * Check if currently recording
     *
     * @return true if recording, false otherwise
     */
    fun isRecording(): Boolean = isRecording

    /**
     * Get audio configuration details
     *
     * @return AudioConfig object with current settings
     */
    fun getAudioConfig(): AudioConfig {
        return AudioConfig(
            sampleRate = sampleRate,
            channelConfig = channelConfig,
            audioFormat = audioFormat,
            bufferSize = bufferSize
        )
    }

    /**
     * Release audio recorder resources
     *
     * Must be called when done with audio recording.
     * Releases native audio resources and stops recording.
     */
    fun release() {
        stopRecording()

        try {
            audioRecord?.release()
        } catch (e: Exception) {
            // Ignore release errors
        }

        audioRecord = null
    }

    /**
     * Get recording state description for debugging
     */
    fun getStateDescription(): String {
        return when {
            audioRecord == null -> "Not initialized"
            isRecording -> "Recording"
            audioRecord?.state == AudioRecord.STATE_INITIALIZED -> "Ready"
            else -> "Error"
        }
    }
}

/**
 * Audio configuration data class
 *
 * Contains audio format settings used for recording.
 * Useful for debugging and service configuration.
 */
data class AudioConfig(
    val sampleRate: Int,
    val channelConfig: Int,
    val audioFormat: Int,
    val bufferSize: Int
) {
    /**
     * Get human-readable description
     */
    fun getDescription(): String {
        val channels = when (channelConfig) {
            AudioFormat.CHANNEL_IN_MONO -> "Mono"
            AudioFormat.CHANNEL_IN_STEREO -> "Stereo"
            else -> "Unknown"
        }

        val format = when (audioFormat) {
            AudioFormat.ENCODING_PCM_16BIT -> "PCM 16-bit"
            AudioFormat.ENCODING_PCM_8BIT -> "PCM 8-bit"
            else -> "Unknown"
        }

        return "Sample Rate: ${sampleRate}Hz, Channels: $channels, Format: $format, Buffer: ${bufferSize} bytes"
    }
}

/**
 * Audio permission helper
 *
 * Provides permission-related constants and utilities
 */
object AudioPermissions {
    const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
    const val PERMISSION_REQUEST_CODE = 1001

    /**
     * Check if audio recording is supported on device
     */
    fun isAudioRecordingSupported(context: Context): Boolean {
        return try {
            val packageManager = context.packageManager
            packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get permission rationale message for user
     */
    fun getPermissionRationale(): String {
        return "This app needs microphone access to transcribe your conversations " +
                "and provide real-time coaching. Your audio is processed locally on " +
                "your device and is never stored or shared."
    }
}

/**
 * Audio recorder state
 */
sealed class AudioRecorderState {
    /** Not initialized */
    object NotInitialized : AudioRecorderState()

    /** Ready to record */
    object Ready : AudioRecorderState()

    /** Currently recording */
    object Recording : AudioRecorderState()

    /** Error occurred */
    data class Error(val message: String) : AudioRecorderState()
}