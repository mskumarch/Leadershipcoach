package com.meetingcoach.leadershipconversationcoach.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for app-level dependencies
 * Currently empty - will add Repository, UseCases later
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // TODO: Provide Repository, Database, etc.
}