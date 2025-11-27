package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Insert
    suspend fun insertMessages(messages: List<SessionMessageEntity>)

    @Insert
    suspend fun insertMetrics(metrics: SessionMetricsEntity)

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE title LIKE '%' || :query || '%' OR mode LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchSessions(query: String): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    suspend fun getAllSessionsList(): List<SessionEntity>

    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): SessionEntity?

    @Query("SELECT * FROM session_messages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    suspend fun getSessionMessages(sessionId: Long): List<SessionMessageEntity>

    @Query("SELECT * FROM session_metrics WHERE sessionId = :sessionId")
    suspend fun getSessionMetrics(sessionId: Long): SessionMetricsEntity?

    @Query("SELECT * FROM session_metrics")
    suspend fun getAllMetrics(): List<SessionMetricsEntity>

    @Query("SELECT AVG(empathyScore) as avgEmpathy, AVG(clarityScore) as avgClarity, AVG(listeningScore) as avgListening FROM session_metrics")
    suspend fun getAverageMetrics(): AverageMetricsTuple?

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("DELETE FROM session_messages WHERE sessionId = :sessionId")
    suspend fun deleteSessionMessages(sessionId: Long)

    @Query("DELETE FROM session_metrics WHERE sessionId = :sessionId")
    suspend fun deleteSessionMetrics(sessionId: Long)

    @Transaction
    suspend fun deleteSessionWithDetails(sessionId: Long) {
        val session = getSessionById(sessionId)
        if (session != null) {
            deleteSessionMessages(sessionId)
            deleteSessionMetrics(sessionId)
            deleteSession(session)
        }
    }

    @Query("UPDATE sessions SET title = :title WHERE id = :sessionId")
    suspend fun updateSessionTitle(sessionId: Long, title: String)
}

data class AverageMetricsTuple(
    val avgEmpathy: Double?,
    val avgClarity: Double?,
    val avgListening: Double?
)
