package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Coaching Banner Component
 *
 * Two types:
 * 1. Critical Nudge (red border) - When something wrong detected
 * 2. Helpful Suggestion (blue border) - Proactive coaching
 *
 * Features:
 * - Copy button for suggestions (virtual meeting use)
 * - Auto-dismiss after timeout
 * - Clear visual distinction
 */

enum class BannerType {
    CRITICAL_NUDGE,      // ⚠️ Red border, urgent
    HELPFUL_SUGGESTION   // 💡 Blue border, helpful
}

@Composable
fun CoachingBanner(
    type: BannerType,
    message: String,
    copyableText: String? = null,
    onDismiss: () -> Unit,
    onGotIt: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    val borderColor = when (type) {
        BannerType.CRITICAL_NUDGE -> Color(0xFFEF4444) // Red
        BannerType.HELPFUL_SUGGESTION -> Color(0xFF3B82F6) // Blue
    }

    val headerIcon = when (type) {
        BannerType.CRITICAL_NUDGE -> "⚠️"
        BannerType.HELPFUL_SUGGESTION -> "💡"
    }

    val headerText = when (type) {
        BannerType.CRITICAL_NUDGE -> "CRITICAL NUDGE"
        BannerType.HELPFUL_SUGGESTION -> "HELPFUL TIP"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Text(
                text = "$headerIcon $headerText",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = borderColor,
                letterSpacing = 0.5.sp
            )

            // Message
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFF1F2937),
                lineHeight = 20.sp
            )

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (type) {
                    BannerType.CRITICAL_NUDGE -> {
                        // Critical: Just "Got it" button
                        Button(
                            onClick = { onGotIt?.invoke() ?: onDismiss() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = borderColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Got it", fontSize = 14.sp)
                        }
                    }

                    BannerType.HELPFUL_SUGGESTION -> {
                        // Helpful: Copy + Dismiss buttons
                        if (copyableText != null) {
                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(copyableText))
                                    onDismiss()
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, borderColor),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = borderColor,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Copy", fontSize = 14.sp, color = borderColor)
                            }
                        }

                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dismiss", fontSize = 14.sp, color = Color(0xFF6B7280))
                        }
                    }
                }
            }
        }
    }
}