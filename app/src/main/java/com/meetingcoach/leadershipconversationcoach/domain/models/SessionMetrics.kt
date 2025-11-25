package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * SessionMetrics.kt - Real-Time Conversation Metrics Model
 *
 * Tracks all metrics displayed during a live coaching session:
 * - Talk ratio (you vs others percentage)
 * - Speaking pace (fast/normal/slow)
 * - Overall sentiment (calm/focused/stressed/anxious)
 * - Conversation temperature (tension level 0-100)
 * - Progress ring scores (empathy, listening, clarity)
 *
 * These metrics are:
 * - Calculated in real-time from TranscriptChunks
 * - Displayed in the floating status pill
 * - Shown in progress rings on Coach screen
 * - Used by AI to generate context-aware nudges
 *
 * Clean Architecture: Domain Layer - Core Business Model
 *
 * Created for: Real-Time Metrics Tracking
 * Last Updated: November 2025
 */

/**
 * Real-time metrics for an active coaching session
 *
 * @property talkRatio Percentage of time the user is speaking (0-100)
 * @property pace Current speaking pace (FAST/NORMAL/SLOW)
 * @property sentiment Overall conversation sentiment (CALM/FOCUSED/STRESSED/ANXIOUS)
 * @property temperature Conversation tension level (0-100, higher = more tense)
 * @property empathyScore Empathy ring score (0-100, based on empathetic language)
 * @property listeningScore Listening ring score (0-100, based on talk/listen ratio)
 * @property clarityScore Clarity ring score (0-100, based on clear questions)
 * @property totalWords Total words spoken across all speakers
 * @property userWords Words spoken by the user
 * @property otherWords Words spoken by others
 * @property questionCount Total questions asked
 * @property openQuestionCount Open-ended questions asked
 * @property empatheticPhraseCount Empathetic phrases detected
 * @property interruptionCount Number of interruptions detected
 * @property lastUpdated Timestamp of last metric update
 *
 * Example Usage:
 * ```
 * val metrics = SessionMetrics(
 *     talkRatio = 35,
 *     pace = Pace.NORMAL,
 *     sentiment = Sentiment.FOCUSED,
 *     temperature = 25
 * )
 *
 * if (metrics.isBalancedRatio()) {
 *     // Good talk/listen balance
 * }
 * ```
 */
