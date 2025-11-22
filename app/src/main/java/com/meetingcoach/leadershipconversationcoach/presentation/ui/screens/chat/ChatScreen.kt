package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.AIBubble
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.ActionCommand
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.BannerType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.ChatInputField
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.CoachingBanner
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.QuickActionsSheet
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.SessionModeModal
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.UserBubble
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components.getContextualQuestions
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel

/**
 * Chat Screen - Main Interface with SharedViewModel
 *
 * Features:
 * - Inline coaching nudges (PUSH)
 * - Quick actions menu (PULL)
 * - AI Q&A
 * - Session mode selector
 * - All state managed by SharedViewModel
 * - Synced with all other screens
 *
 * Optimized for: Virtual meetings with phone/tablet next to laptop
 *
 * NOTE: This screen does NOT use Scaffold because the parent NavigationScreen
 * already has a Scaffold with bottom navigation. Text input is part of content.
 */
@Composable
fun ChatScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    hasRecordAudioPermission: Boolean = true  // ✅ ADDED PARAMETER
) {
    // Observe session state from SharedViewModel
    val sessionState by viewModel.sessionState.collectAsState()

    // Local UI state
    var inputText by remember { mutableStateOf("") }
    var showQuickActions by remember { mutableStateOf(false) }
    var showSessionModeModal by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F9FF),
                        Color(0xFFF5F3FF)
                    )
                )
            )
    ) {
        if (sessionState.isRecording) {
            // ============================================================
            // RECORDING STATE - Minimal View with Inline Nudges
            // ============================================================
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                MinimalStatusBar(
                    duration = sessionState.duration,
                    onStop = {
                        viewModel.stopSession()
                    }
                )

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
                                .padding(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sessionState.messages.forEach { message ->
                                when (message.type) {
                                    // ✅ Coaching cards - map to BannerType
                                    MessageType.URGENT_NUDGE -> {
                                        CoachingBanner(
                                            type = BannerType.CRITICAL_NUDGE,
                                            message = message.content,
                                            copyableText = null, // Domain model doesn't have this yet
                                            onDismiss = {
                                                viewModel.removeMessage(message.id)
                                            },
                                            onGotIt = {
                                                viewModel.removeMessage(message.id)
                                            }
                                        )
                                    }
                                    MessageType.IMPORTANT_PROMPT -> {
                                        CoachingBanner(
                                            type = BannerType.HELPFUL_SUGGESTION,
                                            message = message.content,
                                            copyableText = null,
                                            onDismiss = {
                                                viewModel.removeMessage(message.id)
                                            },
                                            onGotIt = {
                                                viewModel.removeMessage(message.id)
                                            }
                                        )
                                    }
                                    MessageType.HELPFUL_TIP -> {
                                        CoachingBanner(
                                            type = BannerType.HELPFUL_SUGGESTION,
                                            message = message.content,
                                            copyableText = null,
                                            onDismiss = {
                                                viewModel.removeMessage(message.id)
                                            },
                                            onGotIt = {
                                                viewModel.removeMessage(message.id)
                                            }
                                        )
                                    }
                                    MessageType.CONTEXT -> {
                                        // Context cards could use a different banner style
                                        // For now, using helpful suggestion
                                        CoachingBanner(
                                            type = BannerType.HELPFUL_SUGGESTION,
                                            message = message.content,
                                            copyableText = null,
                                            onDismiss = {
                                                viewModel.removeMessage(message.id)
                                            },
                                            onGotIt = {
                                                viewModel.removeMessage(message.id)
                                            }
                                        )
                                    }

                                    // ✅ User question bubble
                                    MessageType.USER_QUESTION -> {
                                        UserBubble(content = message.content)
                                    }

                                    // ✅ AI response bubble
                                    MessageType.AI_RESPONSE -> {
                                        AIBubble(content = message.content)
                                    }

                                    // ✅ Transcript messages don't show in Chat
                                    MessageType.TRANSCRIPT -> {
                                        // Transcript messages appear in Transcript tab only
                                    }

                                    // ✅ Instruction card (welcome message)
                                    MessageType.INSTRUCTION -> {
                                        // Could show instruction card here if needed
                                    }
                                }
                            }
                        }
                    } else {
                        // Calm empty space
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💬",
                                fontSize = 48.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Focus on your conversation",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF6B7280),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "I'll coach you inline as we go",
                                fontSize = 14.sp,
                                color = Color(0xFF9CA3AF),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "☰ Tap menu for quick actions",
                                fontSize = 13.sp,
                                color = Color(0xFF3B82F6),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            spotColor = Color.Black.copy(alpha = 0.15f),
                            ambientColor = Color.Black.copy(alpha = 0.08f)
                        ),
                    color = Color.White.copy(alpha = 0.95f)
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

            // Quick Actions Sheet - PULL
            if (showQuickActions) {
                QuickActionsSheet(
                    suggestedQuestions = getContextualQuestions(
                        hasEmotion = true,
                        longSilence = false,
                        topicChange = false
                    ),
                    onQuestionSelected = { question ->
                        inputText = question
                        showQuickActions = false
                    },
                    onActionSelected = { command ->
                        when (command) {
                            ActionCommand.SUMMARIZE_LAST_10_MIN -> {
                                viewModel.addAIResponse(
                                    "📝 Summary of last 10 minutes:\n\n" +
                                            "• Sarah expressed concerns about timeline\n" +
                                            "• You asked 3 clarifying questions\n" +
                                            "• Team discussed budget constraints\n" +
                                            "• Next step: Follow up on testing phase"
                                )
                            }
                            ActionCommand.EXPLAIN_RESPONSE -> {
                                viewModel.addAIResponse(
                                    "💭 Their last response analysis:\n\n" +
                                            "They're expressing stress about deadlines. " +
                                            "Their tone suggests they need support, not solutions yet. " +
                                            "Good follow-up: Ask how you can help."
                                )
                            }
                            ActionCommand.CHECK_TONE -> {
                                viewModel.addAIResponse(
                                    "🎤 Your tone check:\n\n" +
                                            "✅ You're speaking calmly\n" +
                                            "✅ Good empathy in recent responses\n" +
                                            "⚠️ You interrupted once at 3:45\n" +
                                            "💡 Try pausing 2 seconds before responding"
                                )
                            }
                            ActionCommand.WHAT_DID_I_MISS -> {
                                viewModel.addAIResponse(
                                    "❗Key moments you may have missed:\n\n" +
                                            "• 2:34 - Speaker mentioned feeling 'overwhelmed'\n" +
                                            "• 5:12 - Agreement on timeline concerns\n" +
                                            "• 8:45 - Consensus reached on budget approach"
                                )
                            }
                            ActionCommand.SUGGEST_NEXT_QUESTION -> {
                                viewModel.addAIResponse(
                                    "🔮 Based on the conversation, try asking:\n\n" +
                                            "\"What specific support would help you meet this deadline?\"\n\n" +
                                            "This shows empathy and moves toward solutions."
                                )
                            }
                            ActionCommand.HOW_AM_I_DOING -> {
                                viewModel.addAIResponse(
                                    "📊 Your performance so far:\n\n" +
                                            "🟢 Empathy: 85%\n" +
                                            "🟡 Listening: 68%\n" +
                                            "🟢 Questions: 82%\n\n" +
                                            "You're doing great! Keep up the active listening."
                                )
                            }
                        }
                        showQuickActions = false
                    },
                    onDismiss = { showQuickActions = false }
                )
            }

            // Floating Action Button - Menu
            FloatingActionButton(
                onClick = { showQuickActions = true },
                containerColor = Color.White,
                contentColor = Color(0xFF3B82F6),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    hoveredElevation = 10.dp
                ),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Quick Actions",
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(28.dp)
                )
            }
        } else {
            // ============================================================
            // IDLE STATE - Welcome Screen
            // ============================================================
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Ready for Your Virtual Meeting",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Get real-time coaching during\nZoom, Teams, or Google Meet calls",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { showSessionModeModal = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    )
                ) {
                    Text(
                        text = "🎙️ Start Recording",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Quick tips
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TipRow("💬", "Coaching appears inline naturally")
                    TipRow("☰", "Tap menu for quick actions")
                    TipRow("📋", "Copy suggestions to your chat")
                }
            }
        }

        // Session Mode Modal - ✅ UPDATED TO PASS PERMISSION
        if (showSessionModeModal) {
            SessionModeModal(
                onModeSelected = { mode ->
                    viewModel.startSession(mode, hasRecordAudioPermission)  // ✅ PASS PERMISSION HERE
                    showSessionModeModal = false
                },
                onDismiss = { showSessionModeModal = false }
            )
        }
    }
}

/**
 * Minimal Status Bar
 */
@Composable
private fun MinimalStatusBar(
    duration: String,
    onStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .drawBehind {
                        drawCircle(color = Color(0xFFEF4444))
                    }
            )

            Text(
                text = "Recording",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )

            Text(
                text = duration,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        }

        Button(
            onClick = onStop,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444)
            ),
            shape = CircleShape
        ) {
            Text(
                text = "Stop",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Tip row for idle screen
 */
@Composable
private fun TipRow(emoji: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp
        )

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
    }
}