package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Context Card Component
 *
 * Purpose: Display what the AI heard or observed during the conversation
 *
 * Visual Style:
 * - Background: Light gray (#F3F4F6)
 * - No border
 * - 1dp elevation
 * - 12dp corner radius
 * - Max width: 320dp
 * - Left-aligned with 24dp margin
 */
@Composable
fun ContextCard(
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
           // .widthIn(max = 320.dp)
            .padding(start = 24.dp, end = 75.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F4F6) // Light gray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Text(
                text = "📍 CONTEXT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280), // Medium gray
                letterSpacing = 0.5.sp
            )

            // Content
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF374151), // Dark gray
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}