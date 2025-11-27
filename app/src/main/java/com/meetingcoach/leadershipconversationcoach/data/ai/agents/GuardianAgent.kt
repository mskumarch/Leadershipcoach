package com.meetingcoach.leadershipconversationcoach.data.ai.agents

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * The Guardian Agent - Monitors behavior and provides nudges
 * Watches HOW you are speaking (pace, tone, listening quality).
 */
class GuardianAgent(
    private val geminiModel: GenerativeModel
) : CoachingAgent<GuardianResult> {

    companion object {
        private const val TAG = "GuardianAgent"
    }

    override suspend fun process(
        transcript: List<ChatMessage>,
        context: Map<String, Any>
    ): GuardianResult? = withContext(Dispatchers.IO) {
        try {
            val recentTranscript = transcript
                .filter { it.type == MessageType.TRANSCRIPT }
                .takeLast(15)
                .joinToString("\n") { it.content }

            if (recentTranscript.isBlank()) return@withContext null

            val prompt = buildGuardianPrompt(recentTranscript)
            val response = geminiModel.generateContent(prompt)
            parseGuardianResponse(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Guardian processing failed", e)
            null
        }
    }

    private fun buildGuardianPrompt(transcript: String): String {
        return """
            You are a behavioral coach monitoring a 1:1 conversation.
            
            Analyze this recent conversation for coaching quality:
            "$transcript"
            
            Check for these issues:
            - Is the manager talking too much? (Should be 30-40% max)
            - Are they asking closed questions instead of open ones?
            - Are they interrupting or not giving space?
            - Are they being judgmental instead of curious?
            - Are they rushing to solutions instead of exploring?
            
            If you detect an issue, provide ONE specific, actionable nudge.
            If the conversation is going well, respond with "NONE".
            
            Respond EXACTLY as valid JSON:
            {
              "type": "URGENT|HELPFUL|NONE",
              "message": "Your specific nudge (under 12 words)"
            }
        """.trimIndent()
    }

    private fun parseGuardianResponse(response: String?): GuardianResult? {
        if (response.isNullOrBlank()) return null

        return try {
            val jsonString = response.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(jsonString)
            val type = json.optString("type", "NONE")
            
            if (type == "NONE") return null
            
            GuardianResult(
                nudgeType = type,
                message = json.optString("message", "")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse guardian response", e)
            null
        }
    }
}
