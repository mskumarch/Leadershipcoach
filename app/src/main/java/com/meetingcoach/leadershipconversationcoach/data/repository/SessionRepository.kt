package com.meetingcoach.leadershipconversationcoach.data.repository

import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.Session
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMetrics
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SessionEntity(
    val id: String? = null,
    val created_at: String? = null,
    val started_at: String,
    val ended_at: String,
    val mode: String,
    val duration_seconds: Int,
    val user_id: String? = null
)

@Serializable
data class SessionMessageEntity(
    val id: String? = null,
    val session_id: String,
    val created_at: String? = null,
    val message_type: String,
    val content: String,
    val speaker: String? = null,
    val metadata: String? = null
)

@Serializable
data class SessionMetricsEntity(
    val session_id: String,
    val talk_ratio_user: Int,
    val question_count: Int,
    val open_question_count: Int,
    val empathy_score: Int,
    val listening_score: Int,
    val clarity_score: Int,
    val interruption_count: Int,
    val sentiment: String,
    val temperature: Int
)

@Serializable
data class SessionWithDetails(
    val session: SessionEntity,
    val messages: List<SessionMessageEntity>,
    val metrics: SessionMetricsEntity?
)

@Singleton
class SessionRepository @Inject constructor() {

    private val supabase = createSupabaseClient(
        supabaseUrl = "YOUR_SUPABASE_URL",
        supabaseKey = "YOUR_SUPABASE_ANON_KEY"
    ) {
        install(Postgrest)
    }

    suspend fun saveSession(
        mode: SessionMode,
        startedAt: Instant,
        endedAt: Instant,
        durationSeconds: Int,
        messages: List<ChatMessage>,
        metrics: SessionMetrics
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val sessionEntity = SessionEntity(
                started_at = startedAt.toString(),
                ended_at = endedAt.toString(),
                mode = mode.name,
                duration_seconds = durationSeconds
            )

            val insertedSession = supabase.from("sessions")
                .insert(sessionEntity) {
                    select()
                }
                .decodeSingle<SessionEntity>()

            val sessionId = insertedSession.id ?: return@withContext Result.failure(
                Exception("Failed to get session ID")
            )

            val messageEntities = messages.map { message ->
                SessionMessageEntity(
                    session_id = sessionId,
                    message_type = message.type.name,
                    content = message.content,
                    speaker = message.speaker?.name,
                    metadata = message.metadata?.let { Json.encodeToString(it) }
                )
            }

            if (messageEntities.isNotEmpty()) {
                supabase.from("session_messages")
                    .insert(messageEntities)
            }

            val metricsEntity = SessionMetricsEntity(
                session_id = sessionId,
                talk_ratio_user = metrics.talkRatio,
                question_count = metrics.questionCount,
                open_question_count = metrics.openQuestionCount,
                empathy_score = metrics.empathyScore,
                listening_score = metrics.listeningScore,
                clarity_score = metrics.clarityScore,
                interruption_count = metrics.interruptionCount,
                sentiment = metrics.sentiment.name,
                temperature = metrics.temperature
            )

            supabase.from("session_metrics")
                .insert(metricsEntity)

            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllSessions(): Result<List<SessionEntity>> = withContext(Dispatchers.IO) {
        try {
            val sessions = supabase.from("sessions")
                .select()
                .decodeList<SessionEntity>()
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSessionWithDetails(sessionId: String): Result<SessionWithDetails> = withContext(Dispatchers.IO) {
        try {
            val session = supabase.from("sessions")
                .select {
                    filter {
                        eq("id", sessionId)
                    }
                }
                .decodeSingle<SessionEntity>()

            val messages = supabase.from("session_messages")
                .select {
                    filter {
                        eq("session_id", sessionId)
                    }
                }
                .decodeList<SessionMessageEntity>()

            val metrics = try {
                supabase.from("session_metrics")
                    .select {
                        filter {
                            eq("session_id", sessionId)
                        }
                    }
                    .decodeSingle<SessionMetricsEntity>()
            } catch (e: Exception) {
                null
            }

            Result.success(SessionWithDetails(session, messages, metrics))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSession(sessionId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.from("sessions")
                .delete {
                    filter {
                        eq("id", sessionId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
