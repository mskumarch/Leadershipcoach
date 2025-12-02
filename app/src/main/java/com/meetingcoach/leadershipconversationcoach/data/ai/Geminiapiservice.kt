package com.meetingcoach.leadershipconversationcoach.data.ai

import android.content.Context
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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
     * Detect personality style of the other person
     */
    suspend fun detectPersonality(
        recentTranscript: String
    ): PersonalityAnalysis? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.detectPersonality(recentTranscript)
            val response = generativeModel.generateContent(prompt)
            parsePersonalityAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting personality: ${e.message}", e)
            null
        }
    }

    /**
     * Analyze commitment quality
     */
    suspend fun analyzeCommitmentQuality(
        transcript: String
    ): CommitmentAnalysis? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.analyzeCommitmentQuality(transcript)
            val response = generativeModel.generateContent(prompt)
            parseCommitmentAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing commitment: ${e.message}", e)
            null
        }
    }

    /**
     * Detect Mindset (Growth vs Fixed)
     */
    suspend fun detectMindset(
        transcript: String
    ): MindsetAnalysis? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.detectMindset(transcript)
            val response = generativeModel.generateContent(prompt)
            parseMindsetAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting mindset: ${e.message}", e)
            null
        }
    }

    /**
     * Suggest deeper inquiry (The "Why" Ladder)
     */
    suspend fun deepenInquiry(
        lastQuestion: String,
        context: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.deepenInquiry(lastQuestion, context)
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error generating deep inquiry: ${e.message}", e)
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
     * Generate "One-Tap Follow-Up" email draft
     */
    suspend fun generateFollowUpMessage(
        summary: String,
        actionItems: String,
        decisions: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.generateFollowUpMessage(summary, actionItems, decisions)
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error generating follow-up: ${e.message}", e)
            null
        }
    }

    /**
     * Summarize article content
     */
    suspend fun summarizeArticle(
        title: String,
        content: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = CoachingPrompts.summarizeArticle(title, content)
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error summarizing article: ${e.message}", e)
            null
        }
    }

    /**
     * Generate a daily leadership tip
     */
    suspend fun generateDailyTip(): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Generate a single, short, actionable leadership tip for today.
                It should be inspiring and practical.
                Max 20 words.
                Do not use quotes or prefixes like "Tip:". Just the sentence.
            """.trimIndent()
            val response = generativeModel.generateContent(prompt)
            response.text?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error generating daily tip: ${e.message}", e)
            null
        }
    }

    /**
     * Deep analysis of the session using AUDIO (for Speaker ID and Tone)
     */
    suspend fun analyzeAudioSession(
        audioFile: File,
        sessionMode: String
    ): SessionAnalysisResult? = withContext(Dispatchers.IO) {
        try {
            if (!audioFile.exists()) {
                Log.e(TAG, "Audio file not found: ${audioFile.absolutePath}")
                return@withContext null
            }

            val audioBytes = audioFile.readBytes()
            
            // Build the prompt with audio
            val prompt = content {
                blob("audio/mp4", audioBytes) // Assuming m4a/mp4 format
                text(buildAudioAnalysisPrompt(sessionMode))
            }

            val response = generativeModel.generateContent(prompt)
            parseSessionAnalysis(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Error analyzing audio session: ${e.message}", e)
            null
        }
    }

    /**
     * Deep analysis of the entire session (Text fallback)
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

    /**
     * Clean up and structure the raw transcript
     */
    suspend fun cleanUpTranscript(
        rawTranscript: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                You are an expert editor. 
                Clean up the following raw speech-to-text transcript.
                
                Tasks:
                1. Fix grammar and punctuation.
                2. Identify speakers if possible (Label as "User" and "Other" based on context).
                3. Format as a clean dialogue script.
                4. Remove filler words (um, uh, like).
                
                Raw Transcript:
                "$rawTranscript"
                
                Output ONLY valid JSON in this format:
                [
                  {"speaker": "User", "text": "Hello, how are you?"},
                  {"speaker": "Other", "text": "I am good, thanks."}
                ]
            """.trimIndent()
            
            val response = generativeModel.generateContent(prompt)
            val text = response.text?.trim()
            
            // Simple cleanup to ensure it looks like JSON
            text?.removePrefix("```json")?.removePrefix("```")?.removeSuffix("```")?.trim()
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning transcript: ${e.message}", e)
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
            
            Provide ONE specific, actionable coaching nudge.
            Be concise (under 15 words). Direct and punchy.
            
            Format:
            TYPE: [URGENT/IMPORTANT/HELPFUL]
            MESSAGE: [Suggestion]
            
            If no coaching needed, say "NONE".
        """.trimIndent()
    }

    private fun buildQuestionPrompt(context: String, sessionMode: String): String {
        return "Suggest one powerful, open-ended question for this $sessionMode context. Context: \"$context\""
    }

    private fun buildCoachingAnswerPrompt(question: String, context: String?): String {
        val ctx = context?.let { "Context: \"$it\"\n" } ?: ""
        return """
            You are a seasoned leadership coach.
            $ctx
            User asks: "$question"
            
            Answer directly and concisely (under 50 words).
            Use a coaching tone: empowering, specific, and action-oriented.
            Avoid vague generalizations. Give a concrete step or perspective.
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
        return """
            Summarize this coaching session.
            Transcript: "$transcript"
            
            Format exactly as:
            * [Key Point 1]
            * [Key Point 2]
            * [Key Point 3]
            
            ### Takeaways
            * [Actionable Takeaway 1]
            * [Actionable Takeaway 2]
        """.trimIndent()
    }

    private fun buildAudioAnalysisPrompt(sessionMode: String): String {
        return """
            You are an expert leadership coach analyzing the AUDIO of a '$sessionMode' session.
            
            Tasks:
            1. SPEAKER ID: Identify different speakers (Speaker 1, Speaker 2, etc.).
            2. TONE ANALYSIS: Listen for hesitation, confidence, sarcasm, or tension.
            3. TRANSCRIPT: Generate a high-quality transcript with speaker labels.
            4. DEEP INSIGHTS: Extract commitments, analyze questions, and detect patterns.
            
            Analyze the following metrics (0-100):
            1. Empathy
            2. Clarity
            3. Listening
            
            Also analyze:
            - PACE: Was the speaker too fast/slow?
            - WORDING: Filler words vs Power words?
            - IMPROVEMENTS: 3 actionable tips.
            
            DEEP INSIGHTS:
            - COMMITMENTS: Extract any promises or action items mentioned (e.g., "I will send the report by Friday")
            - QUESTIONS: Count open-ended vs closed questions asked by the manager
            - TALK RATIO: Estimate percentage of time each speaker talked
            - INTERRUPTIONS: Did the manager interrupt? How many times?
            
            Format your response EXACTLY as valid JSON:
            {
              "score_1": [0-100],
              "score_2": [0-100],
              "score_3": [0-100],
              "summary": "Detailed analysis of tone and dynamics...",
              "pace_analysis": "Analysis...",
              "wording_analysis": "Analysis...",
              "improvements": ["Point 1", "Point 2", "Point 3"],
              "commitments": ["Commitment 1", "Commitment 2"],
              "open_questions": 5,
              "closed_questions": 3,
              "manager_talk_percentage": 40,
              "interruption_count": 2,
              "transcript": [
                {"speaker": "Speaker 1", "text": "..."},
                {"speaker": "Speaker 2", "text": "..."}
              ],
              "dynamics_analysis": {
                "power_dynamics_score": 85,
                "subtext_signals": [
                  {"quote": "Let's take this offline", "type": "Deflection", "analysis": "Avoidance..."}
                ],
                "strategies": [
                  {"title": "Build Rapport", "description": "Start with..."}
                ]
              }
            }
        """.trimIndent()
    }

    private fun buildSessionAnalysisPrompt(transcript: String, sessionMode: String): String {
        val basePrompt = "You are an expert leadership coach analyzing a completed '$sessionMode' session.\nTranscript: \"$transcript\"\n"
        
        val specificInstructions = when (sessionMode) {
            "TEAM_MEETING" -> """
                Analyze the team dynamics:
                1. Alignment: Who is aligned? Who is against? (Score 0-100)
                2. Participation: Was it balanced? (Score 0-100)
                3. Clarity: Were goals clear? (Score 0-100)
                
                SUMMARY should focus on: Key conflicts, alignment issues, and hidden tensions.
            """.trimIndent()
            
            "DIFFICULT_CONVERSATION" -> """
                Analyze the conflict resolution:
                1. Empathy: Did the leader validate feelings? (Score 0-100)
                2. Objectivity: Did they stick to facts? (Score 0-100)
                3. De-escalation: Did tension drop? (Score 0-100)
                
                SUMMARY should focus on: Emotional shifts, resistance points, and resolution.
            """.trimIndent()
            
            else -> """
                Analyze the coaching/1:1 session:
                1. Empathy: Understanding feelings. (Score 0-100)
                2. Clarity: Clear communication. (Score 0-100)
                3. Listening: Asking open questions. (Score 0-100)
                
                SUMMARY should focus on: Rapport, key takeaways, and coaching impact.
            """.trimIndent()
        }

        return """
            $basePrompt
            $specificInstructions
            
            Also analyze:
            - PACE: Was the speaker too fast/slow? (e.g., "Good pace", "Too fast")
            - WORDING: Did they use filler words or power words? (e.g., "Used 'um' frequently", "Strong decisive language")
            - IMPROVEMENTS: List 3 specific actionable improvements.
            
            Format your response EXACTLY as valid JSON:
            {
              "score_1": [0-100],
              "score_2": [0-100],
              "score_3": [0-100],
              "summary": "Detailed analysis...",
              "pace_analysis": "Analysis...",
              "wording_analysis": "Analysis...",
              "improvements": ["Point 1", "Point 2", "Point 3"],
              "key_takeaways": ["Takeaway 1", "Takeaway 2"],
              "dynamics_analysis": {
                "power_dynamics_score": 85,
                "subtext_signals": [],
                "strategies": []
              }
            }
        """.trimIndent()
    }

    private fun parseSessionAnalysis(response: String?): SessionAnalysisResult? {
        if (response.isNullOrBlank()) return null
        
        try {
            // Clean up JSON string (remove markdown blocks if present)
            val jsonString = response.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
                
            val json = org.json.JSONObject(jsonString)
            
            val score1 = json.optInt("score_1", 50)
            val score2 = json.optInt("score_2", 50)
            val score3 = json.optInt("score_3", 50)
            val summary = json.optString("summary", "No summary available.")
            val pace = json.optString("pace_analysis", "No pace analysis.")
            val wording = json.optString("wording_analysis", "No wording analysis.")
            
            // Handle improvements array
            val improvementsArray = json.optJSONArray("improvements")
            val improvements = if (improvementsArray != null) {
                (0 until improvementsArray.length()).joinToString(" | ") { improvementsArray.getString(it) }
            } else {
                json.optString("improvements", "No improvements listed.")
            }
            
            // Handle takeaways (optional, can be appended to summary)
            val takeawaysArray = json.optJSONArray("key_takeaways")
            val takeaways = if (takeawaysArray != null) {
                (0 until takeawaysArray.length()).joinToString("\n• ") { takeawaysArray.getString(it) }
            } else ""
            
            val finalSummary = if (takeaways.isNotEmpty()) "$summary\n\nKey Takeaways:\n• $takeaways" else summary

            // Handle transcript array
            val transcriptArray = json.optJSONArray("transcript")
            val transcriptJson = transcriptArray?.toString()

            // Deep Insights
            val commitmentsArray = json.optJSONArray("commitments")
            val commitments = if (commitmentsArray != null) {
                (0 until commitmentsArray.length()).map { commitmentsArray.getString(it) }
            } else emptyList()

            val openQuestions = json.optInt("open_questions", 0)
            val closedQuestions = json.optInt("closed_questions", 0)
            val managerTalkPercentage = json.optInt("manager_talk_percentage", 50)
            val interruptionCount = json.optInt("interruption_count", 0)
            
            // Dynamics Analysis
            val dynamicsJson = json.optJSONObject("dynamics_analysis")?.toString()

            return SessionAnalysisResult(
                score1, score2, score3, finalSummary, pace, wording, improvements, transcriptJson,
                commitments, openQuestions, closedQuestions, managerTalkPercentage, interruptionCount,
                dynamicsJson
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing session analysis JSON: ${e.message}", e)
            // Fallback to old regex parsing if JSON fails
            return parseSessionAnalysisRegex(response)
        }
    }

    private fun parseSessionAnalysisRegex(response: String): SessionAnalysisResult? {
        val score1 = Regex("SCORE_1:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val score2 = Regex("SCORE_2:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val score3 = Regex("SCORE_3:\\s*(\\d+)").find(response)?.groupValues?.get(1)?.toIntOrNull() ?: 50
        val summary = Regex("SUMMARY:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "No summary available."
        val pace = Regex("PACE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "No pace analysis."
        val wording = Regex("WORDING:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "No wording analysis."
        val improvements = Regex("IMPROVEMENTS:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "No improvements listed."
        
        return SessionAnalysisResult(score1, score2, score3, summary, pace, wording, improvements, null, emptyList(), 0, 0, 50, 0, null)
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
    private fun parsePersonalityAnalysis(response: String?): PersonalityAnalysis? {
        if (response.isNullOrBlank()) return null
        
        val style = Regex("STYLE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "UNKNOWN"
        val confidence = Regex("CONFIDENCE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "Low"
        val advice = Regex("ADVICE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "Adapt to their style."
        
        return PersonalityAnalysis(style, confidence, advice)
    }

    private fun parseCommitmentAnalysis(response: String?): CommitmentAnalysis? {
        if (response.isNullOrBlank()) return null
        
        val type = Regex("TYPE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "NONE"
        val advice = Regex("ADVICE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: ""
        
        return CommitmentAnalysis(type, advice)
    }

    private fun parseMindsetAnalysis(response: String?): MindsetAnalysis? {
        if (response.isNullOrBlank()) return null
        
        val mindset = Regex("MINDSET:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: "NEUTRAL"
        val phrase = Regex("PHRASE:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: ""
        val suggestion = Regex("SUGGESTION:\\s*(.+)").find(response)?.groupValues?.get(1)?.trim() ?: ""
        
        return MindsetAnalysis(mindset, phrase, suggestion)
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
 * Personality analysis result
 */
data class PersonalityAnalysis(
    val style: String,       // DRIVER, ANALYTICAL, AMIABLE, EXPRESSIVE
    val confidence: String,  // High, Medium, Low
    val advice: String       // How to adapt
)

data class CommitmentAnalysis(
    val type: String,        // STRONG, VAGUE, NONE
    val advice: String       // Suggestion to lock it in
)

data class MindsetAnalysis(
    val mindset: String,     // FIXED, GROWTH, NEUTRAL
    val phrase: String,      // The trigger phrase
    val suggestion: String   // Reframe suggestion
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
    val summary: String,
    val paceAnalysis: String,
    val wordingAnalysis: String,
    val improvements: String,
    val transcriptJson: String? = null,
    // Deep Insights
    val commitments: List<String> = emptyList(),
    val openQuestions: Int = 0,
    val closedQuestions: Int = 0,
    val managerTalkPercentage: Int = 50,
    val interruptionCount: Int = 0,
    val dynamicsAnalysisJson: String? = null
)