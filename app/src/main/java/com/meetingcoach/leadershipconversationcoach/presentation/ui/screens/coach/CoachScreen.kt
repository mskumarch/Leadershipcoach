package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.ModernRecordingInterface
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PulsingConcentricCircles
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Coach Screen - Modern UI
 *
 * Beautiful, modern interface with:
 * - Pulsing concentric circles for recording
 * - Waveform visualization
 * - Gradient backgrounds
 * - Organic shapes
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (sessionState.isRecording) {
            // Modern recording interface
            ModernRecordingInterface(
                isRecording = true,
                sessionMode = sessionState.mode,
                duration = sessionState.duration,
                onStartRecording = { },
                onStopRecording = { viewModel.stopSession() }
            )
        } else {
            // Welcome screen with modern design
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AccentLavender.copy(alpha = 0.1f),
                                AccentPeach.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    // Animated pulsing circle
                    PulsingConcentricCircles(
                        isActive = true,
                        centerColor = AccentLavender,
                        ringColor = AccentLavender.copy(alpha = 0.3f),
                        size = 180.dp
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Text(
                        text = "AI Leadership Coach",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Select a session mode to begin\nyour coaching journey",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Session mode cards
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        SessionModeCard(
                            title = "1-on-1 Conversation",
                            description = "Build trust and connection",
                            gradient = GradientMintTeal,
                            emoji = "💬",
                            onClick = { showSessionModeModal = true }
                        )
                        
                        SessionModeCard(
                            title = "Team Meeting",
                            description = "Facilitate group discussions",
                            gradient = GradientLavenderSky,
                            emoji = "👥",
                            onClick = { showSessionModeModal = true }
                        )
                        
                        SessionModeCard(
                            title = "Difficult Conversation",
                            description = "Navigate challenging topics",
                            gradient = GradientCoralRose,
                            emoji = "🎯",
                            onClick = { showSessionModeModal = true }
                        )
                    }
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

@Composable
private fun SessionModeCard(
    title: String,
    description: String,
    gradient: List<androidx.compose.ui.graphics.Color>,
    emoji: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradient.map { it.copy(alpha = 0.2f) }
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Emoji circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = gradient
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
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}