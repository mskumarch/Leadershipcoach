package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_analysis")
data class PendingAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val audioFilePath: String,
    val sessionMode: String,
    val createdAt: Long = System.currentTimeMillis()
)
