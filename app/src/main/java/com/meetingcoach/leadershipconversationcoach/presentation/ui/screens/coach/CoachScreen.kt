package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Coach Screen
 *
 * Two states:
 * 1. NOT Recording: Shows SessionModeModal automatically
 * 2. Recording: Shows session controls (progress rings, stats, etc.)
 */
@Composable
fun CoachScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    hasRecordAudioPermission: Boolean = true
) {
    val sessionState by viewModel.sessionState.collectAsState()
    var showSessionModeModal by remember { mutableStateOf(!sessionState.isRecording) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (sessionState.isRecording) {
            // Show session controls
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🧠 Session Active",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Mode: ${sessionState.mode?.getDisplayName() ?: "Unknown"}",
                    fontSize = 16.sp
                )

                Text(
                    text = "Duration: ${sessionState.duration}",
                    fontSize = 16.sp
                )

                // TODO: Add progress rings here
            }
        } else {
            // Show welcome text (modal will appear automatically)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🧠",
                    fontSize = 64.sp
                )

                Text(
                    text = "Coach Tab",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Select session mode to begin",
                    fontSize = 14.sp
                )
            }
        }
    }

    // Show modal when not recording
    if (showSessionModeModal && !sessionState.isRecording) {
        SessionModeModal(
            onModeSelected = { mode ->
                viewModel.startSession(mode, hasRecordAudioPermission)
                showSessionModeModal = false
            },
            onDismiss = { showSessionModeModal = false }
        )
    }
}