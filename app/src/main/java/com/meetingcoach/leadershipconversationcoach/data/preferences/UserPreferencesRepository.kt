package com.meetingcoach.leadershipconversationcoach.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
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
    }

    val analysisIntervalFlow: Flow<Long> = context.dataStore.data
        .map { preferences ->
            // Default to 60 seconds (60000ms)
            preferences[PreferencesKeys.ANALYSIS_INTERVAL] ?: 60_000L
        }

    suspend fun setAnalysisInterval(intervalMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANALYSIS_INTERVAL] = intervalMs
        }
    }
}
