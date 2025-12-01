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
import androidx.compose.ui.unit.dp
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
import androidx.compose.animation.Crossfade
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Brush
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.CalmGreenStart
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.CalmGreenEnd
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.SessionDetailScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.practice.PracticeModeScreen
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.wisdom.WisdomScreen

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
        // Removed standard bottom bar
    ) { paddingValues ->
        // Full screen box with calm green gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
        ) {
            // Background Image
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.meetingcoach.leadershipconversationcoach.R.drawable.background_clouds),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Main Content
            Box(modifier = Modifier.padding(bottom = if (selectedSessionId == null && !showPracticeMode && !showAchievements) 80.dp else 0.dp)) {
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
                    Crossfade(targetState = selectedTab, label = "TabTransition") { tab ->
                        when (tab) {
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
                                onStartSession = { selectedTab = 0 },
                                modifier = Modifier.padding(paddingValues)
                            )
                            4 -> SettingsScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                            5 -> WisdomScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                    }
                }
            }

            // Floating Pill Navigation (Bottom Center)
            if (selectedSessionId == null && !showPracticeMode && !showAchievements) {
                com.meetingcoach.leadershipconversationcoach.presentation.ui.components.FloatingPillNav(
                    currentTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}