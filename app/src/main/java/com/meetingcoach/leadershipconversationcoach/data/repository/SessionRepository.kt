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
        messages: List<ChatMessage>,
        metrics: SessionMetrics
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val sessionEntity = SessionEntity(
                startedAt = startedAt.toEpochMilli(),
                endedAt = endedAt.toEpochMilli(),
                mode = mode.name,
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
                improvements = metrics.improvements
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
}
