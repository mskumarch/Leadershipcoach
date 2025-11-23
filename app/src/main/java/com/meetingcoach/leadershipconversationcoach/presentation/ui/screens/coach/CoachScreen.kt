package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.CoachEmptyState
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.CoachingTipsCarousel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.PulsingIndicator
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.SessionModeBadge
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
        modifier = modifier.fillMaxSize()
    ) {
        val currentMode = sessionState.mode
        if (sessionState.isRecording && currentMode != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE8F5F3),
                                Color(0xFFF3E5F5)
                            )
                        )
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                SessionModeBadge(
                    mode = currentMode,
                    duration = sessionState.duration,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PulsingIndicator(
                        label = "Listening Actively",
                        isActive = sessionState.messages.isNotEmpty()
                    )

                    PulsingIndicator(
                        label = "Analyzing Conversation",
                        isActive = sessionState.messages.size > 3
                    )

                    PulsingIndicator(
                        label = "Coaching Engaged",
                        isActive = sessionState.messages.any { it.type.name.contains("NUDGE") || it.type.name.contains("TIP") }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                CoachingTipsCarousel(
                    sessionMode = currentMode,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            CoachEmptyState(
                modifier = Modifier.fillMaxSize()
            )
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