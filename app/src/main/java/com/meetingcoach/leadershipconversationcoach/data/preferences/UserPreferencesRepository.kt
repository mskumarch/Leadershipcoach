package com.meetingcoach.leadershipconversationcoach.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val ANALYSIS_INTERVAL = longPreferencesKey("analysis_interval")
        val FONT_SIZE_SCALE = floatPreferencesKey("font_size_scale")
        val COACHING_STYLE = stringPreferencesKey("coaching_style")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val DAILY_NUDGE_TIME = longPreferencesKey("daily_nudge_time")
    }

    val analysisIntervalFlow: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ANALYSIS_INTERVAL] ?: 60_000L
        }

    val fontSizeScaleFlow: Flow<Float> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FONT_SIZE_SCALE] ?: 1.0f
        }

    val coachingStyleFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.COACHING_STYLE] ?: "EMPATHETIC" // Default
        }

    val hapticEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.HAPTIC_ENABLED] ?: true
        }

    val dailyNudgeTimeFlow: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DAILY_NUDGE_TIME]
        }

    suspend fun setAnalysisInterval(intervalMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANALYSIS_INTERVAL] = intervalMs
        }
    }

    suspend fun setFontSizeScale(scale: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE_SCALE] = scale
        }
    }

    suspend fun setCoachingStyle(style: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COACHING_STYLE] = style
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun setDailyNudgeTime(timeMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_NUDGE_TIME] = timeMs
        }
    }
}
