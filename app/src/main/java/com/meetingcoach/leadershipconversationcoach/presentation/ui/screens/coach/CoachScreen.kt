package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach

import androidx.compose.foundation.background
<<<<<<< HEAD
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
=======
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
>>>>>>> 43e5bbd (feat: Redesign app icon, enhance UI across multiple screens, and add new documentation.)
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
=======
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
>>>>>>> 43e5bbd (feat: Redesign app icon, enhance UI across multiple screens, and add new documentation.)
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
<<<<<<< HEAD
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.CoachEmptyState
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.CoachingTipsCarousel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.PulsingIndicator
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components.SessionModeBadge
=======
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
>>>>>>> 43e5bbd (feat: Redesign app icon, enhance UI across multiple screens, and add new documentation.)
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
<<<<<<< HEAD
        modifier = modifier.fillMaxSize()
    ) {
        val currentMode = sessionState.mode
        if (sessionState.isRecording && currentMode != null) {
=======
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (sessionState.isRecording) {
            // Show session controls with modern design
>>>>>>> 43e5bbd (feat: Redesign app icon, enhance UI across multiple screens, and add new documentation.)
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
<<<<<<< HEAD
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
=======
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    GlossyPrimaryStart,
                                    GlossyPrimaryEnd
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🧠",
                        fontSize = 60.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Session Active",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mode card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mode",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = sessionState.mode?.getDisplayName() ?: "Unknown",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Duration card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Duration",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = sessionState.duration,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        } else {
            // Show welcome screen with modern design
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(70.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    GlossySecondaryStart,
                                    GlossySecondaryEnd
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🧠",
                        fontSize = 70.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "AI Coach",
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
            }
>>>>>>> 43e5bbd (feat: Redesign app icon, enhance UI across multiple screens, and add new documentation.)
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