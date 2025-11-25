package com.meetingcoach.leadershipconversationcoach.data.ai

import android.content.Context
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * GeminiApiService - Gemini AI Integration
 *
 * Handles all communication with Google's Gemini Pro API for AI-powered coaching.
 * This service wraps the Gemini SDK and provides coaching-specific methods.
 *
 * Features:
 * - Analyze conversation transcripts
 * - Generate coaching nudges
 * - Provide empathetic responses
 * - Context-aware suggestions
 * - Session summaries
 *
 * Cost: FREE (1,500 requests/day with free tier)
 * Model: gemini-pro (text-only, fast, efficient)
 *
 * Note: Keep API key secure - should be in BuildConfig or secure storage
 *
 * Architecture: Data Layer - AI Service
 *
 * Created for: AI Coaching Integration
 * Last Updated: November 2025
 */

class GeminiApiService(
    private val context: Context,
    private val apiKey: String
) {

    companion object {
        private const val TAG = "GeminiApiService"
    }

    // Gemini Flash model - using 'latest' alias for automatic updates
    private val generativeModel: GenerativeModel by lazy {
        Log.d(TAG, "Initializing Gemini Model: gemini-flash-latest")
        GenerativeModel(
            modelName = "gemini-flash-latest",
            apiKey = apiKey
        )
    }

    init {
        if (apiKey.isBlank()) {
            Log.e(TAG, "API Key is missing! Please check local.properties")
        }
    }

    /**
     * Analyze recent conversation and generate coaching suggestions
     */
    suspend fun analyzeConversation(
        recentTranscript: String,
        sessionMode: String,
        currentMetrics: Map<String, Any>
    ): CoachingResponse? = withContext(Dispatchers.IO) {
        if (recentTranscript.isBlank()) return@withContext null

        try {
            val prompt = buildAnalysisPrompt(recentTranscript, sessionMode, currentMetrics)
            val response = generativeModel.generateContent(prompt)
            val text = response.text

            parseCoachingResponse(text)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Generate a suggested question based on conversation context
     */
    suspend fun suggestQuestion(
        conversationContext: String,
        sessionMode: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = buildQuestionPrompt(conversationContext, sessionMode)
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Provide empathetic coaching response to user's question
     */
    suspend fun answerCoachingQuestion(
        userQuestion: String,
        conversationContext: String? = null
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = buildCoachingAnswerPrompt(userQuestion, conversationContext)
            
            // Direct call to Gemini
            val response = generativeModel.generateContent(prompt)
            val text = response.text
            
            text?.trim()
        } catch (e: Exception) {
            "I'm having trouble connecting right now. Please try again."
        }
    }

    /**
     * Detect if conversation is getting tense/heated
     */
    suspend fun detectTension(
        recentTranscript: String
    ): TensionAnalysis? = withContext(Dispatchers.IO) {
        try {
            val prompt = buildTensionDetectionPrompt(recentTranscript)
            val response = generativeModel.generateContent(prompt)
            parseTensionAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting tension: ${e.message}", e)
            null
        }
    }

    /**
     * Generate session summary at the end
     */
    /**
     * Generate session summary at the end
     */
    suspend fun generateSessionSummary(
        fullTranscript: String,
        sessionMetrics: Map<String, Any>
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = buildSummaryPrompt(fullTranscript, sessionMetrics)
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error generating summary: ${e.message}", e)
            null
        }
    }

    /**
     * Deep analysis of the entire session
     */
    suspend fun analyzeSession(
        fullTranscript: String,
        sessionMode: String
    ): SessionAnalysisResult? = withContext(Dispatchers.IO) {
        try {
            val prompt = buildSessionAnalysisPrompt(fullTranscript, sessionMode)
            val response = generativeModel.generateContent(prompt)
            parseSessionAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing session: ${e.message}", e)
            null
        }
    }

    // ============================================================
    // Private Helper Methods (Prompts & Parsing)
    // ============================================================

    private fun buildAnalysisPrompt(
        transcript: String,
        sessionMode: String,
        metrics: Map<String, Any>
    ): String {
        val talkRatio = metrics["talkRatio"] as? Int ?: 50
        return """
            You are an expert leadership coach.
            Session: $sessionMode. Talk Ratio: User $talkRatio%.
            Transcript: "$transcript"
            
            Provide ONE specific coaching nudge if needed.
            Format:
            TYPE: [URGENT/IMPORTANT/HELPFUL]
            MESSAGE: [Suggestion]
            
            If no coaching needed, say "NONE".
        """.trimIndent()
    }

    private fun buildQuestionPrompt(context: String, sessionMode: String): String {
        return "Suggest one open-ended question for this $sessionMode context: \"$context\""
    }

    private fun buildCoachingAnswerPrompt(question: String, context: String?): String {
        val ctx = context?.let { "Context: \"$it\"\n" } ?: ""
        return """
            You are a leadership coach.
            $ctx
            User asks: "$question"
            Answer helpfully and concisely (under 100 words).
        """.trimIndent()
    }

    private fun buildTensionDetectionPrompt(transcript: String): String {
        return """
            Analyze for tension (0-100).
            Transcript: "$transcript"
            Format:
            TENSION_LEVEL: [0-100]
            INDICATORS: [Signs]
            SUGGESTION: [Action]
        """.trimIndent()
    }

    private fun buildSummaryPrompt(transcript: String, metrics: Map<String, Any>): String {
        return "Summarize this coaching session with key points and feedback. Transcript: \"$transcript\""
    }

    private fun buildSessionAnalysisPrompt(transcript: String, sessionMode: String): String {
        return """
            You are an expert leadership coach analyzing a completed "$sessionMode" session.
            Transcript: "$transcript"
            
            Grade this session (0-100) on:
            1. Empathy: Understanding and validating others' feelings.
            2. Clarity: Communicating clearly and concisely.
            3. Listening: Asking questions and letting others speak.
            
            Format your response EXACTLY as follows:
            EMPATHY: [0-100]
            CLARITY: [0-100]
            LISTENING: [0-100]
            SUMMARY: [Brief summary of performance]
        """.trimIndent()
    }

    private fun parseSessionAnalysis(response: String?): SessionAnalysisResult? {
        if (response.isNullOrBlank()) return null
        
        val empathy = Regex("EMPATHY:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val clarity = Regex("CLARITY:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val listening = Regex("LISTENING:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val summary = Regex("SUMMARY:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "No summary available."
        
        return SessionAnalysisResult(empathy, clarity, listening, summary)
    }

    private fun parseCoachingResponse(response: String?): CoachingResponse? {
        if (response.isNullOrBlank() || response.contains("NONE", ignoreCase = true)) return null
        
        val typeMatch = Regex("TYPE:\\s*(.+)", RegexOption.IGNORE_CASE).find(response)
        val messageMatch = Regex("MESSAGE:\\s*(.+)", RegexOption.IGNORE_CASE).find(response)
        
        return CoachingResponse(
            type = typeMatch?.groupValues?.get(1)?.trim()?.uppercase() ?: "HELPFUL",
            message = messageMatch?.groupValues?.get(1)?.trim() ?: response.trim()
        )
    }

    private fun parseTensionAnalysis(response: String?): TensionAnalysis? {
        if (response.isNullOrBlank()) return null
        val level = Regex("TENSION_LEVEL:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        return TensionAnalysis(level, "", "")
    }
}

/**
 * Coaching response from AI
 */
data class CoachingResponse(
    val type: String,        // URGENT, IMPORTANT, HELPFUL, INFO
    val message: String,     // The coaching suggestion
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Tension analysis result
 */
data class TensionAnalysis(
    val level: Int,          // 0-100 tension level
    val indicators: String,  // What caused tension
    val suggestion: String   // How to de-escalate
)

/**
 * API usage statistics
 */
data class ApiUsageStats(
    val requestCount: Int,
    val lastRequestTime: Long,
    val dailyLimit: Int,
    val remainingRequests: Int
)

data class SessionAnalysisResult(
    val empathyScore: Int,
    val clarityScore: Int,
    val listeningScore: Int,
    val summary: String
)