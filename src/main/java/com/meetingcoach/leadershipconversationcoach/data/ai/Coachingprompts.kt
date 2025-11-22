package com.meetingcoach.leadershipconversationcoach.data.ai

import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode

/**
 * CoachingPrompts - Prompt Template Library
 *
 * Centralized collection of AI prompts for different coaching scenarios.
 * Makes prompts easier to:
 * - Maintain and update
 * - Customize for different contexts
 * - Test and improve
 * - Localize (future)
 *
 * Organized by:
 * - Session modes (1-on-1, team, difficult)
 * - Coaching types (analysis, questions, empathy, tension)
 * - User interactions (Q&A, suggestions)
 *
 * Architecture: Data Layer - Prompt Templates
 *
 * Created for: Maintainable AI Prompts
 * Last Updated: November 2025
 */
object CoachingPrompts {

    // ============================================================
    // CONVERSATION ANALYSIS PROMPTS
    // ============================================================

    /**
     * Analyze conversation for coaching opportunities
     */
    fun conversationAnalysis(
        transcript: String,
        sessionMode: SessionMode,
        talkRatio: Int,
        questionCount: Int,
        sessionDuration: Int
    ): String {
        val modeContext = getSessionModeContext(sessionMode)

        return """
            You are an expert leadership coach providing real-time guidance.
            
            SESSION CONTEXT:
            - Type: ${sessionMode.getDisplayName()}
            - Focus: $modeContext
            - Duration: $sessionDuration minutes
            
            CURRENT METRICS:
            - Talk Ratio: User ${talkRatio}%, Others ${100 - talkRatio}%
            - Questions Asked: $questionCount
            - Ideal Ratio: User 30-40%, Others 60-70%
            
            RECENT CONVERSATION:
            "$transcript"
            
            COACHING GUIDELINES:
            1. Provide ONE specific, actionable nudge if needed
            2. Focus on the most important issue right now
            3. Be concise (under 50 words)
            4. Be encouraging and constructive
            5. If no coaching needed, respond "NONE"
            
            PRIORITY AREAS:
            - Talk/listen balance (critical if >60% or <20%)
            - Question quality (open vs closed)
            - Empathy and active listening
            - Interruptions or tension
            - Long silences (if someone hasn't spoken)
            
            Respond in this format:
            TYPE: [URGENT/IMPORTANT/HELPFUL/INFO]
            MESSAGE: [Your coaching suggestion]
            
            Examples:
            
            TYPE: URGENT
            MESSAGE: You've been speaking for 8 minutes straight. Pause and ask: "What questions do you have?"
            
            TYPE: IMPORTANT
            MESSAGE: Try asking an open question: "What concerns you most about this approach?"
            
            TYPE: HELPFUL
            MESSAGE: Great listening! You're letting them speak. Keep it up.
            
            Now analyze the conversation above:
        """.trimIndent()
    }

    /**
     * Session mode specific context
     */
    private fun getSessionModeContext(mode: SessionMode): String {
        return when (mode) {
            SessionMode.ONE_ON_ONE ->
                "Build trust, show empathy, ask open questions, listen actively"
            SessionMode.TEAM_MEETING ->
                "Ensure everyone participates, manage group dynamics, seek diverse perspectives"
            SessionMode.DIFFICULT_CONVERSATION ->
                "Stay calm, de-escalate tension, show empathy, find common ground"
        }
    }

    // ============================================================
    // QUESTION SUGGESTION PROMPTS
    // ============================================================

    /**
     * Suggest next question to ask
     */
    fun suggestQuestion(
        conversationContext: String,
        sessionMode: SessionMode
    ): String {
        return """
            You are an expert leadership coach. Based on this conversation, suggest ONE powerful question.
            
            Session Type: ${sessionMode.getDisplayName()}
            
            Conversation Context:
            "$conversationContext"
            
            QUESTION CRITERIA:
            - Open-ended (not yes/no)
            - Shows empathy and curiosity
            - Moves conversation forward
            - Relevant to current context
            - Under 20 words
            - Natural and conversational
            
            GOOD EXAMPLES:
            - "What concerns you most about this?"
            - "How do you see this affecting the team?"
            - "What would success look like for you?"
            - "Tell me more about what's driving that perspective."
            
            AVOID:
            - Yes/no questions
            - Leading questions
            - Multiple questions at once
            - Overly formal language
            
            Respond with ONLY the question, nothing else.
        """.trimIndent()
    }

