package com.meetingcoach.leadershipconversationcoach.data.repository

import com.meetingcoach.leadershipconversationcoach.data.local.SessionDao
import com.meetingcoach.leadershipconversationcoach.data.local.SessionEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMessageEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMetricsEntity
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

data class SessionWithDetails(
    val session: SessionEntity,
    val messages: List<SessionMessageEntity>,
    val metrics: SessionMetricsEntity?
)

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) {

    suspend fun saveSession(
        mode: SessionMode,
        startedAt: Instant,
        endedAt: Instant,
        durationSeconds: Int,
        title: String? = null,
        messages: List<ChatMessage>,
        metrics: SessionMetrics
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val sessionEntity = SessionEntity(
                startedAt = startedAt.toEpochMilli(),
                endedAt = endedAt.toEpochMilli(),
                mode = mode.name,
                title = title,
                durationSeconds = durationSeconds
            )

            val sessionId = sessionDao.insertSession(sessionEntity)

            val messageEntities = messages.map { message ->
                SessionMessageEntity(
                    sessionId = sessionId,
                    messageType = message.type.name,
                    content = message.content,
                    speaker = message.speaker?.name
                )
            }

            if (messageEntities.isNotEmpty()) {
                sessionDao.insertMessages(messageEntities)
            }

            val metricsEntity = SessionMetricsEntity(
                sessionId = sessionId,
                talkRatioUser = metrics.talkRatio,
                questionCount = metrics.questionCount,
                openQuestionCount = metrics.openQuestionCount,
                empathyScore = metrics.empathyScore,
                listeningScore = metrics.listeningScore,
                clarityScore = metrics.clarityScore,
                interruptionCount = metrics.interruptionCount,
                sentiment = metrics.sentiment.name,
                temperature = metrics.temperature,
                summary = metrics.summary,
                paceAnalysis = metrics.paceAnalysis,
                wordingAnalysis = metrics.wordingAnalysis,
                improvements = metrics.improvements,
                aiTranscriptJson = metrics.aiTranscriptJson,
                dynamicsAnalysisJson = metrics.dynamicsAnalysisJson,
                actionItemsJson = org.json.JSONArray(metrics.actionItems).toString()
            )

            sessionDao.insertMetrics(metricsEntity)

            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllSessionsFlow(): Flow<List<SessionEntity>> {
        return sessionDao.getAllSessions()
    }

    fun searchSessions(query: String): Flow<List<SessionEntity>> {
        return sessionDao.searchSessions(query)
    }

    suspend fun getAllSessions(): Result<List<SessionEntity>> = withContext(Dispatchers.IO) {
        try {
            val sessions = sessionDao.getAllSessionsList()
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSessionWithDetails(sessionId: Long): Result<SessionWithDetails> = withContext(Dispatchers.IO) {
        try {
            val session = sessionDao.getSessionById(sessionId)
                ?: return@withContext Result.failure(Exception("Session not found"))

            val messages = sessionDao.getSessionMessages(sessionId)
            val metrics = sessionDao.getSessionMetrics(sessionId)

            Result.success(SessionWithDetails(session, messages, metrics))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSession(sessionId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            sessionDao.deleteSessionWithDetails(sessionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getAllMetrics(): Result<List<SessionMetricsEntity>> = withContext(Dispatchers.IO) {
        try {
            val metrics = sessionDao.getAllMetrics()
            Result.success(metrics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAverageMetrics(): Result<com.meetingcoach.leadershipconversationcoach.data.local.AverageMetricsTuple?> = withContext(Dispatchers.IO) {
        try {
            val averages = sessionDao.getAverageMetrics()
            Result.success(averages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateSessionTitle(sessionId: Long, title: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            sessionDao.updateSessionTitle(sessionId, title)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSessionTags(sessionId: Long, tags: List<String>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val tagsJson = org.json.JSONArray(tags).toString()
            sessionDao.updateSessionTags(sessionId, tagsJson)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun queuePendingAnalysis(sessionId: Long, audioFilePath: String, mode: String) = withContext(Dispatchers.IO) {
        sessionDao.insertPendingAnalysis(
            com.meetingcoach.leadershipconversationcoach.data.local.PendingAnalysisEntity(
                sessionId = sessionId,
                audioFilePath = audioFilePath,
                sessionMode = mode
            )
        )
    }

    suspend fun getPendingAnalysis() = withContext(Dispatchers.IO) {
        sessionDao.getAllPendingAnalysis()
    }

    suspend fun removePendingAnalysis(pending: com.meetingcoach.leadershipconversationcoach.data.local.PendingAnalysisEntity) = withContext(Dispatchers.IO) {
        sessionDao.deletePendingAnalysis(pending)
    }

    suspend fun getRecentTags(): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val sessions = sessionDao.getSessionsWithTags()
            val allTags = mutableSetOf<String>()
            
            sessions.forEach { session ->
                if (!session.tags.isNullOrBlank()) {
                    try {
                        val jsonArray = org.json.JSONArray(session.tags)
                        for (i in 0 until jsonArray.length()) {
                            allTags.add(jsonArray.getString(i))
                        }
                    } catch (e: Exception) {
                        // Ignore parsing errors
                    }
                }
            }
            Result.success(allTags.toList().sorted())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLastSessionContextByTag(tag: String): Result<SessionWithDetails?> = withContext(Dispatchers.IO) {
        try {
            val sessions = sessionDao.getSessionsWithTags()
            // Find first session that contains the tag
            val matchingSession = sessions.firstOrNull { session ->
                try {
                    val tags = session.tags
                    if (tags.isNullOrBlank()) false
                    else {
                        val jsonArray = org.json.JSONArray(tags)
                        var found = false
                        for (i in 0 until jsonArray.length()) {
                            if (jsonArray.getString(i).equals(tag, ignoreCase = true)) {
                                found = true
                                break
                            }
                        }
                        found
                    }
                } catch (e: Exception) {
                    false
                }
            }

            if (matchingSession != null) {
                getSessionWithDetails(matchingSession.id)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
