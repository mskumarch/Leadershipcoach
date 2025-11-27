package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home.HomeIdleState
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Chat Screen - Main Interaction Hub
 *
 * Refactored to use MaterialTheme colors and modular components.
 */
@Composable
fun ChatScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    hasRecordAudioPermission: Boolean = false,
    onNavigateToPractice: () -> Unit
) {
    val sessionState by viewModel.sessionState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showSessionModeModal by remember { mutableStateOf(false) }
    var showQuickActions by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (sessionState.isRecording) {
            // ============================================================
            // RECORDING STATE
            // ============================================================
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Simple Status Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pulsing recording dot
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 0.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "alpha"
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.error.copy(alpha = alpha), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Recording",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = sessionState.duration,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Main content area - Messages flow
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (sessionState.messages.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .padding(bottom = 100.dp), // Space for FABs and Input
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                sessionState.messages.forEach { message ->
                                    when (message.type) {
                                        // Coaching cards
                                        MessageType.URGENT_NUDGE -> {
                                            CoachingBanner(
                                                type = BannerType.CRITICAL_NUDGE,
                                                message = message.content,
                                                copyableText = null,
                                                onDismiss = { viewModel.removeMessage(message.id) },
                                                onGotIt = { viewModel.removeMessage(message.id) }
                                            )
                                        }
                                        MessageType.IMPORTANT_PROMPT, MessageType.HELPFUL_TIP, MessageType.CONTEXT -> {
                                            CoachingBanner(
                                                type = BannerType.HELPFUL_SUGGESTION,
                                                message = message.content,
                                                copyableText = null,
                                                onDismiss = { viewModel.removeMessage(message.id) },
                                                onGotIt = { viewModel.removeMessage(message.id) }
                                            )
                                        }

                                        // User question bubble
                                        MessageType.USER_QUESTION -> {
                                            UserBubble(content = message.content)
                                        }

                                        // AI response bubble
                                        MessageType.AI_RESPONSE -> {
                                            GlassmorphicAICard {
                                                Text(
                                                    text = message.content,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        } else {
                            // Empty state while recording (listening)
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Listening...",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }

                    // Input Area
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Transparent,
                        shadowElevation = 0.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .padding(bottom = 16.dp) // Reduced padding
                        ) {
                            ChatInputField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                onSend = {
                                    if (inputText.isNotBlank()) {
                                        viewModel.addUserMessage(inputText)
                                        val aiResponse = viewModel.getAIResponse(inputText)
                                        viewModel.addAIResponse(aiResponse)
                                        inputText = ""
                                    }
                                }
                            )
                        }
                    }
                }

                // Floating Stop Button - Centered above input
                FloatingActionButton(
                    onClick = { viewModel.stopSession() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp), // Aligned with Menu FAB
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop")
                        Text("Stop Session", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Actions Sheet - PULL
            if (showQuickActions) {
                QuickActionsSheet(
                    suggestedQuestions = viewModel.getSuggestedQuestions(sessionState.mode),
                    onQuestionSelected = { question ->
                        // Send message immediately
                        viewModel.addUserMessage(question)
                        val aiResponse = viewModel.getAIResponse(question)
                        viewModel.addAIResponse(aiResponse)
                        showQuickActions = false
                    },
                    onActionSelected = { command ->
                        val prompt = when (command) {
                            ActionCommand.SUMMARIZE_LAST_10_MIN -> "Summarize the last 10 minutes of this conversation."
                            ActionCommand.EXPLAIN_RESPONSE -> "Analyze the last response and explain the underlying sentiment."
                            ActionCommand.CHECK_TONE -> "Check my tone in the recent transcripts. Am I speaking too fast or too aggressively?"
                            ActionCommand.WHAT_DID_I_MISS -> "What cues or important points might I have missed recently?"
                            ActionCommand.SUGGEST_NEXT_QUESTION -> "Suggest a good follow-up question based on the current context."
                            ActionCommand.HOW_AM_I_DOING -> "Evaluate my performance in this session so far (empathy, listening, clarity)."
                        }
                        viewModel.getAIResponse(prompt)
                        showQuickActions = false
                    },
                    onDismiss = { showQuickActions = false }
                )
            }

            // Floating Action Button - Menu
            FloatingActionButton(
                onClick = { showQuickActions = true },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 100.dp) // Above nav
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Quick Actions",
                    modifier = Modifier.size(28.dp)
                )
            }
        } else {
            HomeIdleState(
                onStartSession = { showSessionModeModal = true }
            )
        }

        // Session Mode Modal
        if (showSessionModeModal) {
            SessionModeModal(
                onModeSelected = { mode ->
                    if (mode == com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode.ROLEPLAY) {
                        onNavigateToPractice()
                    } else {
                        viewModel.startSession(mode, hasRecordAudioPermission)
                    }
                    showSessionModeModal = false
                },
                onDismiss = { showSessionModeModal = false }
            )
        }
    }
}

// Helper for tips
@Composable
private fun TipRow(icon: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}