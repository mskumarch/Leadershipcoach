package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * DynamicsAnalysis - Result of a political/strategic analysis
 */
data class DynamicsAnalysis(
    val alignmentScore: Int, // 0-100 (0=Hostile, 100=Aligned)
    val tensionLevel: Int,   // 0-100 (0=Calm, 100=Conflict)
    val detectedSignals: List<DynamicsSignal> = emptyList(),
    val strategicAdvice: String? = null
)

/**
 * DynamicsSignal - Specific pattern detected in conversation
 */
data class DynamicsSignal(
    val type: SignalType,
    val description: String,
    val suggestedResponse: String? = null,
    val confidence: Float, // 0.0 - 1.0
    val timestamp: Long = System.currentTimeMillis()
)

enum class SignalType {
    DEFLECTION,        // "Let's take this offline"
    VAGUE_COMMITMENT,  // "I'll try to see..."
    PASSIVE_RESISTANCE,// "Yes, but..."
    HIDDEN_AGENDA,     // Detecting underlying motivation
    STRONG_ALIGNMENT,  // "I completely agree and will do X"
    POWER_PLAY         // Interrupting, dominating
}
