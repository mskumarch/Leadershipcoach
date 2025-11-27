package com.meetingcoach.leadershipconversationcoach.data.ai.agents

import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage

/**
 * Base interface for all AI Coaching Agents.
 */
interface CoachingAgent<T> {
    /**
     * Process the current session context and return a result.
     * @param transcript The recent conversation history.
     * @param context Additional context (e.g., current stage, metrics).
     */
    suspend fun process(transcript: List<ChatMessage>, context: Map<String, Any>): T?
}

// Result Types

data class NavigatorResult(
    val currentStage: String, // e.g., "GOAL", "REALITY"
    val topicSummary: String
)

data class WhispererResult(
    val suggestedQuestion: String,
    val rationale: String
)

data class GuardianResult(
    val nudgeType: String, // "URGENT", "HELPFUL"
    val message: String
)
