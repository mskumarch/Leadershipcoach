package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.local.SessionEntity
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val sessions: List<SessionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSession: SessionWithDetails? = null,
    val averageMetrics: com.meetingcoach.leadershipconversationcoach.data.local.AverageMetricsTuple? = null,
    val searchQuery: String = "",

    val recentEmpathyScores: List<Int> = emptyList(),
    val generatedFollowUp: String? = null,
    val isGeneratingFollowUp: Boolean = false
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val geminiApiService: com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        loadSessions()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadSessions() {
        viewModelScope.launch {
            _searchQuery
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        sessionRepository.getAllSessionsFlow()
                    } else {
                        sessionRepository.searchSessions(query)
                    }
                }
                .collect { sessions ->
                    // Load metrics for trends (this is a bit inefficient, ideally we'd have a joined query)
                    // For now, we'll just fetch all metrics and map them
                    val metricsResult = sessionRepository.getAllMetrics()
                    val metrics = metricsResult.getOrNull() ?: emptyList()
                    
                    // Map metrics to sessions to get chronological order
                    val scores = sessions.mapNotNull { session ->
                        metrics.find { it.sessionId == session.id }?.empathyScore
                    }.take(10).reversed() // Take last 10, chronological
                    
                    _uiState.update { 
                        it.copy(
                            sessions = sessions.sortedByDescending { session -> session.createdAt },
                            recentEmpathyScores = scores,
                            isLoading = false
                        ) 
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun loadSessionDetails(sessionId: Long) {
        viewModelScope.launch {
            // Load details
            val detailsResult = sessionRepository.getSessionWithDetails(sessionId)
            
            // Load averages
            val averagesResult = sessionRepository.getAverageMetrics()
            
            if (detailsResult.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    selectedSession = detailsResult.getOrNull(),
                    averageMetrics = averagesResult.getOrNull()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = detailsResult.exceptionOrNull()?.message ?: "Failed to load details"
                )
            }
        }
    }

    fun clearSelectedSession() {
        _uiState.value = _uiState.value.copy(selectedSession = null)
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            sessionRepository.deleteSession(sessionId)
                .onSuccess {
                    loadSessions()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to delete session"
                    )
                }
        }
    }

    fun generateFollowUpDraft(sessionId: Long) {
        val session = _uiState.value.selectedSession ?: return
        val metrics = session.metrics ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingFollowUp = true, generatedFollowUp = null) }
            
            val summary = metrics.summary ?: "No summary available."
            val decisions = "See summary for details." // Ideally parse this from summary
            val actionItems = "See summary for details." // Ideally parse this from summary
            
            val draft = geminiApiService.generateFollowUpMessage(summary, actionItems, decisions)
            
            _uiState.update { it.copy(isGeneratingFollowUp = false, generatedFollowUp = draft) }
        }
    }
    
    fun clearFollowUpDraft() {
        _uiState.update { it.copy(generatedFollowUp = null) }
    }
}
