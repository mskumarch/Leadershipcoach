package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startedAt: Long,
    val endedAt: Long,
    val mode: String,
    val title: String? = null,
    val durationSeconds: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_messages")
data class SessionMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val messageType: String,
    val content: String,
    val speaker: String? = null,
    val metadata: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_metrics")
data class SessionMetricsEntity(
    @PrimaryKey
    val sessionId: Long,
    val talkRatioUser: Int,
    val questionCount: Int,
    val openQuestionCount: Int,
    val empathyScore: Int,
    val listeningScore: Int,
    val clarityScore: Int,
    val interruptionCount: Int,
    val sentiment: String,
    val temperature: Int,
    val summary: String? = null,
    val paceAnalysis: String? = null,
    val wordingAnalysis: String? = null,
    val improvements: String? = null
)
