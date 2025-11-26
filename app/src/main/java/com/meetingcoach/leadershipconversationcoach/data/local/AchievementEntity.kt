package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String, // e.g., "first_session", "empathy_master"
    val title: String,
    val description: String,
    val iconResId: Int, // Resource ID for the badge icon
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val progress: Int = 0, // Current progress (e.g., 3/5 sessions)
    val target: Int = 1 // Target to unlock (e.g., 5)
)
