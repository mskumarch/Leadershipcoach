package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * AI Response Bubble Component
 *
 * Purpose: Display AI coach's responses to user questions
 *
 * Visual Style:
 * - Background: Gray (#F3F4F6)
 * - Text color: Dark gray (#1F2937)
 * - Corner radius: 16dp
 * - Max width: 280dp (slightly wider than user bubble)
 * - Padding: 12dp
 * - Elevation: 2dp
 * - Alignment: Left with 24dp margin
 * - Header: "🤖 AI Response" (11sp, semi-bold, #6B7280)
 *
 * Example: [AI's answer appears in gray bubble on left side]
 */
@Composable
fun AIBubble(
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier
                //.widthIn(max = 280.dp)
                .padding(start = 24.dp, end = 75.dp)

                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.10f),
                    spotColor = Color.Black.copy(alpha = 0.10f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3F4F6) // Gray
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp // Using shadow instead
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header
                Text(
                    text = "🤖 AI Response",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6B7280), // Medium gray
                    letterSpacing = 0.3.sp
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
        }
    }
}