    // ============================================================
    // USER Q&A PROMPTS
    // ============================================================

    /**
     * Answer user's coaching question
     */
    fun answerCoachingQuestion(
        question: String,
        conversationContext: String?
    ): String {
        val contextSection = if (conversationContext != null) {
            """
            CURRENT CONVERSATION CONTEXT:
            "$conversationContext"
            
            """.trimIndent()
        } else {
            ""
        }

        return """
            You are a supportive, expert leadership coach. Answer this question with practical advice.
            
            ${contextSection}USER'S QUESTION:
            "$question"
            
            RESPONSE GUIDELINES:
            - Be warm, encouraging, and supportive
            - Provide specific, actionable advice
            - Keep it concise (under 100 words)
            - Use examples when helpful
            - Reference the conversation context if relevant
            - Be conversational and human
            
            TONE:
            - Empathetic but not patronizing
            - Confident but not arrogant  
            - Practical and actionable
            - Encouraging and growth-oriented
            
            Now respond to the user's question:
        """.trimIndent()
    }

    // ============================================================
    // TENSION DETECTION PROMPTS
    // ============================================================

    /**
     * Detect conversation tension/conflict
     */
    fun detectTension(transcript: String): String {
        return """
            Analyze this conversation for signs of tension, conflict, or stress.
            
            CONVERSATION:
            "$transcript"
            
            TENSION INDICATORS:
            - Defensive language ("but", "actually", "you don't understand")
            - Raised emotions (frustration, anger, anxiety)
            - Interruptions or talking over each other
            - Sarcasm or passive-aggressive comments
            - Repeated disagreements
            - Avoidance or silence after contentious points
            
            TENSION LEVELS:
            0-25: Calm and productive
            26-50: Some tension but manageable
            51-75: Rising tension, intervention helpful
            76-100: High conflict, urgent de-escalation needed
            
            Respond in this EXACT format:
            TENSION_LEVEL: [0-100]
            INDICATORS: [Specific phrases or patterns causing tension]
            SUGGESTION: [One specific de-escalation technique]
            
            Example:
            TENSION_LEVEL: 65
            INDICATORS: Multiple "buts", defensive tone, interruptions detected
            SUGGESTION: Take a breath. Say: "I want to understand your perspective. Help me see what you're seeing."
        """.trimIndent()
    }

    // ============================================================
    // EMPATHY COACHING PROMPTS
    // ============================================================

    /**
     * Coach on showing empathy
     */
    fun empathyCoaching(context: String): String {
        return """
            The user needs help showing empathy in this conversation.
            
            Context:
            "$context"
            
            Provide ONE specific empathetic phrase they could say right now.
            
            EMPATHETIC RESPONSES:
            - "I can see this is really important to you"
            - "That sounds challenging. Tell me more about what you're experiencing"
            - "I appreciate you sharing that with me"
            - "How is this affecting you?"
            - "What would be most helpful for you right now?"
            
            Respond with ONE phrase (under 15 words) that fits this specific context.
        """.trimIndent()
    }

    // ============================================================
    // SESSION SUMMARY PROMPTS
    // ============================================================

    /**
     * Generate end-of-session summary
     */
    fun sessionSummary(
        fullTranscript: String,
        talkRatio: Int,
        questionCount: Int,
        openQuestionCount: Int,
        empatheticPhraseCount: Int,
        interruptionCount: Int,
        duration: Int
    ): String {
        return """
            Create a comprehensive coaching summary for this conversation session.
            
            SESSION METRICS:
            - Duration: $duration minutes
            - Talk Ratio: User ${talkRatio}%, Others ${100 - talkRatio}%
            - Questions Asked: $questionCount ($openQuestionCount open-ended)
            - Empathetic Phrases: $empatheticPhraseCount
            - Interruptions: $interruptionCount
            
            FULL TRANSCRIPT:
            "$fullTranscript"
            
            Create a summary with these sections:
            
            ## 🎯 KEY DISCUSSION POINTS
            [3-5 main topics discussed]
            
            ## ✅ YOUR COACHING STRENGTHS
            [2-3 specific examples of what you did well]
            
            ## 📈 AREAS FOR IMPROVEMENT
            [2-3 specific, actionable suggestions]
            
            ## 📋 ACTION ITEMS
            [Any action items mentioned in the conversation]
            
            ## 🔮 NEXT CONVERSATION FOCUS
            [1-2 areas to focus on in your next conversation]
            
            TONE:
            - Encouraging and growth-oriented
            - Specific with examples from conversation
            - Balanced (acknowledge strengths AND improvements)
            - Actionable and practical
            
            Keep the summary concise but meaningful.
        """.trimIndent()
    }