data class SessionMetrics(
    val talkRatio: Int = 0,
    val pace: Pace = Pace.NORMAL,
    val sentiment: Sentiment = Sentiment.CALM,
    val temperature: Int = 0,
    val empathyScore: Int = 0,
    val listeningScore: Int = 0,
    val clarityScore: Int = 0,
    val totalWords: Int = 0,
    val userWords: Int = 0,
    val otherWords: Int = 0,
    val questionCount: Int = 0,
    val openQuestionCount: Int = 0,
    val empatheticPhraseCount: Int = 0,
    val interruptionCount: Int = 0,
    val summary: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
) {

    // ============================================================
    // TALK RATIO HELPERS
    // ============================================================

    /**
     * Returns true if talk ratio is balanced (30-40% user speaking)
     * Ideal for coaching: user speaks 30-40%, others speak 60-70%
     */
    fun isBalancedRatio(): Boolean = talkRatio in 30..40

    /**
     * Returns true if user is speaking too much (>60%)
     */
    fun isTalkingTooMuch(): Boolean = talkRatio > 60

    /**
     * Returns true if user is speaking too little (<20%)
     */
    fun isNotTalkingEnough(): Boolean = talkRatio < 20

    /**
     * Returns talk ratio category for display
     */
    fun getTalkRatioStatus(): String = when {
        talkRatio < 20 -> "Too Quiet"
        talkRatio in 20..29 -> "Good Listening"
        talkRatio in 30..40 -> "Balanced"
        talkRatio in 41..60 -> "Moderate"
        else -> "Talking Too Much"
    }

    /**
     * Returns color indicator for talk ratio
     * Green = Balanced, Yellow = Okay, Red = Issue
     */
    fun getTalkRatioColor(): String = when {
        isBalancedRatio() -> "GREEN"
        talkRatio in 20..60 -> "YELLOW"
        else -> "RED"
    }

    // ============================================================
    // PACE HELPERS
    // ============================================================

    /**
     * Returns true if pace is normal (ideal)
     */
    fun isNormalPace(): Boolean = pace == Pace.NORMAL

    /**
     * Returns true if speaking too fast (may seem rushed)
     */
    fun isTooFast(): Boolean = pace == Pace.FAST

    /**
     * Returns true if speaking too slow (may lose engagement)
     */
    fun isTooSlow(): Boolean = pace == Pace.SLOW

    /**
     * Returns coaching tip for current pace
     */
    fun getPaceCoachingTip(): String = when (pace) {
        Pace.FAST -> "Consider slowing down. Pauses show confidence."
        Pace.VERY_SLOW -> "You're speaking very slow. Speed up to ensure clarity."
        Pace.SLOW -> "Your pace is thoughtful. Ensure you maintain engagement."
        Pace.NORMAL -> "Great pace! You're speaking at a good speed."
        Pace.FAST -> "Consider slowing down. Pauses show confidence."
        Pace.VERY_FAST -> "You're speaking very fast. Slow down to ensure clarity."
    }

    // ============================================================
    // SENTIMENT & TEMPERATURE HELPERS
    // ============================================================

    /**
     * Returns true if conversation is calm and productive
     */
    fun isCalm(): Boolean = sentiment == Sentiment.CALM

    /**
     * Returns true if conversation is focused and engaged
     */
    fun isFocused(): Boolean = sentiment == Sentiment.FOCUSED

    /**
     * Returns true if conversation is stressed or tense
     */
    fun isStressed(): Boolean = sentiment in listOf(Sentiment.STRESSED, Sentiment.ANXIOUS)

    /**
     * Returns temperature zone (Green/Yellow/Red)
     */
    fun getTemperatureZone(): String = when (temperature) {
        in 0..33 -> "GREEN"   // Calm, productive
        in 34..66 -> "YELLOW" // Getting tense
        else -> "RED"         // High stress/tension
    }

    /**
     * Returns true if temperature is in danger zone (>67)
     */
    fun isHighTension(): Boolean = temperature > 67

    /**
     * Returns true if temperature is rising (>50)
     */
    fun isTensionRising(): Boolean = temperature > 50

    /**
     * Returns coaching tip for current temperature
     */
    fun getTemperatureCoachingTip(): String = when {
        temperature < 34 -> "Conversation is flowing well. Keep it up!"
        temperature in 34..66 -> "Tension is rising. Consider a pause or empathetic question."
        else -> "High tension detected. De-escalate with empathy and open questions."
    }

    // ============================================================
    // PROGRESS RING HELPERS (Apple Watch Style)
    // ============================================================

    /**
     * Returns true if empathy goal is met (>= 70%)
     */
    fun isEmpathyGoalMet(): Boolean = empathyScore >= 70

    /**
     * Returns true if listening goal is met (balanced ratio)
     */
    fun isListeningGoalMet(): Boolean = isBalancedRatio()

    /**
     * Returns true if clarity goal is met (>= 5 open questions)
     */
    fun isClarityGoalMet(): Boolean = openQuestionCount >= 5

    /**
     * Returns true if all three progress ring goals are met
     * Triggers celebration animation
     */
    fun areAllGoalsMet(): Boolean {
        return isEmpathyGoalMet() && isListeningGoalMet() && isClarityGoalMet()
    }

    /**
     * Returns number of goals met (0-3)
     */
    fun getGoalsMetCount(): Int {
        var count = 0
        if (isEmpathyGoalMet()) count++
        if (isListeningGoalMet()) count++
        if (isClarityGoalMet()) count++
        return count
    }

    /**
     * Returns color for empathy ring (Green if goal met, Yellow otherwise)
     */
    fun getEmpathyRingColor(): String {
        return if (isEmpathyGoalMet()) "GREEN" else "YELLOW"
    }

    /**
     * Returns color for listening ring
     */
    fun getListeningRingColor(): String {
        return if (isListeningGoalMet()) "GREEN" else "YELLOW"
    }

    /**
     * Returns color for clarity ring
     */
    fun getClarityRingColor(): String {
        return if (isClarityGoalMet()) "GREEN" else "YELLOW"
    }

    // ============================================================
    // QUESTION QUALITY HELPERS
    // ============================================================

    /**
     * Returns open-ended question ratio (0.0 to 1.0)
     * Higher = better quality questions
     */
    fun getOpenQuestionRatio(): Float {
        if (questionCount == 0) return 0f
        return openQuestionCount.toFloat() / questionCount.toFloat()
    }

    /**
     * Returns true if user is asking good quality questions (>50% open-ended)
     */
    fun isAskingGoodQuestions(): Boolean {
        return questionCount > 0 && getOpenQuestionRatio() >= 0.5f
    }

    /**
     * Returns percentage of open-ended questions (0-100)
     */
    fun getOpenQuestionPercentage(): Int {
        return (getOpenQuestionRatio() * 100).toInt()
    }

    // ============================================================
    // EMPATHY TRACKING HELPERS
    // ============================================================

    /**
     * Returns true if user is demonstrating good empathy (>= 5 phrases)
     */
    fun isShowingEmpathy(): Boolean = empatheticPhraseCount >= 5

    /**
     * Returns empathy level description
     */
    fun getEmpathyLevel(): String = when {
        empatheticPhraseCount == 0 -> "None detected"
        empatheticPhraseCount in 1..2 -> "Low"
        empatheticPhraseCount in 3..5 -> "Moderate"
        empatheticPhraseCount in 6..10 -> "Good"
        else -> "Excellent"
    }

    // ============================================================
    // INTERRUPTION TRACKING HELPERS
    // ============================================================

    /**
     * Returns true if interruption count is acceptable (<= 2)
     */
    fun hasAcceptableInterruptions(): Boolean = interruptionCount <= 2

    /**
     * Returns true if interruptions are problematic (> 5)
     */
    fun hasProblematicInterruptions(): Boolean = interruptionCount > 5

    /**
     * Returns interruption status for display
     */
    fun getInterruptionStatus(): String = when {
        interruptionCount == 0 -> "None"
        interruptionCount in 1..2 -> "Minimal"
        interruptionCount in 3..5 -> "Moderate"
        else -> "Frequent"
    }

    // ============================================================
    // OVERALL SESSION QUALITY
    // ============================================================

    /**
     * Calculates overall session quality score (0-100)
     * Based on balanced ratio, low interruptions, good questions, and empathy
     */
    fun getOverallQualityScore(): Int {
        var score = 0

        // Talk ratio (30 points max)
        score += when {
            isBalancedRatio() -> 30
            talkRatio in 20..60 -> 20
            else -> 10
        }

        // Question quality (25 points max)
        score += (getOpenQuestionRatio() * 25).toInt()

        // Empathy (25 points max)
        score += minOf(empathyScore / 4, 25)

        // Interruptions (20 points max)
        score += when {
            interruptionCount == 0 -> 20
            interruptionCount <= 2 -> 15
            interruptionCount <= 5 -> 10
            else -> 5
        }

        return minOf(score, 100)
    }

    /**
     * Returns overall session grade (A+ to F)
     */
    fun getSessionGrade(): String = when (getOverallQualityScore()) {
        in 97..100 -> "A+"
        in 93..96 -> "A"
        in 90..92 -> "A-"
        in 87..89 -> "B+"
        in 83..86 -> "B"
        in 80..82 -> "B-"
        in 77..79 -> "C+"
        in 73..76 -> "C"
        in 70..72 -> "C-"
        in 67..69 -> "D+"
        in 63..66 -> "D"
        in 60..62 -> "D-"
        else -> "F"
    }

    /**
     * Returns true if session quality is good (>= 80%)
     */
    fun isGoodSession(): Boolean = getOverallQualityScore() >= 80

    /**
     * Returns true if session quality needs improvement (< 60%)
     */
    fun needsImprovement(): Boolean = getOverallQualityScore() < 60

    // ============================================================
    // DISPLAY HELPERS
    // ============================================================

    /**
     * Returns formatted talk ratio for display (e.g., "35%")
     */
    fun getFormattedTalkRatio(): String = "$talkRatio%"

    /**
     * Returns formatted temperature for display (e.g., "45°")
     */
    fun getFormattedTemperature(): String = "$temperature°"

    /**
     * Returns a summary string of key metrics for logging
     */
    fun toSummaryString(): String {
        return """
            Talk Ratio: ${getFormattedTalkRatio()} (${getTalkRatioStatus()})
            Pace: ${pace.getDisplayName()}
            Sentiment: ${sentiment.getDisplayName()}
            Temperature: ${getFormattedTemperature()} (${getTemperatureZone()})
            Questions: $questionCount ($openQuestionCount open)
            Empathy Phrases: $empatheticPhraseCount
            Interruptions: $interruptionCount
            Overall Score: ${getOverallQualityScore()}% (${getSessionGrade()})
        """.trimIndent()
    }

    // ============================================================
    // METRIC UPDATE HELPERS
    // ============================================================

    /**
     * Creates a copy with updated talk ratio
     */
    fun withUpdatedTalkRatio(userWords: Int, totalWords: Int): SessionMetrics {
        val newRatio = if (totalWords > 0) {
            ((userWords.toFloat() / totalWords.toFloat()) * 100).toInt()
        } else 0

        return copy(
            talkRatio = newRatio,
            userWords = userWords,
            totalWords = totalWords,
            otherWords = totalWords - userWords,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with incremented question count
     */
    fun withNewQuestion(isOpenEnded: Boolean): SessionMetrics {
        return copy(
            questionCount = questionCount + 1,
            openQuestionCount = if (isOpenEnded) openQuestionCount + 1 else openQuestionCount,
            clarityScore = calculateClarityScore(
                questionCount + 1,
                if (isOpenEnded) openQuestionCount + 1 else openQuestionCount
            ),
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with incremented empathetic phrase count
     */
    fun withNewEmpatheticPhrase(): SessionMetrics {
        val newCount = empatheticPhraseCount + 1
        return copy(
            empatheticPhraseCount = newCount,
            empathyScore = calculateEmpathyScore(newCount),
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with incremented interruption count
     */
    fun withNewInterruption(): SessionMetrics {
        return copy(
            interruptionCount = interruptionCount + 1,
            lastUpdated = System.currentTimeMillis()
        )
    }

    /**
     * Creates a copy with updated temperature based on emotion
     */
    fun withUpdatedTemperature(emotion: Emotion?): SessionMetrics {
        val contribution = emotion?.getTemperatureValue() ?: 30
        // Smooth temperature changes: 70% old + 30% new
        val newTemp = ((temperature * 0.7) + (contribution * 0.3)).toInt()

        return copy(
            temperature = newTemp.coerceIn(0, 100),
            sentiment = calculateSentiment(newTemp),
            lastUpdated = System.currentTimeMillis()
        )
    }

    // ============================================================
    // PRIVATE CALCULATION HELPERS
    // ============================================================

    private fun calculateEmpathyScore(phraseCount: Int): Int {
        // Target: 10 empathetic phrases = 100%
        return minOf((phraseCount * 10), 100)
    }

    private fun calculateClarityScore(totalQuestions: Int, openQuestions: Int): Int {
        // Target: 5 open questions = 100%
        return minOf((openQuestions * 20), 100)
    }

    private fun calculateSentiment(temperature: Int): Sentiment {
        return when (temperature) {
            in 0..25 -> Sentiment.CALM
            in 26..50 -> Sentiment.FOCUSED
            in 51..75 -> Sentiment.STRESSED
            else -> Sentiment.ANXIOUS
        }
    }
}