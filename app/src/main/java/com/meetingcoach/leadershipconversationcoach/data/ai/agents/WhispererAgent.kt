package com.meetingcoach.leadershipconversationcoach.data.ai.agents

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * The Whisperer Agent - Generates context-aware questions
 * Provides the perfect thing to say next based on the current stage and topic.
 */
class WhispererAgent(
    private val geminiModel: GenerativeModel
) : CoachingAgent<WhispererResult> {

    companion object {
        private const val TAG = "WhispererAgent"
    }

    override suspend fun process(
        transcript: List<ChatMessage>,
        context: Map<String, Any>
    ): WhispererResult? = withContext(Dispatchers.IO) {
        try {
            val recentTranscript = transcript
                .filter { it.type == MessageType.TRANSCRIPT }
                .takeLast(10)
                .joinToString("\n") { it.content }

            if (recentTranscript.isBlank()) return@withContext null

            val currentStage = context["stage"] as? String ?: "GOAL"
            val prompt = buildWhispererPrompt(recentTranscript, currentStage)
            val response = geminiModel.generateContent(prompt)
            parseWhispererResponse(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Whisperer processing failed", e)
            null
        }
    }

    private fun buildWhispererPrompt(transcript: String, stage: String): String {
        val stageGuidance = when (stage) {
            "GOAL" -> "Help them clarify what they want to achieve. Ask about desired outcomes."
            "REALITY" -> "Help them explore the current situation. Ask about facts, feelings, and obstacles."
            "OPTIONS" -> "Help them brainstorm solutions. Ask about possibilities and alternatives."
            "WAY_FORWARD" -> "Help them commit to action. Ask about next steps and accountability."
            else -> "Ask an open-ended question to move the conversation forward."
        }

        return """
            You are an expert 1:1 coach. The conversation is in the "$stage" stage.
            
            Stage Guidance: $stageGuidance
            
            Recent conversation:
            "$transcript"
            
            Generate ONE powerful, open-ended question that:
            1. Is specific to what they just said
            2. Moves the conversation forward in the $stage stage
            3. Is concise (under 15 words)
            
            Respond EXACTLY as valid JSON:
            {
              "question": "Your suggested question here",
              "rationale": "Why this question is powerful (1 sentence)"
            }
        """.trimIndent()
    }

    private fun parseWhispererResponse(response: String?): WhispererResult? {
        if (response.isNullOrBlank()) return null

        return try {
            val jsonString = response.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(jsonString)
            WhispererResult(
                suggestedQuestion = json.optString("question", "What would you like to focus on?"),
                rationale = json.optString("rationale", "")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse whisperer response", e)
            // Fallback
            WhispererResult(
                suggestedQuestion = "What would you like to focus on today?",
                rationale = "Opening question"
            )
        }
    }
}
