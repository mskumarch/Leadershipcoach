package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionEntity
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionRepository
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val sessions: List<SessionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedSession: SessionWithDetails? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            sessionRepository.getAllSessions()
                .onSuccess { sessions ->
                    _uiState.value = _uiState.value.copy(
                        sessions = sessions.sortedByDescending { it.created_at },
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load sessions"
                    )
                }
        }
    }

    fun loadSessionDetails(sessionId: String) {
        viewModelScope.launch {
            sessionRepository.getSessionWithDetails(sessionId)
                .onSuccess { details ->
                    _uiState.value = _uiState.value.copy(selectedSession = details)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load session details"
                    )
                }
        }
    }

    fun clearSelectedSession() {
        _uiState.value = _uiState.value.copy(selectedSession = null)
    }

    fun deleteSession(sessionId: String) {
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
}
