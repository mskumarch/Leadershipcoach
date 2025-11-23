package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.FloatingEmptyState
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.ModernRecordingInterface
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.SettingsCard
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Coach Screen - Premium Sage/Taupe Design
 *
 * Personal growth sanctuary aesthetic with:
 * - Sage green background
 * - Warm taupe session cards
 * - Glassmorphic elements
 * - Smooth animations
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
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Sage green
    ) {
        if (sessionState.isRecording) {
            // Modern recording interface with sage/taupe colors
            ModernRecordingInterface(
                isRecording = true,
                sessionMode = sessionState.mode,
                duration = sessionState.duration,
                onStartRecording = { },
                onStopRecording = { viewModel.stopSession() }
            )
        } else {
            // Welcome screen with premium design
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Floating empty state with sage green
                FloatingEmptyState(
                    icon = "🧠",
                    title = "AI Leadership Coach",
                    subtitle = "Select a session mode to begin your coaching journey"
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Session mode cards with warm taupe
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    SessionModeCard(
                        title = "1-on-1 Conversation",
                        description = "Build trust and deep connection",
                        emoji = "💬",
                        onClick = { showSessionModeModal = true }
                    )
                    
                    SessionModeCard(
                        title = "Team Meeting",
                        description = "Facilitate inclusive discussions",
                        emoji = "👥",
                        onClick = { showSessionModeModal = true }
                    )
                    
                    SessionModeCard(
                        title = "Difficult Conversation",
                        description = "Navigate challenging topics with care",
                        emoji = "🎯",
                        onClick = { showSessionModeModal = true }
                    )
                }
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

/**
 * Premium Session Mode Card - Warm Taupe Glass
 */
@Composable
private fun SessionModeCard(
    title: String,
    description: String,
    emoji: String,
    onClick: () -> Unit
) {
    SettingsCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            color = androidx.compose.ui.graphics.Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Emoji in sage green circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    SageGreen.copy(alpha = 0.3f),
                                    SageGreen.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 28.sp
                    )
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepCharcoal
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeutralGray
                    )
                }
            }
        }
    }
}