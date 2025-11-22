package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * SessionExtensions.kt - Extension Functions for Session Data
 *
 * Provides helper functions for working with lists of messages.
 * Note: TranscriptChunk extensions are in TranscriptChunkExtensions.kt
 *
 * Architecture: Domain Layer - Extensions
 * Last Updated: November 2025
 */

// ============================================================
// CHAT MESSAGE EXTENSIONS
// ============================================================

/**
 * Filter visible messages (not dismissed)
 */
fun List<ChatMessage>.filterVisible(): List<ChatMessage> {
    return filter { it.isDismissable }
}

/**
 * Filter coaching cards only
 */
fun List<ChatMessage>.filterCoachingCards(): List<ChatMessage> {
    return filter {
        it.type in listOf(
            MessageType.URGENT_NUDGE,
            MessageType.IMPORTANT_PROMPT,
            MessageType.HELPFUL_TIP,
            MessageType.CONTEXT
        )
    }
}

/**
 * Filter transcript messages only
 */
fun List<ChatMessage>.filterTranscripts(): List<ChatMessage> {
    return filter { it.type == MessageType.TRANSCRIPT }
}

/**
 * Filter messages needing action
 */
fun List<ChatMessage>.filterNeedsAction(): List<ChatMessage> {
    return filter { it.hasActionButtons }
}

/**
 * Get most recent message
 */
fun List<ChatMessage>.getMostRecent(): ChatMessage? {
    return maxByOrNull { it.timestamp }
}

/**
 * Check if message is urgent nudge
 */
fun ChatMessage.isUrgentNudge(): Boolean {
    return type == MessageType.URGENT_NUDGE
}

// ============================================================
// TRANSCRIPT CHUNK EXTENSIONS (Additional Helpers)
// ============================================================

/**
 * Check if transcript is from other speaker
 */
fun TranscriptChunk.isFromOther(): Boolean {
    return speaker == Speaker.OTHER
}

/**
 * Check if transcript is high confidence (>= 80%)
 */
fun TranscriptChunk.isHighConfidence(): Boolean {
    return confidence >= 0.8f
}

/**
 * Check if transcript is empathetic
 */
fun TranscriptChunk.isEmpathetic(): Boolean {
    return hasEmpatheticLanguage()
}

/**
 * Check if transcript is defensive
 */
fun TranscriptChunk.isDefensive(): Boolean {
    val defensivePatterns = listOf(
        "but i", "you don't understand", "that's not fair",
        "i already told you", "why are you", "you always",
        "you never"
    )
    val lowerText = text.lowercase()
    return defensivePatterns.any { lowerText.contains(it) }
}

// ============================================================
// SESSION STATUS EXTENSIONS
// ============================================================

/**
 * Check if session status is active
 */
fun SessionStatus.isActive(): Boolean {
    return this == SessionStatus.RECORDING || this == SessionStatus.PREPARING
}