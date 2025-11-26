package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.CoachBottomNavigationBar
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.ChatScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.CoachScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.HistoryScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.settings.SettingsScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.TranscriptScreen
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Main Navigation Screen with Bottom Navigation
 *
 * All screens share the same SessionViewModel instance
 * State is synchronized across all tabs
 */
import androidx.compose.runtime.mutableStateOf
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.CalmGreenStart
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.CalmGreenEnd
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.SessionDetailScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.practice.PracticeModeScreen

import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.gamification.AchievementsScreen

@Composable
fun NavigationScreen(
    hasRecordAudioPermission: Boolean = true,
    viewModel: SessionViewModel = hiltViewModel() // Single shared instance
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedSessionId by remember { mutableStateOf<Long?>(null) }
    var showPracticeMode by remember { mutableStateOf(false) }
    var showAchievements by remember { mutableStateOf(false) }

    // Handle system back press
    BackHandler(enabled = selectedSessionId != null || showPracticeMode || showAchievements) {
        if (selectedSessionId != null) {
            selectedSessionId = null
        } else if (showPracticeMode) {
            showPracticeMode = false
        } else if (showAchievements) {
            showAchievements = false
        }
    }

    Scaffold(
        bottomBar = {
            if (selectedSessionId == null && !showPracticeMode && !showAchievements) {
                CoachBottomNavigationBar(
                    currentDestination = when (selectedTab) {
                        0 -> "chat"
                        1 -> "transcript"
                        2 -> "progress"
                        3 -> "history"
                        4 -> "settings"
                        else -> "chat"
                    },
                    onNavigate = { destination ->
                        selectedTab = when (destination) {
                            "chat" -> 0
                            "transcript" -> 1
                            "progress" -> 2
                            "history" -> 3
                            "settings" -> 4
                            else -> 0
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        // Full screen box with calm green gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CalmGreenStart, CalmGreenEnd)
                    )
                )
        ) {
            if (showPracticeMode) {
                PracticeModeScreen(
                    onBackClick = { showPracticeMode = false },
                    onScenarioClick = { /* TODO: Start Scenario */ }
                )
            } else if (showAchievements) {
                AchievementsScreen(
                    onBackClick = { showAchievements = false }
                )
            } else if (selectedSessionId != null) {
                SessionDetailScreen(
                    sessionId = selectedSessionId!!,
                    onBackClick = { selectedSessionId = null }
                )
            } else {
                when (selectedTab) {
                    0 -> ChatScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(paddingValues),
                        hasRecordAudioPermission = hasRecordAudioPermission,
                        onNavigateToPractice = { showPracticeMode = true }
                    )
                    1 -> TranscriptScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(paddingValues)
                    )
                    2 -> {
                        com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress.ProgressScreen(
                            modifier = Modifier.padding(paddingValues),
                            onAchievementsClick = { showAchievements = true }
                        )
                    }
                    3 -> HistoryScreen(
                        onSessionClick = { sessionId -> selectedSessionId = sessionId },
                        modifier = Modifier.padding(paddingValues)
                    )
                    4 -> SettingsScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}