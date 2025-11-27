package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.repository.RssRepository
import com.meetingcoach.leadershipconversationcoach.domain.models.RssItem
import com.meetingcoach.leadershipconversationcoach.data.ai.GeminiApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WisdomUiState(
    val articles: List<RssItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedArticle: RssItem? = null,
    val articleSummary: String? = null,
    val isSummarizing: Boolean = false
)

@HiltViewModel
class WisdomViewModel @Inject constructor(
    private val rssRepository: RssRepository,
    private val geminiApiService: GeminiApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WisdomUiState())
    val uiState: StateFlow<WisdomUiState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val articles = rssRepository.fetchAllFeeds()
                _uiState.update { it.copy(articles = articles, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load articles: ${e.message}") }
            }
        }
    }

    fun selectArticle(article: RssItem) {
        _uiState.update { it.copy(selectedArticle = article, articleSummary = null) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedArticle = null, articleSummary = null) }
    }

    fun summarizeArticle(article: RssItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSummarizing = true) }
            val summary = geminiApiService.summarizeArticle(article.title, article.description)
            _uiState.update { it.copy(isSummarizing = false, articleSummary = summary) }
        }
    }
}
