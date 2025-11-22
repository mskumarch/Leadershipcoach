package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode

/**
 * SessionUiState - UI State for SessionViewModel
 *
 * Holds the current state of the session for UI rendering.
 * This is different from SessionState enum which tracks lifecycle (IDLE, RECORDING, etc.)
 *
 * This data class holds:
 * - Recording status
 * - Session configuration
 * - Duration tracking
 * - Messages/transcripts
 * - Real-time metrics
 */
data class SessionUiState(
    val isRecording: Boolean = false,
    val mode: SessionMode? = null,
    val startTime: Long? = null,
    val duration: String = "00:00",
    val messages: List<ChatMessage> = emptyList(),
    val talkRatio: Int = 0,
    val pace: String = "Normal",
    val sentiment: String = "Calm"
) {
    /**
     * Returns true if a session is configured and ready to start
     */
    fun isReadyToRecord(): Boolean = mode != null && !isRecording

    /**
     * Returns true if there are any messages
     */
    fun hasMessages(): Boolean = messages.isNotEmpty()

    /**
     * Returns elapsed time in milliseconds
     */
    fun getElapsedMs(): Long {
        return if (isRecording && startTime != null) {
            System.currentTimeMillis() - startTime
        } else {
            0L
        }
    }
}