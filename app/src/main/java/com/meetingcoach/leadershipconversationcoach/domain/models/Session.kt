package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * Session.kt - Complete Coaching Session Model
 *
 * Represents a complete coaching session with all associated data:
 * - Session configuration (mode, participants, focus areas)
 * - Chat messages (coaching cards, user questions, AI responses)
 * - Transcript chunks (real-time speech data)
 * - Session metrics (talk ratio, pace, sentiment, scores)
 * - Session state (idle, recording, paused, completed)
 * - Analytics data (questions asked, empathy shown, interruptions)
 * - Action items and notes
 *
 * This is the primary aggregate root for a coaching session.
 * All session-related data flows through this model.
 *
 * Clean Architecture: Domain Layer - Aggregate Root
 *
 * Created for: Complete Session Management
 * Last Updated: November 2025
 */

import java.util.UUID

/**
 * Represents a complete coaching session
 *
 * @property id Unique identifier for this session
 * @property mode Session type (ONE_ON_ONE, TEAM_MEETING, DIFFICULT_CONVERSATION)
 * @property state Current session state (IDLE, RECORDING, PAUSED, etc.)
 * @property startTime Unix timestamp when session started (ms)
 * @property endTime Unix timestamp when session ended (ms), null if ongoing
 * @property pausedTime Total time session was paused (ms)
 * @property participants List of participant names
 * @property messages All chat messages (cards, bubbles, transcripts)
 * @property transcriptChunks All real-time transcript data
 * @property metrics Current session metrics (updated in real-time)
 * @property focusAreas Custom coaching focuses enabled for this session
 * @property actionItems AI-generated action items from the session
 * @property tags User-defined tags for categorization
 * @property notes User's private notes about the session
 * @property summary AI-generated session summary
 * @property title Optional custom title for the session
 * @property lastUpdated Timestamp of last update
 *
 * Example Usage:
 * ```
 * val session = Session(
 *     mode = SessionMode.ONE_ON_ONE,
 *     status = SessionStatus.RECORDING,
 *     participants = listOf("Sarah Chen", "You")
 * )
 *
 * // Add a transcript chunk
 * val updatedSession = session.addTranscriptChunk(chunk)
 *
 * // Update metrics
 * val withMetrics = updatedSession.updateMetrics(newMetrics)
 *
 * // Complete session
 * val completed = withMetrics.complete()
 * ```
 */
