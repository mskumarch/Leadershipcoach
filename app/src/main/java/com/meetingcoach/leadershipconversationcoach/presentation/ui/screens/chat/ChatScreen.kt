package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.NotePanel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SentimentIndicator
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home.HomeIdleState
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.GradientBackground
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.MetricsHUD
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StreamingTranscriptBubble
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

    // Auto-scroll state
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    
    // Scroll to bottom on new message
    LaunchedEffect(sessionState.messages.size) {
        if (sessionState.messages.isNotEmpty()) {
            listState.animateScrollToItem(sessionState.messages.size - 1)
        }
    }

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

    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground(modifier = modifier) {
        // Snackbar for Guardian nudges
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter) // Move to top for better visibility
                .padding(top = 80.dp)
        )
        
        if (sessionState.isRecording) {
            if (sessionState.mode == com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode.DYNAMICS) {
                com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.dynamics.DynamicsRecordingScreen(
                    viewModel = viewModel,
                    onStopSession = { viewModel.stopSession() }
                )
            } else {
                // ============================================================
                // RECORDING STATE - THE COCKPIT
                // ============================================================
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 1. Metrics HUD (Glassmorphic Top Bar) - Ghost Mode Logic
                        // Calculate alpha based on health: If talk ratio is balanced (30-70), fade out to 0.3f
                        val isHealthy = sessionState.metrics.talkRatio in 30..70
                        val hudAlpha by animateFloatAsState(
                            targetValue = if (isHealthy) 0.3f else 1.0f,
                            animationSpec = tween(durationMillis = 1000)
                        )

                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .alpha(hudAlpha)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                MetricsHUD(
                                    isRecording = !sessionState.isPaused,
                                    duration = sessionState.duration,
                                    talkRatio = sessionState.metrics.talkRatio,
                                    qualityScore = (sessionState.metrics.openQuestionCount * 10).coerceAtMost(100)
                                )
                                
                                // Live Coaching Controls
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Sentiment (Mocked for now, connect to VM later)
                                    SentimentIndicator(sentiment = "Engaged")
                                    
                                    // Note Panel (Scrollable)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        NotePanel(
                                            onCategorySelected = { category: String ->
                                                // TODO: Handle category selection (e.g., filter notes or add tag)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // 2. Main Feed (LazyColumn)
                        androidx.compose.foundation.lazy.LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(sessionState.messages) { message: com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage ->
                                when (message.type) {
                                    // LIVE TRANSCRIPT
                                    MessageType.TRANSCRIPT -> {
                                        StreamingTranscriptBubble(
                                            text = message.content,
                                            isFinal = true, // Assuming stored messages are final chunks
                                            speaker = message.speaker?.name ?: "UNKNOWN"
                                        )
                                    }
                                    
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
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                            UserMessageBubble(message = message.content)
                                        }
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
                            
                            // Live Partial Transcript
                            if (sessionState.partialTranscript.isNotEmpty()) {
                                item {
                                    StreamingTranscriptBubble(
                                        text = sessionState.partialTranscript,
                                        isFinal = false,
                                        speaker = "You"
                                    )
                                }
                            }
                        }
                    }

                    // 3. Floating Controls (Bottom)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                 brush = Brush.verticalGradient(
                                     colors = listOf(Color.Transparent, AppPalette.Stone50.copy(alpha = 0.9f))
                                 )
                            )
                            .padding(16.dp)
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Input Area
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Magic Wand (Quick Actions)
                            SmallFloatingActionButton(
                                onClick = { showQuickActions = true },
                                containerColor = AppPalette.Lavender500,
                                contentColor = Color.White,
                                shape = CircleShape
                            ) {
                                Text("✨", fontSize = 20.sp)
                            }
                            
                            // Text Input
                            Box(modifier = Modifier.weight(1f)) {
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
                        
                        // Session Controls (Pause/Stop)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Pause/Resume
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (sessionState.isPaused) viewModel.resumeSession() else viewModel.pauseSession()
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(AppPalette.Stone100, CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (sessionState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = if (sessionState.isPaused) "Resume" else "Pause",
                                    tint = DeepCharcoal
                                )
                            }

                            // Stop (Prominent)
                            Button(
                                onClick = { 
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.stopSession() 
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Red500),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("END SESSION", fontWeight = FontWeight.Bold)
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
                            onQuestionSelected = { question: String ->
                                viewModel.addUserMessage(question)
                                val aiResponse = viewModel.getAIResponse(question)
                                viewModel.addAIResponse(aiResponse)
                                showQuickActions = false
                            },
                            onActionSelected = { command: ActionCommand ->
                                val prompt = when (command) {
                                    ActionCommand.SUMMARIZE_LAST_10_MIN -> promptSummarize
                                    ActionCommand.EXPLAIN_RESPONSE -> promptExplain
                                    ActionCommand.CHECK_TONE -> promptCheckTone
                                    ActionCommand.WHAT_DID_I_MISS -> promptWhatMissed
                                    ActionCommand.SUGGEST_NEXT_QUESTION -> promptSuggestQuestion
                                    ActionCommand.HOW_AM_I_DOING -> promptEvaluate
                                    else -> ""
                                }
                                viewModel.getAIResponse(prompt)
                                showQuickActions = false
                            },
                            onDismiss = { showQuickActions = false }
                        )
                    }
                }
            }
        } else {
            // IDLE STATE
            HomeIdleState(
                onStartSession = { showSessionModeModal = true }
            )
        }

        // Session Mode Modal
        if (showSessionModeModal) {
            SessionModeModal(
                onModeSelected = { mode: com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode ->
                    if (mode == com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode.ROLEPLAY) {
                        onNavigateToPractice()
                        showSessionModeModal = false
                    } else {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.startSession(mode, hasRecordAudioPermission)
                        showSessionModeModal = false
                    }
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