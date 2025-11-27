package com.meetingcoach.leadershipconversationcoach.data.ai.strategies

import com.google.ai.client.generativeai.GenerativeModel
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.*

/**
 * 1:1 Coaching Strategy
 * Focuses on GROW Model, rapport building, and active listening.
 */
class OneOnOneStrategy(
    geminiModel: GenerativeModel
) : CoachingStrategy {

    override val sessionMode: String = "ONE_ON_ONE"

    override val navigator: CoachingAgent<NavigatorResult> = NavigatorAgent(geminiModel)
    override val whisperer: CoachingAgent<WhispererResult> = WhispererAgent(geminiModel)
    override val guardian: CoachingAgent<GuardianResult> = GuardianAgent(geminiModel)

    // Use research-based intervals from CoachingConstants
    override val navigatorIntervalMs: Long = com.meetingcoach.leadershipconversationcoach.domain.CoachingConstants.AgentIntervals.NAVIGATOR_INTERVAL_MS

    override val guardianIntervalMs: Long = com.meetingcoach.leadershipconversationcoach.domain.CoachingConstants.AgentIntervals.GUARDIAN_INTERVAL_MS
}
