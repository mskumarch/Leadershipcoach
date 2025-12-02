package com.meetingcoach.leadershipconversationcoach.domain.usecase

import android.util.Log
import com.meetingcoach.leadershipconversationcoach.data.ai.CoachingEngine
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics
import com.meetingcoach.leadershipconversationcoach.utils.Result
import java.io.File
import javax.inject.Inject

/**
 * Use Case for analyzing a completed coaching session.
 * 
 * Encapsulates the logic for:
 * 1. Attempting high-fidelity audio analysis
 * 2. Falling back to text transcript analysis if audio fails
 * 3. Merging AI insights with calculated metrics
 */
class AnalyzeSessionUseCase @Inject constructor(
    private val coachingEngine: CoachingEngine
) {

    private val TAG = "AnalyzeSessionUseCase"

    /**
     * Analyze a session using available data (audio or text)
     *
     * @param audioFile The recorded audio file (optional)
     * @param messages The list of chat messages from the session
     * @param currentMetrics The basic metrics calculated during the session
     * @return Result containing the updated SessionMetrics with AI insights
     */
    suspend operator fun invoke(
        audioFile: File?,
        messages: List<ChatMessage>,
        currentMetrics: SessionMetrics
    ): Result<SessionMetrics> {
        
        Log.d(TAG, "Starting session analysis...")
        
        // 1. Try Audio Analysis first (Higher Fidelity)
        var aiMetrics: SessionMetrics? = null
        
        if (audioFile != null && audioFile.exists()) {
            try {
                Log.d(TAG, "Attempting audio analysis on file: ${audioFile.name}")
                aiMetrics = coachingEngine.analyzeAudioSession(audioFile)
                Log.d(TAG, "Audio analysis successful")
            } catch (e: Exception) {
                Log.e(TAG, "Audio analysis failed, falling back to text", e)
                // Continue to text fallback
            }
        } else {
            Log.d(TAG, "No audio file available, skipping audio analysis")
        }

        // 2. Fallback to Text Analysis if Audio failed or unavailable
        if (aiMetrics == null) {
            try {
                Log.d(TAG, "Attempting text transcript analysis")
                // Extract raw transcript from messages
                val rawTranscript = messages
                    .filter { it.type == MessageType.TRANSCRIPT }
                    .joinToString("\n") { it.content }

                if (rawTranscript.isNotBlank()) {
                    aiMetrics = coachingEngine.analyzeSession(messages)
                    Log.d(TAG, "Text analysis successful")
                } else {
                    Log.w(TAG, "Transcript is empty, cannot perform analysis")
                    return Result.Error("Session transcript is empty")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Text analysis failed", e)
                return Result.Error("Analysis failed: ${e.message}", e)
            }
        }

        // 3. Merge Results
        return if (aiMetrics != null) {
            val finalMetrics = currentMetrics.copy(
                empathyScore = aiMetrics.empathyScore,
                clarityScore = aiMetrics.clarityScore,
                listeningScore = aiMetrics.listeningScore,
                summary = aiMetrics.summary,
                paceAnalysis = aiMetrics.paceAnalysis,
                wordingAnalysis = aiMetrics.wordingAnalysis,
                improvements = aiMetrics.improvements,
                aiTranscriptJson = aiMetrics.aiTranscriptJson,
                // Deep Analyst fields
                // Deep Analyst fields
                commitments = aiMetrics.commitments,
                openQuestions = aiMetrics.openQuestions,
                closedQuestions = aiMetrics.closedQuestions,
                managerTalkPercentage = aiMetrics.managerTalkPercentage,
                interruptionCount = aiMetrics.interruptionCount,
                dynamicsAnalysisJson = aiMetrics.dynamicsAnalysisJson
            )
            Result.Success(finalMetrics)
        } else {
            Result.Error("Could not generate analysis metrics")
        }
    }
}