    // ============================================================
    // PATTERN-SPECIFIC PROMPTS
    // ============================================================

    /**
     * Coaching for talking too much
     */
    fun talkingTooMuch(talkRatio: Int): String {
        return """
            TYPE: URGENT
            MESSAGE: You've been speaking ${talkRatio}% of the time. Great leaders listen more than they speak. Try: "I've been talking a lot. What are YOUR thoughts?"
        """.trimIndent()
    }

    /**
     * Coaching for not talking enough
     */
    fun notTalkingEnough(talkRatio: Int): String {
        return """
            TYPE: HELPFUL
            MESSAGE: You've only spoken ${talkRatio}% of the time. It's okay to share your perspective too. Try: "Here's what I'm thinking..."
        """.trimIndent()
    }

    /**
     * Coaching for no questions asked
     */
    fun noQuestionsAsked(duration: Int): String {
        return """
            TYPE: IMPORTANT
            MESSAGE: You haven't asked any questions in $duration minutes. Curiosity shows care. Try: "What's your perspective on this?"
        """.trimIndent()
    }

    /**
     * Coaching for too many interruptions
     */
    fun tooManyInterruptions(count: Int): String {
        return """
            TYPE: URGENT
            MESSAGE: You've interrupted $count times. Let others finish their thoughts. Pause for 2 seconds after they speak before responding.
        """.trimIndent()
    }

    /**
     * Coaching for asking only closed questions
     */
    fun onlyClosedQuestions(): String {
        return """
            TYPE: IMPORTANT
            MESSAGE: Your questions can be answered with yes/no. Try open-ended questions: "How do you see this?" or "What concerns you most?"
        """.trimIndent()
    }

    /**
     * Coaching for high tension detected
     */
    fun highTensionDetected(): String {
        return """
            TYPE: URGENT
            MESSAGE: Tension is rising. Take a breath. Try: "I want to understand your perspective. Can you help me see what you're seeing?"
        """.trimIndent()
    }

    /**
     * Coaching for someone being silent
     */
    fun silentParticipant(name: String, duration: Int): String {
        return """
            TYPE: HELPFUL
            MESSAGE: $name hasn't spoken in $duration minutes. Try: "$name, what's your take on this?"
        """.trimIndent()
    }

    // ============================================================
    // POSITIVE REINFORCEMENT PROMPTS
    // ============================================================

    /**
     * Celebrate good coaching moment
     */
    fun celebrateGoodQuestion(): String {
        return """
            TYPE: INFO
            MESSAGE: 🎉 Excellent question! That was open-ended and showed genuine curiosity. Keep it up!
        """.trimIndent()
    }

    /**
     * Celebrate good listening
     */
    fun celebrateGoodListening(): String {
        return """
            TYPE: INFO
            MESSAGE: ✅ Great listening! You're giving space for others to think and speak. This builds trust.
        """.trimIndent()
    }

    /**
     * Celebrate showing empathy
     */
    fun celebrateEmpathy(): String {
        return """
            TYPE: INFO
            MESSAGE: 💚 That was empathetic! You acknowledged their feelings before problem-solving. Well done!
        """.trimIndent()
    }

    /**
     * Celebrate good pace/pause
     */
    fun celebrateGoodPause(): String {
        return """
            TYPE: INFO
            MESSAGE: 👏 Perfect pause! Giving space shows confidence and respect. They can think and respond fully.
        """.trimIndent()
    }

    // ============================================================
    // CONTEXT BUILDERS
    // ============================================================

    /**
     * Build rich context summary from recent conversation
     */
    fun buildContextSummary(
        transcripts: List<String>,
        metrics: Map<String, Any>
    ): String {
        val transcriptText = transcripts.joinToString("\n")
        val metricsText = metrics.entries.joinToString(", ") { "${it.key}: ${it.value}" }

        return """
            Recent conversation:
            $transcriptText
            
            Metrics: $metricsText
        """.trimIndent()
    }
}