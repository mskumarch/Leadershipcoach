package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * SessionState - Live Session State Model
 *
 * Represents the current state of an active coaching session.
 * This is different from Session (which is for saved/historical sessions).
 *
 * Used by SessionViewModel to manage real-time session state.
 *
 * Architecture: Domain Layer - State Model
 *
 * Created for: Live Session State Management
 * Last Updated: November 2025
 */
data class SessionState(
    // Session status
    val isRecording: Boolean = false,
    val isPaused: Boolean = false,
    val mode: SessionMode? = null,
    val startTime: Long? = null,
    val duration: String = "00:00",

    // Messages (transcript, nudges, Q&A)
    val messages: List<ChatMessage> = emptyList(),

    // Real-time metrics
    val metrics: SessionMetrics = SessionMetrics(),

    // UI state
    val partialTranscript: String = "",
    val isProcessing: Boolean = false,
    val isReflectionPending: Boolean = false, // New
    val error: String? = null
)