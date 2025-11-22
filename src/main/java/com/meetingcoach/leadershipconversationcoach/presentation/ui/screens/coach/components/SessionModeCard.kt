package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Session Mode Card Component
 *
 * Purpose: Display session type options for user to choose from
 *
 * Visual Style:
 * - White background
 * - 4dp elevation
 * - 16dp corner radius
 * - Large emoji icon (48sp)
 * - Mode name (18sp, bold)
 * - Description (14sp, 2-3 lines)
 * - "Start Session" button at bottom
 *
 * Example:
 * 👤
 * One-on-One Coaching
 * Direct report check-in, mentoring, or feedback conversation
 * [Start Session]
 */
@Composable
fun SessionModeCard(
    emoji: String,
    title: String,
    description: String,
    onStartSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // Using shadow instead
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Large emoji icon
            Text(
                text = emoji,
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )

            // Mode name
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937), // Dark gray
                textAlign = TextAlign.Center
            )

            // Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF6B7280), // Medium gray
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Start Session button
            Button(
                onClick = onStartSession,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6) // Blue
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Start Session",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}