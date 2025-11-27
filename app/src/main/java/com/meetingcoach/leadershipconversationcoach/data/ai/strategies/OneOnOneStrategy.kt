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

    // Run Navigator every 60 seconds to track GROW progression
    override val navigatorIntervalMs: Long = 60_000L

    // Run Guardian every 30 seconds to monitor behavior
    override val guardianIntervalMs: Long = 30_000L
}
