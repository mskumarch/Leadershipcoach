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

import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress.Milestone

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
    val milestones: List<Milestone> = emptyList(), // New
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

                    // Calculate Real Milestones
                    val realMilestones = mutableListOf<Milestone>()
                    
                    // 1. First Session
                    val firstSession = sessions.minByOrNull { it.startedAt }
                    if (firstSession != null) {
                        val date = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(java.util.Date(firstSession.startedAt))
                        realMilestones.add(Milestone(date, "First Step", "Completed your first coaching session.", true))
                    }

                    // 2. High Score (Empathy > 90)
                    val highScoreSession = metrics.find { it.empathyScore >= 90 }
                    if (highScoreSession != null) {
                        // Find session date
                        val session = sessions.find { it.id == highScoreSession.sessionId }
                        if (session != null) {
                            val date = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(java.util.Date(session.startedAt))
                            realMilestones.add(Milestone(date, "Empathy Master", "Achieved > 90% empathy score.", true))
                        }
                    }

                    // 3. Consistency (Total Sessions)
                    if (sessions.size >= 5) {
                        val latest = sessions.maxByOrNull { it.startedAt }
                        if (latest != null) {
                             val date = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(java.util.Date(latest.startedAt))
                             realMilestones.add(Milestone(date, "Consistency King", "Completed 5+ sessions.", true))
                        }
                    }
                    
                    // 4. Joined (Static or based on first app open, using first session for now)
                    // (Optional, maybe skip if redundant with First Step)

                    // Sort by date descending (mock logic for sorting strings is hard, so we just reverse insertion order or use actual timestamps if we changed Milestone model)
                    // For now, let's just reverse them so newest is top
                    val sortedMilestones = realMilestones.reversed()

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
                        milestones = sortedMilestones,
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
