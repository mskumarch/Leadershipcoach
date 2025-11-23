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
@Composable
fun NavigationScreen(
    hasRecordAudioPermission: Boolean = true,
    viewModel: SessionViewModel = hiltViewModel() // Single shared instance
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent, // Transparent to prevent black rectangle
        bottomBar = {
            CoachBottomNavigationBar(
                currentDestination = when (selectedTab) {
                    0 -> "chat"
                    1 -> "transcript"
                    2 -> "coach"
                    3 -> "history"
                    4 -> "settings"
                    else -> "chat"
                },
                onNavigate = { destination ->
                    selectedTab = when (destination) {
                        "chat" -> 0
                        "transcript" -> 1
                        "coach" -> 2
                        "history" -> 3
                        "settings" -> 4
                        else -> 0
                    }
                }
            )
        }
    ) { paddingValues ->
        // Full screen box with sage green background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Sage green
        ) {
            when (selectedTab) {
                0 -> ChatScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues),
                    hasRecordAudioPermission = hasRecordAudioPermission
                )
                1 -> TranscriptScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues)
                )
                2 -> CoachScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(paddingValues),
                    hasRecordAudioPermission = hasRecordAudioPermission
                )
                3 -> HistoryScreen(
                    modifier = Modifier.padding(paddingValues)
                )
                4 -> SettingsScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}