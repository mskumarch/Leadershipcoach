package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.*
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
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
    hasRecordAudioPermission: Boolean = true
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
            .background(SageGreen) // Sage Green explicitly
    ) {
        if (sessionState.isRecording) {
            // ============================================================
            // RECORDING STATE - Minimal View with Inline Nudges
            // ============================================================
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Glassmorphic Header
                GlassmorphicFloatingPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Pulsing recording dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MutedCoral, CircleShape)
                            )

                            Text(
                                text = "Recording",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DeepCharcoal
                            )

                            Text(
                                text = sessionState.duration,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepCharcoal
                            )
                        }

                        Button(
                            onClick = { viewModel.stopSession() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MutedCoral
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = "Stop",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
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
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            sessionState.messages.forEach { message ->
                                when (message.type) {
                                    // ✅ Coaching cards - map to BannerType
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

                                    // ✅ User question bubble
                                    MessageType.USER_QUESTION -> {
                                        UserBubble(content = message.content)
                                    }

                                    // ✅ AI response bubble
                                    MessageType.AI_RESPONSE -> {
                                        GlassmorphicAICard {
                                            Text(
                                                text = message.content,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = DeepCharcoal
                                            )
                                        }
                                    }

                                    MessageType.TRANSCRIPT -> { /* Shown in Transcript tab */ }
                                    MessageType.INSTRUCTION -> { /* Optional instruction */ }
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
                            FloatingEmptyState(
                                icon = "💬",
                                title = "Focus on conversation",
                                subtitle = "I'll coach you inline as we go"
                            )
                        }
                    }
                }

                // Input Area
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = GlassWhite,
                    shadowElevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(bottom = 80.dp) // Space for bottom nav
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
                        // Dynamic responses based on actual context (simulated)
                        val response = when (command) {
                            ActionCommand.SUMMARIZE_LAST_10_MIN -> 
                                "📝 Summary of recent conversation:\n\n" +
                                "• Key topics discussed: Project timeline, resources\n" +
                                "• You asked clarifying questions about scope\n" +
                                "• Sentiment is generally positive but cautious\n" +
                                "• Action item: Review budget proposal"
                            
                            ActionCommand.EXPLAIN_RESPONSE -> 
                                "💭 Analysis of last response:\n\n" +
                                "The speaker seems hesitant. They might need reassurance about the deadline. " +
                                "Try asking: 'What support do you need to feel confident?'"
                            
                            ActionCommand.CHECK_TONE -> 
                                "🎤 Your tone check:\n\n" +
                                "✅ Calm and steady pace\n" +
                                "✅ Good use of open-ended questions\n" +
                                "💡 Remember to pause after asking to let them think"
                            
                            ActionCommand.WHAT_DID_I_MISS -> 
                                "❗ Potential missed cues:\n\n" +
                                "• They hesitated when mentioning the budget\n" +
                                "• Repeated use of 'maybe' suggests uncertainty"
                            
                            ActionCommand.SUGGEST_NEXT_QUESTION -> 
                                "🔮 Suggested follow-up:\n\n" +
                                "\"How does this align with your team's capacity right now?\""
                            
                            ActionCommand.HOW_AM_I_DOING -> 
                                "📊 Session Performance:\n\n" +
                                "🟢 Empathy: High\n" +
                                "🟢 Listening: Good\n" +
                                "🟡 Clarity: Could be more concise"
                        }
                        viewModel.addAIResponse(response)
                        showQuickActions = false
                    },
                    onDismiss = { showQuickActions = false }
                )
            }

            // Floating Action Button - Menu
            FloatingActionButton(
                onClick = { showQuickActions = true },
                containerColor = SoftCream,
                contentColor = ActiveBlue,
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
            // ============================================================
            // IDLE STATE - Welcome Screen (Premium Design)
            // ============================================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FloatingEmptyState(
                    icon = "🎯",
                    title = "Ready for Your Meeting",
                    subtitle = "Get real-time coaching during your calls"
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { showSessionModeModal = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActiveBlue
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(56.dp)
                        .shadow(8.dp, RoundedCornerShape(50), spotColor = ActiveBlue.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "🎙️ Start Recording",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Quick tips in glass card
                SettingsCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TipRow("💬", "Coaching appears inline naturally")
                        TipRow("☰", "Tap menu for quick actions")
                        TipRow("📋", "Copy suggestions to your chat")
                    }
                }
            }
        }

        // Session Mode Modal
        if (showSessionModeModal) {
            SessionModeModal(
                onModeSelected = { mode ->
                    viewModel.startSession(mode, hasRecordAudioPermission)
                    showSessionModeModal = false
                },
                onDismiss = { showSessionModeModal = false }
            )
        }
    }
}

@Composable
private fun TipRow(emoji: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )

        Text(
            text = text,
            fontSize = 15.sp,
            color = NeutralGray,
            fontWeight = FontWeight.Medium
        )
    }
}