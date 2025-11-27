package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Urgent Nudge Card Component
 *
 * Purpose: Immediate coaching intervention requiring action
 *
 * Visual Style:
 * - Background: White
 * - Border: 2dp solid red (#EF4444)
 * - 4dp elevation with shadow
 * - 12dp corner radius
 * - Max width: 320dp
 * - Action buttons at bottom (Done/Dismiss)
 *
 * Example: "⚠️ URGENT NUDGE"
 *          "You've interrupted 3 times. Let them finish."
 *          [✓ Done] [⊗ Dismiss]
 */
@Composable
fun UrgentNudgeCard(
    content: String,
    onDone: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.animation.AnimatedVisibility(
        visible = true,
        enter = androidx.compose.animation.slideInVertically(initialOffsetY = { it / 2 }) + androidx.compose.animation.fadeIn(),
        exit = androidx.compose.animation.slideOutVertically(targetOffsetY = { it / 2 }) + androidx.compose.animation.fadeOut()
    ) {
        Card(
            modifier = modifier
                .padding(start = 24.dp, end = 75.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0xFFEF4444).copy(alpha = 0.2f),
                    spotColor = Color(0xFFEF4444).copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(2.dp, Color(0xFFEF4444)), // Red border
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp // Using shadow instead
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Header and Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "URGENT NUDGE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444), // Red
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Content
                    Text(
                        text = content,
                        fontSize = 15.sp,
                        color = Color(0xFF1F2937), // Dark gray
                        lineHeight = 22.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Horizontal Divider
                androidx.compose.material3.HorizontalDivider(
                    color = Color(0xFFEF4444).copy(alpha = 0.15f),
                    thickness = 1.dp
                )

                // Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Done Button
                    OutlinedButton(
                        onClick = onDone,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF10B981)), // Green border
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFECFDF5) // Light green bg
                        )
                    ) {
                        Text(
                            text = "✓ Done",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF059669) // Darker Green
                        )
                    }

                    // Dismiss Button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)), // Light gray border
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFF9FAFB) // Light gray bg
                        )
                    ) {
                        Text(
                            text = "Dismiss",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280) // Gray
                        )
                    }
                }
            }
        }
    }
}