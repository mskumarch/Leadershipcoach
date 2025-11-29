package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.zIndex
import androidx.compose.foundation.border
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
    
    // Scroll to bottom on new message or partial transcript
    LaunchedEffect(sessionState.messages.size, sessionState.partialTranscript) {
        val totalItems = sessionState.messages.size + if (sessionState.partialTranscript.isNotEmpty()) 1 else 0
        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
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

    // Main Background
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GlassDesign.EtherealBackground)
    ) {
        // Snackbar for Guardian nudges
        androidx.compose.material3.SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .zIndex(10f)
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
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Header "Ask AI Coach" (Glassy)
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .padding(top = 40.dp), // Status bar
                        shape = RoundedCornerShape(24.dp),
                        alpha = 0.6f
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { /* TODO: Back */ }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppPalette.Sage900)
                            }
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Ask AI Coach",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppPalette.Sage900
                                )
                                // Live Status
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(AppPalette.Red500, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = sessionState.duration ?: "00:00",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AppPalette.Sage700
                                    )
                                }
                            }
                            
                            IconButton(onClick = { /* TODO: Menu */ }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = AppPalette.Sage900)
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(sessionState.messages) { message: com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage ->
                            when (message.type) {
                                // LIVE TRANSCRIPT
                                MessageType.TRANSCRIPT -> {
                                    StreamingTranscriptBubble(
                                        text = message.content,
                                        isFinal = true,
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        // AI Avatar
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(AppPalette.Sage100, CircleShape)
                                                .border(1.dp, Color.White, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🤖", fontSize = 16.sp)
                                        }
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        if (message.content == "Thinking...") {
                                            GlassCard(
                                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                                                alpha = 0.8f
                                            ) {
                                                Box(modifier = Modifier.padding(12.dp)) {
                                                    TypingIndicator()
                                                }
                                            }
                                        } else {
                                            GlassCard(
                                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
                                                alpha = 0.8f
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    Text(
                                                        text = "AI Coach",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = AppPalette.Sage700,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = message.content,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = AppPalette.Sage900
                                                    )
                                                }
                                            }
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

                    // 3. Floating Controls (Bottom)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(bottom = 80.dp), // Space for nav
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Suggested Actions (Chips)
                        if (!sessionState.isPaused) {
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SuggestionChip(
                                    onClick = { viewModel.addUserMessage("What did I miss?") },
                                    label = { Text("What did I miss?") },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                                SuggestionChip(
                                    onClick = { viewModel.addUserMessage("Check my tone") },
                                    label = { Text("Check my tone") },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                                SuggestionChip(
                                    onClick = { viewModel.addUserMessage("Summarize") },
                                    label = { Text("Summarize") },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }

                        // Input Area
                        GlassCard(
                            shape = RoundedCornerShape(50),
                            alpha = 0.9f
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Magic Wand
                                IconButton(onClick = { showQuickActions = true }) {
                                    Text("✨", fontSize = 20.sp)
                                }
                                
                                // Text Input
                                Box(modifier = Modifier.weight(1f)) {
                                    BasicTextField(
                                        value = inputText,
                                        onValueChange = { inputText = it },
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = AppPalette.Sage900),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        decorationBox = { innerTextField ->
                                            if (inputText.isEmpty()) {
                                                Text("Ask your coach...", style = MaterialTheme.typography.bodyMedium, color = AppPalette.Sage900.copy(alpha = 0.5f))
                                            }
                                            innerTextField()
                                        }
                                    )
                                }
                                
                                // Send / Mic
                                IconButton(
                                    onClick = {
                                        if (inputText.isNotBlank()) {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.addUserMessage(inputText)
                                            val aiResponse = viewModel.getAIResponse(inputText)
                                            viewModel.addAIResponse(aiResponse)
                                            inputText = ""
                                        }
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(AppPalette.Sage700, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (inputText.isNotBlank()) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                                        contentDescription = "Send",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    
                    // Quick Actions Sheet
                    if (showQuickActions) {
                        // ... (Keep existing logic)
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