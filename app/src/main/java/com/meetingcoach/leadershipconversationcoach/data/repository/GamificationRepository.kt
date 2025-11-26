package com.meetingcoach.leadershipconversationcoach.data.repository

import com.meetingcoach.leadershipconversationcoach.data.local.AchievementDao
import com.meetingcoach.leadershipconversationcoach.data.local.AchievementEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMetricsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamificationRepository @Inject constructor(
    private val achievementDao: AchievementDao
) {
    val allAchievements: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()

    suspend fun initializeDefaults() {
        val defaults = listOf(
            AchievementEntity(
                id = "first_session",
                title = "First Step",
                description = "Complete your first coaching session.",
                iconResId = android.R.drawable.star_on, // Placeholder
                target = 1
            ),
            AchievementEntity(
                id = "empathy_master",
                title = "Empathy Master",
                description = "Achieve >80% Empathy Score in a session.",
                iconResId = android.R.drawable.star_on,
                target = 1
            ),
            AchievementEntity(
                id = "good_listener",
                title = "Good Listener",
                description = "Listen more than you speak (Talk Ratio < 40%).",
                iconResId = android.R.drawable.star_on,
                target = 1
            ),
            AchievementEntity(
                id = "consistent_leader",
                title = "Consistent Leader",
                description = "Complete 5 sessions.",
                iconResId = android.R.drawable.star_on,
                target = 5
            )
        )
        achievementDao.insertAll(defaults)
    }

    suspend fun checkAchievements(metrics: SessionMetricsEntity) {
        // 1. First Session
        unlockOrProgress("first_session", 1)

        // 2. Empathy Master
        if (metrics.empathyScore > 80) {
            unlockOrProgress("empathy_master", 1)
        }

        // 3. Good Listener
        if (metrics.talkRatioUser < 40) {
            unlockOrProgress("good_listener", 1)
        }

        // 4. Consistent Leader
        unlockOrProgress("consistent_leader", 1)
    }

    private suspend fun unlockOrProgress(id: String, amount: Int) {
        val achievement = achievementDao.getAchievement(id) ?: return
        
        if (!achievement.isUnlocked) {
            val newProgress = achievement.progress + amount
            val isUnlocked = newProgress >= achievement.target
            
            val updated = achievement.copy(
                progress = newProgress,
                isUnlocked = isUnlocked,
                unlockedAt = if (isUnlocked) System.currentTimeMillis() else null
            )
            achievementDao.update(updated)
        }
    }
}
