package com.meetingcoach.leadershipconversationcoach.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meetingcoach.leadershipconversationcoach.data.local.AchievementEntity
import com.meetingcoach.leadershipconversationcoach.data.repository.GamificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val gamificationRepository: GamificationRepository
) : ViewModel() {

    val achievements: StateFlow<List<AchievementEntity>> = gamificationRepository.allAchievements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
