package com.meetingcoach.leadershipconversationcoach.domain.models

import java.util.UUID

/**
 * Stakeholder - Represents a person the user interacts with.
 * Stores behavioral patterns and history.
 */
data class Stakeholder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val role: String,
    val relationship: String, // e.g., "Peer", "Manager", "Direct Report"
    val tendencies: List<BehavioralTendency> = emptyList(),
    val lastInteraction: Long? = null
)

data class BehavioralTendency(
    val type: String, // e.g., "Deflection", "Vague Commitment"
    val frequency: String, // "High", "Medium", "Low"
    val strategy: String // "Push for specifics", "Build rapport first"
)
