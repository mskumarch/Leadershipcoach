package com.meetingcoach.leadershipconversationcoach.data.ai.strategies

import com.meetingcoach.leadershipconversationcoach.data.ai.agents.CoachingAgent
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.GuardianResult
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.NavigatorResult
import com.meetingcoach.leadershipconversationcoach.data.ai.agents.WhispererResult

/**
 * Strategy interface for different session types.
 * Each strategy defines which agents to use and how they are configured.
 */
interface CoachingStrategy {
    val sessionMode: String

    // The Real-Time Trio
    val navigator: CoachingAgent<NavigatorResult>
    val whisperer: CoachingAgent<WhispererResult>
    val guardian: CoachingAgent<GuardianResult>
    
    // Configuration
    val navigatorIntervalMs: Long
    val guardianIntervalMs: Long
}
