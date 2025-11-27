package com.meetingcoach.leadershipconversationcoach.data.ai.agents

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * The Navigator Agent - Tracks GROW Model progression
 * Determines where the conversation is in the coaching framework.
 */
class NavigatorAgent(
    private val geminiModel: GenerativeModel
) : CoachingAgent<NavigatorResult> {

    companion object {
        private const val TAG = "NavigatorAgent"
    }

    override suspend fun process(
        transcript: List<ChatMessage>,
        context: Map<String, Any>
    ): NavigatorResult? = withContext(Dispatchers.IO) {
        try {
            val recentTranscript = transcript
                .filter { it.type == MessageType.TRANSCRIPT }
                .takeLast(10)
                .joinToString("\n") { it.content }

            if (recentTranscript.isBlank()) return@withContext null

            val prompt = buildNavigatorPrompt(recentTranscript)
            val response = geminiModel.generateContent(prompt)
            parseNavigatorResponse(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Navigator processing failed", e)
            null
        }
    }

    private fun buildNavigatorPrompt(transcript: String): String {
        return """
            You are a GROW Model expert analyzing a 1:1 coaching conversation.
            
            The GROW Model has 4 stages:
            - GOAL: Establishing what the person wants to achieve
            - REALITY: Understanding the current situation
            - OPTIONS: Exploring possible solutions
            - WAY_FORWARD: Committing to specific actions
            
            Analyze this recent conversation:
            "$transcript"
            
            Determine which stage the conversation is currently in.
            Also provide a brief summary of the current topic.
            
            Respond EXACTLY as valid JSON:
            {
              "stage": "GOAL|REALITY|OPTIONS|WAY_FORWARD",
              "topic": "Brief topic summary"
            }
        """.trimIndent()
    }

    private fun parseNavigatorResponse(response: String?): NavigatorResult? {
        if (response.isNullOrBlank()) return null

        return try {
            val jsonString = response.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(jsonString)
            NavigatorResult(
                currentStage = json.optString("stage", "GOAL"),
                topicSummary = json.optString("topic", "")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse navigator response", e)
            null
        }
    }
}
