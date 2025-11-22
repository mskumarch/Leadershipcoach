package com.meetingcoach.leadershipconversationcoach.domain.models

import java.util.UUID

/**
 * ChatMessage - Message Model for Chat Interface
 *
 * Represents any message shown in the chat screen:
 * - Coaching nudges (URGENT_NUDGE, IMPORTANT_PROMPT, HELPFUL_TIP, CONTEXT)
 * - User questions (USER_QUESTION)
 * - AI responses (AI_RESPONSE)
 * - Transcript text (TRANSCRIPT)
 * - Instructions (INSTRUCTION)
 *
 * Architecture: Domain Layer - Models
 *
 * Last Updated: November 2025
 */
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val type: MessageType,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val speaker: Speaker? = null,
    val priority: Priority = Priority.INFO,
    val metadata: MessageMetadata? = null
) {
    /**
     * Can this message be dismissed by the user?
     */
    val isDismissable: Boolean
        get() = when (type) {
            MessageType.URGENT_NUDGE -> true
            MessageType.IMPORTANT_PROMPT -> true
            MessageType.HELPFUL_TIP -> true
            MessageType.CONTEXT -> true
            else -> false
        }

    /**
     * Does this message have action buttons?
     */
    val hasActionButtons: Boolean
        get() = when (type) {
            MessageType.URGENT_NUDGE -> true  // "Done" and "Dismiss" buttons
            else -> false
        }

    /**
     * Get icon for this message type
     */
    fun getIcon(): String {
        return when (type) {
            MessageType.INSTRUCTION -> "🎯"
            MessageType.TRANSCRIPT -> "💬"
            MessageType.CONTEXT -> "📍"
            MessageType.URGENT_NUDGE -> "⚠️"
            MessageType.IMPORTANT_PROMPT -> "💡"
            MessageType.HELPFUL_TIP -> "✨"
            MessageType.USER_QUESTION -> "❓"
            MessageType.AI_RESPONSE -> "🤖"
        }
    }

    /**
     * Get label/title for this message type
     */
    fun getLabel(): String {
        return when (type) {
            MessageType.INSTRUCTION -> "INSTRUCTION"
            MessageType.TRANSCRIPT -> "TRANSCRIPT"
            MessageType.CONTEXT -> "CONTEXT"
            MessageType.URGENT_NUDGE -> "URGENT NUDGE"
            MessageType.IMPORTANT_PROMPT -> "IMPORTANT PROMPT"
            MessageType.HELPFUL_TIP -> "HELPFUL TIP"
            MessageType.USER_QUESTION -> "YOUR QUESTION"
            MessageType.AI_RESPONSE -> "AI RESPONSE"
        }
    }

    /**
     * Is this a coaching nudge?
     */
    fun isCoachingNudge(): Boolean {
        return type in listOf(
            MessageType.URGENT_NUDGE,
            MessageType.IMPORTANT_PROMPT,
            MessageType.HELPFUL_TIP,
            MessageType.CONTEXT
        )
    }

    /**
     * Is this from the user?
     */
    fun isFromUser(): Boolean {
        return type == MessageType.USER_QUESTION || speaker == Speaker.USER
    }

    /**
     * Is this from AI?
     */
    fun isFromAI(): Boolean {
        return type == MessageType.AI_RESPONSE
    }
}

/**
 * MessageMetadata - Additional message information
 */
data class MessageMetadata(
    val emotion: Emotion? = null,
    val confidence: Float? = null,
    val relatedNudgeId: String? = null,
    val actionTaken: Boolean = false
)