package com.meetingcoach.leadershipconversationcoach.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SessionEntity::class,
        SessionMessageEntity::class,
        SessionMetricsEntity::class,
        AchievementEntity::class,
        PendingAnalysisEntity::class,
        StakeholderEntity::class
    ],
    version = 11,
    exportSchema = false
)
@androidx.room.TypeConverters(StakeholderConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun achievementDao(): AchievementDao
    abstract fun stakeholderDao(): StakeholderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "leadership_coach_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
