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

import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress.StakeholderStatus
import org.json.JSONObject

data class ProgressUiState(
    val overallScore: Int = 0,
    val empathyScore: Int = 0,
    val clarityScore: Int = 0,
    val listeningScore: Int = 0,
    val influenceScore: Int = 0, // New
    val paceScore: Int = 0,      // New
    val weeklyActivity: List<Int> = List(7) { 0 },
    val averageTalkRatio: Int = 0,
    val stakeholders: List<StakeholderStatus> = emptyList(),
    val trendData: List<Float> = emptyList(), // New
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
                    
                    // Calculate Influence & Pace (Mock logic for now as these aren't direct columns yet)
                    val avgInfluence = (avgClarity + avgListening) / 2 + 5 
                    val avgPace = 75 

                    val overall = (avgEmpathy + avgClarity + avgListening) / 3
                    val avgTalkRatio = metrics.map { it.talkRatioUser }.average().toInt()

                    // Calculate Trend Data (Last 10 sessions overall score)
                    // We need to join metrics with sessions to get date order, but metrics are usually inserted in order
                    // Simplification: Take last 10 metrics
                    val trendData = metrics.takeLast(10).map { 
                        ((it.empathyScore + it.clarityScore + it.listeningScore) / 3).toFloat() / 100f 
                    }

                    // Calculate Stakeholders from Dynamics JSON
                    val stakeholderMap = mutableMapOf<String, MutableList<Int>>() 
                    val stakeholderRoles = mutableMapOf<String, String>() 

                    metrics.forEach { metric ->
                        if (!metric.dynamicsAnalysisJson.isNullOrBlank()) {
                            try {
                                val json = JSONObject(metric.dynamicsAnalysisJson)
                                val map = json.optJSONArray("stakeholder_map")
                                if (map != null) {
                                    for (i in 0 until map.length()) {
                                        val s = map.getJSONObject(i)
                                        val name = s.optString("speaker")
                                        val role = s.optString("role")
                                        if (name.isNotBlank()) {
                                            stakeholderMap.getOrPut(name) { mutableListOf() }.add(metric.empathyScore)
                                            stakeholderRoles[name] = role 
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                // Ignore parsing errors
                            }
                        }
                    }

                    val stakeholders = stakeholderMap.map { (name, scores) ->
                        StakeholderStatus(
                            name = name,
                            score = scores.average().toInt(),
                            role = stakeholderRoles[name] ?: "Neutral"
                        )
                    }.sortedByDescending { it.score }.take(5) 

                    // Calculate Weekly Activity
                    val activity = MutableList(7) { 0 }
                    val now = System.currentTimeMillis()
                    val oneDay = 24 * 60 * 60 * 1000L
                    
                    sessions.forEach { session ->
                        val age = now - session.startedAt
                        val daysAgo = (age / oneDay).toInt()
                        if (daysAgo < 7) {
                            activity[6 - daysAgo]++ 
                        }
                    }

                    _uiState.value = ProgressUiState(
                        overallScore = overall,
                        empathyScore = avgEmpathy,
                        clarityScore = avgClarity,
                        listeningScore = avgListening,
                        influenceScore = avgInfluence,
                        paceScore = avgPace,
                        weeklyActivity = activity,
                        averageTalkRatio = avgTalkRatio,
                        stakeholders = stakeholders,
                        trendData = trendData,
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
