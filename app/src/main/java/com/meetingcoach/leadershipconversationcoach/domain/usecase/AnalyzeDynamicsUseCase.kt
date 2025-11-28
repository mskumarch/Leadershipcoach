package com.meetingcoach.leadershipconversationcoach.domain.usecase

import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingOrchestrator
import com.meetingcoach.leadershipconversationcoach.domain.models.DynamicsAnalysis
import com.meetingcoach.leadershipconversationcoach.domain.models.DynamicsSignal
import com.meetingcoach.leadershipconversationcoach.domain.models.SignalType
import javax.inject.Inject

/**
 * AnalyzeDynamicsUseCase
 *
 * The "Brain" of Dynamics Mode.
 * Analyzes transcript chunks for political subtext, alignment, and resistance.
 */
class AnalyzeDynamicsUseCase @Inject constructor(
    private val coachingOrchestrator: CoachingOrchestrator
) {

    suspend operator fun invoke(transcript: String): DynamicsAnalysis {
        // In a real implementation, this would call the LLM with a specific prompt.
        // For now, we will simulate the logic to prove the architecture works.
        
        return simulateAnalysis(transcript)
    }

    private fun simulateAnalysis(transcript: String): DynamicsAnalysis {
        val lowerText = transcript.lowercase()
        val signals = mutableListOf<DynamicsSignal>()
        var alignment = 75 // Default start
        var tension = 20

        // 1. Detect Deflection ("Offline", "Circle back")
        if (lowerText.contains("offline") || lowerText.contains("circle back") || lowerText.contains("parking lot")) {
            signals.add(DynamicsSignal(
                type = SignalType.DEFLECTION,
                description = "Deflection detected: 'Let's take this offline'",
                confidence = 0.9f
            ))
            alignment -= 15
        }

        // 2. Detect Vague Commitment ("Try", "Maybe", "See if")
        if (lowerText.contains("try to") || lowerText.contains("see if") || lowerText.contains("maybe")) {
            signals.add(DynamicsSignal(
                type = SignalType.VAGUE_COMMITMENT,
                description = "Vague commitment detected",
                confidence = 0.8f
            ))
            alignment -= 10
        }

        // 3. Detect Passive Resistance ("Yes but", "However")
        if (lowerText.contains("yes but") || lowerText.contains("understand however")) {
            signals.add(DynamicsSignal(
                type = SignalType.PASSIVE_RESISTANCE,
                description = "Passive resistance detected",
                confidence = 0.85f
            ))
            alignment -= 20
            tension += 15
        }
        
        // 4. Detect Strong Alignment ("Definitely", "I will", "Agree")
        if (lowerText.contains("definitely") || lowerText.contains("i will") || lowerText.contains("agree")) {
             signals.add(DynamicsSignal(
                type = SignalType.STRONG_ALIGNMENT,
                description = "Strong alignment detected",
                confidence = 0.9f
            ))
            alignment += 15
        }

        // Clamp values
        alignment = alignment.coerceIn(0, 100)
        tension = tension.coerceIn(0, 100)

        // Generate Strategic Advice based on signals
        val advice = when {
            signals.any { it.type == SignalType.DEFLECTION } -> "⚠️ They are stalling. Ask: 'What is the specific blocker preventing us from deciding now?'"
            signals.any { it.type == SignalType.VAGUE_COMMITMENT } -> "⚓ Anchor the commitment. Ask: 'Who will own this by when?'"
            signals.any { it.type == SignalType.PASSIVE_RESISTANCE } -> "🔍 Probe deeper. Ask: 'I sense some hesitation. What are we missing?'"
            alignment < 40 -> "🛑 Stop pitching. Start listening. You are losing them."
            else -> null
        }

        return DynamicsAnalysis(
            alignmentScore = alignment,
            tensionLevel = tension,
            detectedSignals = signals,
            strategicAdvice = advice
        )
    }
}
