package com.meetingcoach.leadershipconversationcoach.data.ai

import android.content.Context
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.domain.models.Priority
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.domain.models.TranscriptChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * CoachingEngine - AI-Powered Coaching Logic
 *
 * The brain of the coaching system. Analyzes conversations in real-time and
 * generates contextual coaching nudges using Gemini AI.
 *
 * Key Responsibilities:
 * - Decide WHEN to analyze (smart batching, not every transcript)
 * - Maintain conversation context for AI
 * - Generate appropriate coaching nudges
 * - Prevent nudge spam (anti-spam logic)
 * - Detect patterns (talk ratio, questions, tension, interruptions)
 * - Convert AI responses to ChatMessage objects
 *
 * Smart Features:
 * - Batches transcripts before analysis (reduces API calls)
 * - Minimum 30 seconds between coaching nudges (prevents spam)
 * - Priority-based nudge filtering
 * - Context-aware coaching based on session mode
 * - Tracks coaching history to avoid repetition
 *
 * Architecture: Business Logic Layer
 *
 * Created for: Real-Time AI Coaching
 * Last Updated: November 2025
 */
class CoachingEngine(
    private val context: Context,
    private val geminiService: GeminiApiService
) {

    // Conversation state
    private val recentTranscripts = mutableListOf<TranscriptChunk>()
    private var sessionMode: SessionMode? = null
    private var sessionStartTime: Long = 0L
    private var lastNudgeTime: Long = 0L
    private val nudgeHistory = mutableListOf<String>()

    // Analysis job
    private val engineScope = CoroutineScope(Dispatchers.Default + Job())
    private var analysisJob: Job? = null

    // Configuration
    private var config = CoachingConfig()

    /**
     * Update coaching configuration
     */
    fun updateConfig(newConfig: CoachingConfig) {
        this.config = newConfig
        // Restart analysis if needed, or let the next loop pick it up
        // For immediate effect, we could cancel and restart the job, 
        // but simple variable update is safer for now.
    }

    // Callbacks
    private var onNudgeGenerated: ((ChatMessage) -> Unit)? = null

    companion object {
        private const val ANALYSIS_INTERVAL_MS = 60_000L      // Analyze every 30 seconds
        private const val MIN_NUDGE_INTERVAL_MS = 60_000L     // Min 30s between nudges
        private const val TRANSCRIPT_CONTEXT_WINDOW = 10      // Keep last 10 transcript chunks
        private const val MAX_NUDGE_HISTORY = 20              // Remember last 20 nudges
    }

    // ============================================================
    // Public API
    // ============================================================

    /**
     * Start coaching engine for a new session
     *
     * @param mode Session mode (ONE_ON_ONE, TEAM_MEETING, etc.)
     * @param onNudge Callback when coaching nudge is generated
     */
    fun startSession(
        mode: SessionMode,
        onNudge: (ChatMessage) -> Unit
    ) {
        // Initialize session
        sessionMode = mode
        sessionStartTime = System.currentTimeMillis()
        lastNudgeTime = 0L
        onNudgeGenerated = onNudge

        // Clear state
        recentTranscripts.clear()
        nudgeHistory.clear()

        // Start periodic analysis
        startPeriodicAnalysis()
    }

    /**
     * Stop coaching engine
     */
    fun stopSession() {
        analysisJob?.cancel()
        analysisJob = null

        recentTranscripts.clear()
        nudgeHistory.clear()
        sessionMode = null
        onNudgeGenerated = null
    }

    /**
     * Add new transcript chunk for analysis
     *
     * @param chunk New transcript from STT
     */
    fun addTranscript(chunk: TranscriptChunk) {
        // Only process final results, not partial
        if (chunk.isPartial) return

        // Add to recent transcripts
        recentTranscripts.add(chunk)

        // Keep only recent context window
        if (recentTranscripts.size > TRANSCRIPT_CONTEXT_WINDOW) {
            recentTranscripts.removeAt(0)
        }
    }

    /**
     * Update session metrics for context-aware coaching
     *
     * @param metrics Current session metrics
     */
    fun updateMetrics(metrics: SessionMetrics) {
        // Check for immediate intervention triggers
        checkImmediateTriggers(metrics)
    }

    /**
     * User asks AI a question
     *
     * @param question User's question
     * @param conversationContext Recent conversation summary
     */
    suspend fun answerQuestion(
        question: String,
        conversationContext: String? = null
    ): ChatMessage {
        val context = conversationContext ?: getConversationContext()
        val answer = geminiService.answerCoachingQuestion(question, context)

        return ChatMessage(
            type = MessageType.AI_RESPONSE,
            content = answer ?: "I'm having trouble connecting. Please try again.",
            priority = Priority.INFO
        )
    }

    /**
     * User requests a suggested question
     */
    suspend fun suggestQuestion(): ChatMessage? {
        val context = getConversationContext()
        val mode = sessionMode?.name ?: "CONVERSATION"

        val question = geminiService.suggestQuestion(context, mode)

        return if (question != null) {
            ChatMessage(
                type = MessageType.HELPFUL_TIP,
                content = "Try asking: \"$question\"",
                priority = Priority.HELPFUL
            )
        } else {
            null
        }
    }

    /**
     * Analyze the session using AUDIO for deeper insights
     */
    suspend fun analyzeAudioSession(
        audioFile: java.io.File
    ): SessionMetrics? {
        val modeName = sessionMode?.name ?: "COACHING"
        val analysis = geminiService.analyzeAudioSession(audioFile, modeName)
        
        return if (analysis != null) {
            SessionMetrics(
                empathyScore = analysis.empathyScore,
                clarityScore = analysis.clarityScore,
                listeningScore = analysis.listeningScore,
                summary = analysis.summary,
                paceAnalysis = analysis.paceAnalysis,
                wordingAnalysis = analysis.wordingAnalysis,
                improvements = analysis.improvements,
                // We'll store the raw JSON in a temporary field in metrics if needed, 
                // or handle it in the ViewModel. For now, let's add it to metrics.
                aiTranscriptJson = analysis.transcriptJson
            )
        } else {
            null
        }
    }

    /**
     * Analyze the full session to generate final metrics (Text fallback)
     */
    suspend fun analyzeSession(
        transcripts: List<ChatMessage>
    ): SessionMetrics? {
        val fullText = transcripts
            .filter { it.type == MessageType.TRANSCRIPT }
            .joinToString("\n") { "${it.speaker?.name ?: "Unknown"}: ${it.content}" }
            
        if (fullText.isBlank()) return null
        
        val modeName = sessionMode?.name ?: "COACHING"
        val analysis = geminiService.analyzeSession(fullText, modeName)
        
        return if (analysis != null) {
            SessionMetrics(
                empathyScore = analysis.empathyScore,
                clarityScore = analysis.clarityScore,
                listeningScore = analysis.listeningScore,
                summary = analysis.summary,
                paceAnalysis = analysis.paceAnalysis,
                wordingAnalysis = analysis.wordingAnalysis,
                improvements = analysis.improvements
                // We preserve other calculated metrics if passed, but here we return just the AI ones
                // The ViewModel will merge them
            )
        } else {
            null
        }
    }

    // ============================================================
    // Private Analysis Methods
    // ============================================================

    /**
     * Start periodic analysis loop
     */
    private fun startPeriodicAnalysis() {
        analysisJob = engineScope.launch {
            while (true) {
                delay(config.analysisIntervalMs)

                // Only analyze if we have enough transcripts
                if (recentTranscripts.size >= 3) {
                    analyzeAndGenerateNudge()
                }
            }
        }
    }

    /**
     * Analyze recent conversation and generate coaching nudge if needed
     */
    private suspend fun analyzeAndGenerateNudge() {
        // Check if enough time has passed since last nudge
        val timeSinceLastNudge = System.currentTimeMillis() - lastNudgeTime
        if (timeSinceLastNudge < MIN_NUDGE_INTERVAL_MS) {
            return // Too soon for another nudge
        }

        // Get conversation context
        val transcript = getConversationContext()
        val mode = sessionMode?.name ?: "CONVERSATION"

        // Build metrics map for AI context
        val metricsMap = buildMetricsMap()

        // Analyze with Gemini
        val response = geminiService.analyzeConversation(
            recentTranscript = transcript,
            sessionMode = mode,
            currentMetrics = metricsMap
        )

        // If AI suggests a nudge, generate it
        if (response != null) {
            val nudge = convertToNudge(response)

            // Check if this is a duplicate
            if (!isDuplicateNudge(nudge.content)) {
                // Record nudge
                lastNudgeTime = System.currentTimeMillis()
                nudgeHistory.add(nudge.content)

                // Keep nudge history limited
                if (nudgeHistory.size > MAX_NUDGE_HISTORY) {
                    nudgeHistory.removeAt(0)
                }

                // Deliver nudge
                onNudgeGenerated?.invoke(nudge)
            }
        }
    }

    /**
     * Check for immediate intervention triggers (don't wait for periodic analysis)
     */
    private fun checkImmediateTriggers(metrics: SessionMetrics) {
        val timeSinceLastNudge = System.currentTimeMillis() - lastNudgeTime
        if (timeSinceLastNudge < MIN_NUDGE_INTERVAL_MS) {
            return // Too soon
        }

        // Check for critical situations
        // Check for critical situations
        val urgentNudge = when {
            // Talking WAY too much (>80%)
            metrics.talkRatio > 80 -> {
                ChatMessage(
                    type = MessageType.URGENT_NUDGE,
                    content = "You've been speaking for ${metrics.talkRatio}% of the time. " +
                            "Consider asking: \"What are your thoughts?\"",
                    priority = Priority.URGENT
                )
            }

            // High tension detected
            metrics.temperature > 75 -> {
                ChatMessage(
                    type = MessageType.URGENT_NUDGE,
                    content = "Tension is rising. Take a breath. Consider: \"I want to understand your perspective. Help me see what you're seeing.\"",
                    priority = Priority.URGENT
                )
            }

            // Many interruptions
            metrics.interruptionCount > 5 -> {
                ChatMessage(
                    type = MessageType.IMPORTANT_PROMPT,
                    content = "You've interrupted ${metrics.interruptionCount} times. Let others finish their thoughts before responding.",
                    priority = Priority.IMPORTANT
                )
            }
            
            // Filler Words Check (New)
            checkFillerWords() -> {
                ChatMessage(
                    type = MessageType.HELPFUL_TIP,
                    content = "Detected frequent filler words ('um', 'like'). Pause instead of filling the silence.",
                    priority = Priority.HELPFUL
                )
            }

            else -> null
        }

        // Deliver urgent nudge
        if (urgentNudge != null && !isDuplicateNudge(urgentNudge.content)) {
            lastNudgeTime = System.currentTimeMillis()
            nudgeHistory.add(urgentNudge.content)
            onNudgeGenerated?.invoke(urgentNudge)
        }
    }

    private fun checkFillerWords(): Boolean {
        // Get last 3 user transcripts
        val userChunks = recentTranscripts.takeLast(5).filter { it.isFromUser() }
        if (userChunks.isEmpty()) return false
        
        val fillerRegex = Regex("\\b(um|uh|like|you know|sort of)\\b", RegexOption.IGNORE_CASE)
        val totalFillers = userChunks.sumOf { fillerRegex.findAll(it.text).count() }
        
        // Trigger if > 3 fillers in last few chunks
        return totalFillers > 3
    }

    /**
     * Convert AI CoachingResponse to ChatMessage
     */
    private fun convertToNudge(response: CoachingResponse): ChatMessage {
        val messageType = when (response.type.uppercase()) {
            "URGENT" -> MessageType.URGENT_NUDGE
            "IMPORTANT" -> MessageType.IMPORTANT_PROMPT
            "HELPFUL" -> MessageType.HELPFUL_TIP
            else -> MessageType.CONTEXT
        }

        val priority = when (response.type.uppercase()) {
            "URGENT" -> Priority.URGENT
            "IMPORTANT" -> Priority.IMPORTANT
            "HELPFUL" -> Priority.HELPFUL
            else -> Priority.INFO
        }

        return ChatMessage(
            type = messageType,
            content = response.message,
            priority = priority,
            timestamp = response.timestamp
        )
    }

    /**
     * Get conversation context as string for AI
     */
    private fun getConversationContext(): String {
        return recentTranscripts
            .takeLast(TRANSCRIPT_CONTEXT_WINDOW)
            .joinToString("\n") { chunk ->
                "[${chunk.speaker?.getDisplayName() ?: "Unknown"}]: ${chunk.text}"
            }
    }

    /**
     * Build metrics map for AI context
     */
    private fun buildMetricsMap(): Map<String, Any> {
        // Calculate from recent transcripts
        val userWords = recentTranscripts.count { it.isFromUser() }
        val totalChunks = recentTranscripts.size
        val talkRatio = if (totalChunks > 0) {
            ((userWords.toFloat() / totalChunks) * 100).toInt()
        } else 50

        val questionCount = recentTranscripts.count {
            it.isFromUser() && it.isQuestion()
        }

        return mapOf(
            "talkRatio" to talkRatio,
            "questionCount" to questionCount,
            "transcriptCount" to totalChunks,
            "sessionDuration" to getSessionDurationMinutes()
        )
    }

    /**
     * Get session duration in minutes
     */
    private fun getSessionDurationMinutes(): Int {
        val elapsed = System.currentTimeMillis() - sessionStartTime
        return (elapsed / 60000).toInt()
    }

    /**
     * Check if this nudge is too similar to recent ones
     */
    private fun isDuplicateNudge(content: String): Boolean {
        // Simple duplicate detection - check if similar message in last 3 nudges
        val recentNudges = nudgeHistory.takeLast(3)

        return recentNudges.any { recent ->
            // Simple similarity check (could be improved with better algorithm)
            val similarity = calculateSimilarity(content.lowercase(), recent.lowercase())
            similarity > 0.7 // 70% similar = duplicate
        }
    }

    /**
     * Calculate string similarity (simple Levenshtein-based)
     */
    private fun calculateSimilarity(s1: String, s2: String): Float {
        val longer = if (s1.length > s2.length) s1 else s2
        val shorter = if (s1.length > s2.length) s2 else s1

        if (longer.isEmpty()) return 1.0f

        val editDistance = levenshteinDistance(longer, shorter)
        return (longer.length - editDistance).toFloat() / longer.length
    }

    /**
     * Calculate Levenshtein distance between two strings
     */
    private fun levenshteinDistance(s1: String, s2: String): Int {
        val len1 = s1.length
        val len2 = s2.length
        val dp = Array(len1 + 1) { IntArray(len2 + 1) }

        for (i in 0..len1) dp[i][0] = i
        for (j in 0..len2) dp[0][j] = j

        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }

        return dp[len1][len2]
    }

    // ============================================================
    // Configuration
    // ============================================================



    /**
     * Get current configuration
     */
    fun getConfig(): CoachingConfig = config
}

/**
 * Coaching engine configuration
 */
data class CoachingConfig(
    val analysisIntervalMs: Long = 30_000L,      // How often to analyze
    val minNudgeIntervalMs: Long = 30_000L,      // Min time between nudges
    val enableUrgentNudges: Boolean = true,      // Allow immediate interventions
    val enableHelpfulTips: Boolean = true,       // Show helpful suggestions
    val aggressiveness: NudgeAggressiveness = NudgeAggressiveness.BALANCED
)

/**
 * How aggressive coaching should be
 */
enum class NudgeAggressiveness {
    /** Fewer nudges, only critical moments */
    MINIMAL,

    /** Moderate nudges, balanced approach */
    BALANCED,

    /** Many nudges, very proactive coaching */
    AGGRESSIVE
}