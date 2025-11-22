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
import androidx.compose.material3.Divider
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
    Card(
        modifier = modifier
            // .widthIn(max = 320.dp)
            .padding(start = 24.dp, end = 75.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(12.dp),
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
                Text(
                    text = "⚠️ URGENT NUDGE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444), // Red
                    letterSpacing = 0.5.sp
                )

                // Content
                Text(
                    text = content,
                    fontSize = 14.sp,
                    color = Color(0xFF1F2937), // Dark gray
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Horizontal Divider
            Divider(
                color = Color(0xFFEF4444).copy(alpha = 0.3f), // Red with 30% opacity
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
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF10B981)) // Green border
                ) {
                    Text(
                        text = "✓ Done",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF10B981) // Green
                    )
                }

                // Dismiss Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF6B7280)) // Gray border
                ) {
                    Text(
                        text = "⊗ Dismiss",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280) // Gray
                    )
                }
            }
        }
    }
}