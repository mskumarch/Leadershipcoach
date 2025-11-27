package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val analysisInterval: StateFlow<Long> = userPreferencesRepository.analysisIntervalFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 60_000L
        )

    val fontSizeScale: StateFlow<Float> = userPreferencesRepository.fontSizeScaleFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1.0f
        )

    val coachingStyle: StateFlow<String> = userPreferencesRepository.coachingStyleFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "EMPATHETIC"
        )

    val hapticEnabled: StateFlow<Boolean> = userPreferencesRepository.hapticEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val dailyNudgeTime: StateFlow<Long?> = userPreferencesRepository.dailyNudgeTimeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setAnalysisInterval(intervalMs: Long) {
        viewModelScope.launch {
            userPreferencesRepository.setAnalysisInterval(intervalMs)
        }
    }

    fun setFontSizeScale(scale: Float) {
        viewModelScope.launch {
            userPreferencesRepository.setFontSizeScale(scale)
        }
    }

    fun setCoachingStyle(style: String) {
        viewModelScope.launch {
            userPreferencesRepository.setCoachingStyle(style)
        }
    }

    fun setHapticEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setHapticEnabled(enabled)
        }
    }

    fun setDailyNudgeTime(timeMs: Long) {
        viewModelScope.launch {
            userPreferencesRepository.setDailyNudgeTime(timeMs)
        }
    }
}
