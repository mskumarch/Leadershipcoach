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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
    val audioLevel by viewModel.audioLevel.collectAsState()
    val currentGrowStage by viewModel.currentGrowStage.collectAsState()
    val suggestedQuestion by viewModel.suggestedQuestion.collectAsState()
    val activeNudge by viewModel.activeNudge.collectAsState()
    
    var inputText by remember { mutableStateOf("") }
    var showSessionModeModal by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var wasRecording by remember { mutableStateOf(false) }

    LaunchedEffect(sessionState.isRecording) {
        if (wasRecording && !sessionState.isRecording) {
            // Session just stopped
            showSaveDialog = true
        }
        wasRecording = sessionState.isRecording
    }

    var showQuickActions by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    // Show Guardian nudges as snackbar
    LaunchedEffect(activeNudge) {
        activeNudge?.let { nudge ->
            snackbarHostState.showSnackbar(
                message = nudge.message,
                duration = androidx.compose.material3.SnackbarDuration.Short
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Snackbar for Guardian nudges
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
        
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
                        // Pulsing recording dot with live amplitude
                        PulsingConcentricCircles(
                            modifier = Modifier.size(24.dp),
                            isActive = sessionState.isRecording && !sessionState.isPaused,
                            amplitude = audioLevel,
                            size = 24.dp,
                            centerColor = if (sessionState.isPaused) AppPalette.Stone500 else MaterialTheme.colorScheme.error,
                            ringColor = if (sessionState.isPaused) AppPalette.Stone300 else MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (sessionState.isPaused) androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.status_paused) else androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.status_recording),
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

                    // Metrics Bar (Talk Ratio & Quality)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Talk Ratio Gauge
                        com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.TalkRatioGauge(
                            userTalkRatio = sessionState.metrics.talkRatio,
                            size = 48.dp
                        )

                        // Question Quality Badge
                        com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.QuestionQualityBadge(
                            openQuestionCount = sessionState.metrics.openQuestionCount,
                            totalQuestionCount = sessionState.metrics.questionCount
                        )
                    }
                    
                    // GROW Stage Indicator (Only for 1:1)
                    if (sessionState.mode == com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode.ONE_ON_ONE && currentGrowStage != "START") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GrowStageIndicator(currentStage = currentGrowStage)
                        }
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
                                            if (message.content == "Thinking...") {
                                                GlassmorphicAICard {
                                                    TypingIndicator()
                                                }
                                            } else {
                                                GlassmorphicAICard {
                                                    Text(
                                                        text = message.content,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
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
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
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

                // Floating Controls - Centered above input
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pause/Resume Button
                    SmallFloatingActionButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (sessionState.isPaused) {
                                viewModel.resumeSession()
                            } else {
                                viewModel.pauseSession()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Icon(
                            imageVector = if (sessionState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (sessionState.isPaused) "Resume" else "Pause"
                        )
                    }

                    // Stop Button
                    FloatingActionButton(
                        onClick = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.stopSession() 
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "Stop")
                            Text(androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.stop_session), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick Actions Sheet
            if (showQuickActions) {
                val promptSummarize = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_summarize)
                val promptExplain = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_explain)
                val promptCheckTone = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_check_tone)
                val promptWhatMissed = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_what_missed)
                val promptSuggestQuestion = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_suggest_question)
                val promptEvaluate = androidx.compose.ui.res.stringResource(com.meetingcoach.leadershipconversationcoach.R.string.prompt_evaluate)

                QuickActionsSheet(
                    suggestedQuestions = viewModel.getSuggestedQuestions(sessionState.mode),
                    dynamicQuestion = suggestedQuestion?.suggestedQuestion,
                    onQuestionSelected = { question ->
                        // Send message immediately
                        viewModel.addUserMessage(question)
                        val aiResponse = viewModel.getAIResponse(question)
                        viewModel.addAIResponse(aiResponse)
                        showQuickActions = false
                    },

                    onActionSelected = { command ->
                        val prompt = when (command) {
                            ActionCommand.SUMMARIZE_LAST_10_MIN -> promptSummarize
                            ActionCommand.EXPLAIN_RESPONSE -> promptExplain
                            ActionCommand.CHECK_TONE -> promptCheckTone
                            ActionCommand.WHAT_DID_I_MISS -> promptWhatMissed
                            ActionCommand.SUGGEST_NEXT_QUESTION -> promptSuggestQuestion
                            ActionCommand.HOW_AM_I_DOING -> promptEvaluate
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
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.startSession(mode, hasRecordAudioPermission)
                    }
                    showSessionModeModal = false
                },
                onDismiss = { showSessionModeModal = false }
            )
        }

        // Save Session Title Dialog
        if (showSaveDialog) {
            var titleInput by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("Save Session") },
                text = {
                    Column {
                        Text("Add a title to this session to find it easily later.")
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            label = { Text("Session Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (titleInput.isNotBlank()) {
                                viewModel.updateLastSessionTitle(titleInput)
                            }
                            showSaveDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveDialog = false }) {
                        Text("Skip")
                    }
                }
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