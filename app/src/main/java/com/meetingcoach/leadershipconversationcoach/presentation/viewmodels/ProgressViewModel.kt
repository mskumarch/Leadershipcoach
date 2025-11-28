package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProgressUiState(
    val overallScore: Int = 0,
    val empathyScore: Int = 0,
    val clarityScore: Int = 0,
    val listeningScore: Int = 0,
    val weeklyActivity: List<Int> = List(7) { 0 }, // Mon-Sun
    val averageTalkRatio: Int = 0,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    fun loadProgressData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val metricsResult = sessionRepository.getAllMetrics()
            val sessionsResult = sessionRepository.getAllSessions()

            if (metricsResult.isSuccess && sessionsResult.isSuccess) {
                val metrics = metricsResult.getOrDefault(emptyList())
                val sessions = sessionsResult.getOrDefault(emptyList())

                if (metrics.isNotEmpty()) {
                    val avgEmpathy = metrics.map { it.empathyScore }.average().toInt()
                    val avgClarity = metrics.map { it.clarityScore }.average().toInt()
                    val avgListening = metrics.map { it.listeningScore }.average().toInt()

                    val overall = (avgEmpathy + avgClarity + avgListening) / 3
                    val avgTalkRatio = metrics.map { it.talkRatioUser }.average().toInt()

                    // Calculate Weekly Activity
                    // This is a simplified implementation. In a real app, use Calendar/LocalDate.
                    val activity = MutableList(7) { 0 }
                    val now = System.currentTimeMillis()
                    val oneDay = 24 * 60 * 60 * 1000L
                    
                    sessions.forEach { session ->
                        val age = now - session.startedAt
                        val daysAgo = (age / oneDay).toInt()
                        if (daysAgo < 7) {
                            // Map daysAgo to index (0 = Today, 6 = 6 days ago)
                            // But chart expects Mon-Sun or similar. 
                            // Let's just map to "Days Ago" for now, reversed.
                            // Actually, let's just count sessions per day for the last 7 days.
                            activity[6 - daysAgo]++ 
                        }
                    }

                    _uiState.value = ProgressUiState(
                        overallScore = overall,
                        empathyScore = avgEmpathy,
                        clarityScore = avgClarity,
                        listeningScore = avgListening,
                        weeklyActivity = activity,
                        averageTalkRatio = avgTalkRatio,
                        isLoading = false
                    )
                } else {
                    _uiState.value = ProgressUiState(isLoading = false)
                }
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
