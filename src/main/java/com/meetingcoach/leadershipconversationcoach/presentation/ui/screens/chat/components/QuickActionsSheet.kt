package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Action Commands for Quick Actions
 */
enum class ActionCommand {
    SUMMARIZE_LAST_10_MIN,
    EXPLAIN_RESPONSE,
    CHECK_TONE,
    WHAT_DID_I_MISS,
    SUGGEST_NEXT_QUESTION,
    HOW_AM_I_DOING
}

/**
 * Quick Actions Bottom Sheet
 *
 * Two sections:
 * 1. AI Context Questions - Smart suggestions based on conversation
 * 2. Quick Actions - Pre-built commands (summarize, explain, etc.)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionsSheet(
    suggestedQuestions: List<String>,
    onQuestionSelected: (String) -> Unit,
    onActionSelected: (ActionCommand) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💡 Quick Actions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF6B7280)
                    )
                }
            }

            Divider(color = Color(0xFFE5E7EB))

            // Section 1: AI Suggested Questions
            if (suggestedQuestions.isNotEmpty()) {
                SectionHeader("💬 AI Suggested Questions")

                suggestedQuestions.forEach { question ->
                    ActionItem(
                        icon = "💬",
                        text = question,
                        onClick = {
                            onQuestionSelected(question)
                            onDismiss()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color(0xFFE5E7EB))
            }

            // Section 2: Quick Actions
            SectionHeader("🎯 Quick Actions")

            ActionItem(
                icon = "📝",
                text = "Summarize last 10 minutes",
                onClick = {
                    onActionSelected(ActionCommand.SUMMARIZE_LAST_10_MIN)
                    onDismiss()
                }
            )

            ActionItem(
                icon = "💭",
                text = "Explain their last response",
                onClick = {
                    onActionSelected(ActionCommand.EXPLAIN_RESPONSE)
                    onDismiss()
                }
            )

            ActionItem(
                icon = "🎤",
                text = "Check my tone",
                onClick = {
                    onActionSelected(ActionCommand.CHECK_TONE)
                    onDismiss()
                }
            )

            ActionItem(
                icon = "❓",
                text = "What did I miss?",
                onClick = {
                    onActionSelected(ActionCommand.WHAT_DID_I_MISS)
                    onDismiss()
                }
            )

            ActionItem(
                icon = "🔮",
                text = "Suggest next question",
                onClick = {
                    onActionSelected(ActionCommand.SUGGEST_NEXT_QUESTION)
                    onDismiss()
                }
            )

            ActionItem(
                icon = "📊",
                text = "How am I doing?",
                onClick = {
                    onActionSelected(ActionCommand.HOW_AM_I_DOING)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel button
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

/**
 * Section header
 */
@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF6B7280),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    )
}

/**
 * Individual action item
 */
@Composable
private fun ActionItem(
    icon: String,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )

            Text(
                text = text,
                fontSize = 15.sp,
                color = Color(0xFF1F2937),
                lineHeight = 22.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Get contextual AI questions based on conversation state
 */
fun getContextualQuestions(
    hasEmotion: Boolean = false,
    longSilence: Boolean = false,
    topicChange: Boolean = false
): List<String> {
    return buildList {
        // Always include these basics
        add("What concerns you most about this?")
        add("How can I support you?")
        add("What would success look like?")

        // Context-aware additions
        if (hasEmotion) {
            add("How are you feeling about this?")
            add("What's most challenging for you?")
        }

        if (longSilence) {
            add("What are your thoughts on this?")
            add("Tell me more about that")
        }

        if (topicChange) {
            add("Should we explore this further?")
            add("What else is important?")
        }
    }.take(5) // Max 5 questions
}