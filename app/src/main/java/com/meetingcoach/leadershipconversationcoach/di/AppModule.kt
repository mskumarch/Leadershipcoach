package com.meetingcoach.leadershipconversationcoach.di

import android.content.Context
import com.meetingcoach.leadershipconversationcoach.data.local.AppDatabase
import com.meetingcoach.leadershipconversationcoach.data.local.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    @Singleton
    fun provideAchievementDao(database: AppDatabase): com.meetingcoach.leadershipconversationcoach.data.local.AchievementDao {
        return database.achievementDao()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@ApplicationContext context: Context): com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService {
        return com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService(
            context = context,
            apiKey = com.meetingcoach.leadershipconversationcoach.BuildConfig.GEMINI_API_KEY
        )
    }
}