data class Session(
    val id: String = UUID.randomUUID().toString(),
    val mode: SessionMode,
    val status: SessionStatus = SessionStatus.IDLE,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val pausedTime: Long = 0L,
    val participants: List<String> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val transcriptChunks: List<TranscriptChunk> = emptyList(),
    val metrics: SessionMetrics = SessionMetrics(),
    val focusAreas: List<String> = emptyList(),
    val actionItems: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val notes: String? = null,
    val summary: String? = null,
    val title: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
) {

    // ============================================================
    // SESSION STATE CHECKS
    // ============================================================

    /**
     * Returns true if session is currently idle (not started)
     */
    fun isIdle(): Boolean = status == SessionStatus.IDLE

    /**
     * Returns true if session is preparing to start
     */
    fun isPreparing(): Boolean = status == SessionStatus.PREPARING
    /**
     * Returns true if session is actively recording
     */
    fun isRecording(): Boolean = status == SessionStatus.RECORDING

    /**
     * Returns true if session is paused
     */
    fun isPaused(): Boolean = status == SessionStatus.PAUSED

    /**
     * Returns true if session is completed
     */
    fun isCompleted(): Boolean = status == SessionStatus.COMPLETED

    /**
     * Returns true if session has an error
     */
    fun hasError(): Boolean = status == SessionStatus.ERROR

    /**
     * Returns true if session is active (recording or paused)
     */
    fun isActive(): Boolean = status.isActive()

    /**
     * Returns true if session can be paused (currently recording)
     */
    fun canBePaused(): Boolean = isRecording()

    /**
     * Returns true if session can be resumed (currently paused)
     */
    fun canBeResumed(): Boolean = isPaused()

    /**
     * Returns true if session can be stopped (recording or paused)
     */
    fun canBeStopped(): Boolean = isActive()

    // ============================================================
    // DURATION CALCULATIONS
    // ============================================================

    /**
     * Returns total session duration in milliseconds (excluding paused time)
     */
    fun getDuration(): Long {
        val end = endTime ?: System.currentTimeMillis()
        return (end - startTime) - pausedTime
    }

    /**
     * Returns raw duration including paused time
     */
    fun getRawDuration(): Long {
        val end = endTime ?: System.currentTimeMillis()
        return end - startTime
    }

    /**
     * Returns duration formatted as HH:MM:SS
     */
    fun getFormattedDuration(): String {
        val durationMs = getDuration()
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / (1000 * 60)) % 60
        val hours = (durationMs / (1000 * 60 * 60))

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    /**
     * Returns duration formatted as "X min" or "X hr Y min"
     */
    fun getHumanReadableDuration(): String {
        val durationMs = getDuration()
        val totalMinutes = (durationMs / (1000 * 60)).toInt()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "$hours hr $minutes min"
            hours > 0 -> "$hours hr"
            else -> "$minutes min"
        }
    }

    // ============================================================
    // PARTICIPANT MANAGEMENT
    // ============================================================

    /**
     * Returns number of participants
     */
    fun getParticipantCount(): Int = participants.size

    /**
     * Returns true if this is a one-on-one session (2 participants)
     */
    fun isOneOnOne(): Boolean = participants.size == 2

    /**
     * Returns true if this is a group session (3+ participants)
     */
    fun isGroupSession(): Boolean = participants.size >= 3

    /**
     * Returns formatted participant list (e.g., "Sarah, Tom, and You")
     */
    fun getFormattedParticipants(): String {
        return when {
            participants.isEmpty() -> "No participants"
            participants.size == 1 -> participants[0]
            participants.size == 2 -> "${participants[0]} and ${participants[1]}"
            else -> {
                val first = participants.dropLast(1).joinToString(", ")
                "$first, and ${participants.last()}"
            }
        }
    }

    // ============================================================
    // MESSAGE MANAGEMENT
    // ============================================================

    /**
     * Returns visible messages only (not dismissed)
     */
    fun getVisibleMessages(): List<ChatMessage> = messages.filterVisible()

    /**
     * Returns coaching cards only
     */
    fun getCoachingCards(): List<ChatMessage> = messages.filterCoachingCards()

    /**
     * Returns transcript messages only
     */
    fun getTranscriptMessages(): List<ChatMessage> = messages.filterTranscripts()

    /**
     * Returns messages needing action
     */
    fun getMessagesNeedingAction(): List<ChatMessage> = messages.filterNeedsAction()

    /**
     * Returns count of coaching nudges given
     */
    fun getCoachingNudgeCount(): Int = getCoachingCards().size

    /**
     * Returns count of urgent nudges
     */
    fun getUrgentNudgeCount(): Int = messages.count { it.isUrgentNudge() }

    /**
     * Returns most recent message
     */
    fun getMostRecentMessage(): ChatMessage? = messages.getMostRecent()

    // ============================================================
    // TRANSCRIPT MANAGEMENT
    // ============================================================

    /**
     * Returns all transcript text as a single string
     */
    fun getFullTranscript(): String = transcriptChunks.toText()

    /**
     * Returns transcript chunks by speaker
     */
    fun getTranscriptBySpeaker(speaker: Speaker): List<TranscriptChunk> {
        return transcriptChunks.filterBySpeaker(speaker)
    }

    /**
     * Returns user's transcript chunks
     */
    fun getUserTranscript(): List<TranscriptChunk> {
        return transcriptChunks.filter { it.isFromUser() }
    }

    /**
     * Returns other speakers' transcript chunks
     */
    fun getOthersTranscript(): List<TranscriptChunk> {
        return transcriptChunks.filter { it.isFromOther() }
    }

    /**
     * Returns total word count across all speakers
     */
    fun getTotalWordCount(): Int = transcriptChunks.getTotalWordCount()

    /**
     * Returns most recent transcript chunk
     */
    fun getMostRecentTranscript(): TranscriptChunk? = transcriptChunks.getMostRecent()

    // ============================================================
    // ANALYTICS & INSIGHTS
    // ============================================================

    /**
     * Returns count of questions asked by user
     */
    fun getQuestionCount(): Int = transcriptChunks.count { it.isQuestion() && it.isFromUser() }

    /**
     * Returns count of open-ended questions
     */
    fun getOpenQuestionCount(): Int {
        return transcriptChunks.count { it.isOpenEndedQuestion() && it.isFromUser() }
    }

    /**
     * Returns count of empathetic phrases used
     */
    fun getEmpatheticPhraseCount(): Int {
        return transcriptChunks.count { it.isEmpathetic() && it.isFromUser() }
    }

    /**
     * Returns count of defensive phrases detected
     */
    fun getDefensivePhraseCount(): Int {
        return transcriptChunks.count { it.isDefensive() }
    }

    /**
     * Returns average confidence score across all transcripts
     */
    fun getAverageConfidence(): Float = transcriptChunks.getAverageConfidence()

    /**
     * Returns percentage of high-confidence transcripts (>= 80%)
     */
    fun getHighConfidencePercentage(): Int {
        if (transcriptChunks.isEmpty()) return 0
        val highConfCount = transcriptChunks.count { it.isHighConfidence() }
        return ((highConfCount.toFloat() / transcriptChunks.size) * 100).toInt()
    }

    // ============================================================
    // SESSION QUALITY ASSESSMENT
    // ============================================================

    /**
     * Returns overall session quality score (0-100)
     * Delegates to metrics.getOverallQualityScore()
     */
    fun getQualityScore(): Int = metrics.getOverallQualityScore()

    /**
     * Returns session grade (A+ to F)
     */
    fun getGrade(): String = metrics.getSessionGrade()

    /**
     * Returns true if this was a high-quality session (>= 80%)
     */
    fun isHighQualitySession(): Boolean = getQualityScore() >= 80

    /**
     * Returns true if session needs improvement (< 60%)
     */
    fun needsImprovement(): Boolean = getQualityScore() < 60

    /**
     * Returns key strengths identified in this session
     */
    fun getKeyStrengths(): List<String> {
        val strengths = mutableListOf<String>()

        if (metrics.isBalancedRatio()) {
            strengths.add("Balanced talk/listen ratio")
        }
        if (metrics.isAskingGoodQuestions()) {
            strengths.add("Asked quality open-ended questions")
        }
        if (metrics.isShowingEmpathy()) {
            strengths.add("Demonstrated empathy effectively")
        }
        if (metrics.hasAcceptableInterruptions()) {
            strengths.add("Minimal interruptions")
        }
        if (metrics.isNormalPace()) {
            strengths.add("Maintained good speaking pace")
        }
        if (!metrics.isStressed()) {
            strengths.add("Kept conversation calm and productive")
        }

        return strengths
    }

    /**
     * Returns areas for improvement identified in this session
     */
    fun getAreasForImprovement(): List<String> {
        val improvements = mutableListOf<String>()

        if (metrics.isTalkingTooMuch()) {
            improvements.add("Listen more, speak less")
        }
        if (metrics.isNotTalkingEnough()) {
            improvements.add("Speak up and contribute more")
        }
        if (!metrics.isAskingGoodQuestions()) {
            improvements.add("Ask more open-ended questions")
        }
        if (!metrics.isShowingEmpathy()) {
            improvements.add("Show more empathy and understanding")
        }
        if (metrics.hasProblematicInterruptions()) {
            improvements.add("Reduce interruptions, let others finish")
        }
        if (metrics.isTooFast()) {
            improvements.add("Slow down your speaking pace")
        }
        if (metrics.isStressed()) {
            improvements.add("Stay calm and manage stress better")
        }

        return improvements
    }

    /**
     * Returns a complete session summary with key stats
     */
    fun getSessionSummary(): String {
        return """
            Session Summary:
            ═══════════════════════════════════
            Mode: ${mode.getDisplayName()}
            Duration: ${getHumanReadableDuration()}
            Participants: ${getFormattedParticipants()}
            
            Metrics:
            • Talk Ratio: ${metrics.getFormattedTalkRatio()} (${metrics.getTalkRatioStatus()})
            • Pace: ${metrics.pace.getDisplayName()}
            • Sentiment: ${metrics.sentiment.getDisplayName()}
            • Temperature: ${metrics.getFormattedTemperature()}
            
            Performance:
            • Questions Asked: ${getQuestionCount()} (${getOpenQuestionCount()} open-ended)
            • Empathetic Phrases: ${getEmpatheticPhraseCount()}
            • Interruptions: ${metrics.interruptionCount}
            • Coaching Nudges: ${getCoachingNudgeCount()}
            
            Overall: ${getQualityScore()}% (${getGrade()})
            
            Strengths:
            ${getKeyStrengths().joinToString("\n") { "✓ $it" }}
            
            Areas for Improvement:
            ${getAreasForImprovement().joinToString("\n") { "→ $it" }}
        """.trimIndent()
    }

    // ============================================================
    // FOCUS AREAS
    // ============================================================

    /**
     * Returns true if any focus areas are enabled
     */
    fun hasFocusAreas(): Boolean = focusAreas.isNotEmpty()

    /**
     * Returns formatted focus areas list
     */
    fun getFormattedFocusAreas(): String {
        return if (focusAreas.isEmpty()) {
            "No specific focus areas"
        } else {
            focusAreas.joinToString(", ")
        }
    }

    // ============================================================
    // ACTION ITEMS & NOTES
    // ============================================================

    /**
     * Returns true if action items have been generated
     */
    fun hasActionItems(): Boolean = actionItems.isNotEmpty()

    /**
     * Returns count of action items
     */
    fun getActionItemCount(): Int = actionItems.size

    /**
     * Returns true if user has added notes
     */
    fun hasNotes(): Boolean = !notes.isNullOrBlank()

    /**
     * Returns true if AI has generated a summary
     */
    fun hasSummary(): Boolean = !summary.isNullOrBlank()

    // ============================================================
    // TAGS
    // ============================================================

    /**
     * Returns true if session has tags
     */
    fun hasTags(): Boolean = tags.isNotEmpty()

    /**
     * Returns formatted tags
     */
    fun getFormattedTags(): String {
        return if (tags.isEmpty()) {
            "No tags"
        } else {
            tags.joinToString(", ") { "#$it" }
        }
    }

    // ============================================================
    // TITLE MANAGEMENT
    // ============================================================

    /**
     * Returns display title for this session
     * Uses custom title if set, otherwise generates from mode and date
     */
    fun getDisplayTitle(): String {
        return title ?: generateDefaultTitle()
    }

    /**
     * Generates a default title based on mode and date
     */
    private fun generateDefaultTitle(): String {
        val modeStr = when (mode) {
            SessionMode.ONE_ON_ONE -> "1-on-1"
            SessionMode.TEAM_MEETING -> "Team Meeting"
            SessionMode.DIFFICULT_CONVERSATION -> "Difficult Conversation"
            SessionMode.ROLEPLAY -> "Roleplay Practice"
            SessionMode.DYNAMICS -> "Dynamics Strategy"
        }

        val date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
            .format(java.util.Date(startTime))

        return "$modeStr - $date"
    }

    // ============================================================
    // STATE TRANSITION METHODS (Immutable Updates)
    // ============================================================

    /**
     * Creates a copy with session started (IDLE -> RECORDING)
     */
    fun start(): Session {
        return copy(
            status = SessionStatus.RECORDING,
            startTime = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with session paused
     */
    fun pause(): Session {
        require(canBePaused()) { "Cannot pause session in status: $status" }
        return copy(
            status = SessionStatus.PAUSED,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with session resumed
     */
    fun resume(pauseDuration: Long): Session {
        require(canBeResumed()) { "Cannot resume session in status: $status" }
        return copy(
            status = SessionStatus.RECORDING,
            pausedTime = pausedTime + pauseDuration,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with session completed
     */
    fun complete(): Session {
        return copy(
            status = SessionStatus.COMPLETED,
            endTime = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with error state
     */
    fun markAsError(): Session {
        return copy(
            status = SessionStatus.ERROR,
            lastUpdated = System.currentTimeMillis()
        )
    }

    // ============================================================
    // DATA UPDATE METHODS (Immutable Updates)
    // ============================================================

    /**
     * Creates a copy with a new message added
     */
    fun addMessage(message: ChatMessage): Session {
        return copy(
            messages = messages + message,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with a new transcript chunk added
     */
    fun addTranscriptChunk(chunk: TranscriptChunk): Session {
        return copy(
            transcriptChunks = transcriptChunks + chunk,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated metrics
     */
    fun updateMetrics(newMetrics: SessionMetrics): Session {
        return copy(
            metrics = newMetrics,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated participants
     */
    fun updateParticipants(newParticipants: List<String>): Session {
        return copy(
            participants = newParticipants,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with a new action item added
     */
    fun addActionItem(item: String): Session {
        return copy(
            actionItems = actionItems + item,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated action items
     */
    fun updateActionItems(newItems: List<String>): Session {
        return copy(
            actionItems = newItems,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated notes
     */
    fun updateNotes(newNotes: String): Session {
        return copy(
            notes = newNotes,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated summary
     */
    fun updateSummary(newSummary: String): Session {
        return copy(
            summary = newSummary,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with a new tag added
     */
    fun addTag(tag: String): Session {
        return copy(
            tags = (tags + tag).distinct(),
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with a tag removed
     */
    fun removeTag(tag: String): Session {
        return copy(
            tags = tags - tag,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with custom title
     */
    fun updateTitle(newTitle: String): Session {
        return copy(
            title = newTitle,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated focus areas
     */
    fun updateFocusAreas(newFocusAreas: List<String>): Session {
        return copy(
            focusAreas = newFocusAreas,
            lastUpdated = System.currentTimeMillis()
        )
    }

    // ============================================================
    // EXPORT HELPERS
    // ============================================================

    /**
     * Returns session data formatted for export
     */
    fun toExportString(format: ExportFormat): String {
        return when (format) {
            ExportFormat.TEXT -> toPlainText()
            ExportFormat.MARKDOWN -> toMarkdown()
            ExportFormat.PDF -> toPlainText() // PDF rendering handled separately
            ExportFormat.JSON -> "JSON export handled by serialization"
            ExportFormat.CSV -> toPlainText()
        }
    }

    private fun toPlainText(): String {
        return """
            ${getDisplayTitle()}
            ${"=".repeat(50)}
            
            ${getSessionSummary()}
            
            ${if (hasActionItems()) "\nAction Items:\n${actionItems.joinToString("\n") { "• $it" }}\n" else ""}
            ${if (hasNotes()) "\nNotes:\n$notes\n" else ""}
            
            Full Transcript:
            ${"=".repeat(50)}
            ${getFullTranscript()}
        """.trimIndent()
    }

    private fun toMarkdown(): String {
        return """
            # ${getDisplayTitle()}
            
            ## Overview
            - **Mode**: ${mode.getDisplayName()}
            - **Duration**: ${getHumanReadableDuration()}
            - **Participants**: ${getFormattedParticipants()}
            - **Grade**: ${getGrade()} (${getQualityScore()}%)
            
            ## Metrics
            - **Talk Ratio**: ${metrics.getFormattedTalkRatio()}
            - **Questions**: ${getQuestionCount()} total, ${getOpenQuestionCount()} open-ended
            - **Empathy**: ${getEmpatheticPhraseCount()} phrases
            - **Interruptions**: ${metrics.interruptionCount}
            
            ## Strengths
            ${getKeyStrengths().joinToString("\n") { "- $it" }}
            
            ## Areas for Improvement
            ${getAreasForImprovement().joinToString("\n") { "- $it" }}
            
            ${if (hasActionItems()) "\n## Action Items\n${actionItems.joinToString("\n") { "- [ ] $it" }}\n" else ""}
            ${if (hasNotes()) "\n## Notes\n$notes\n" else ""}
            
            ## Transcript
            ```
            ${getFullTranscript()}
            ```
        """.trimIndent()
    }
}