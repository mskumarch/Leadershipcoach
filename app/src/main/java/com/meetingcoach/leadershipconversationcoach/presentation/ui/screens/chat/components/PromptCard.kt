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
 * Important Prompt Card Component
 *
 * Purpose: Reflective coaching questions to encourage self-awareness
 *
 * Visual Style:
 * - Background: Amber (#FEF3C7)
 * - No border
 * - 2dp elevation
 * - 12dp corner radius
 * - Max width: 320dp
 * - No action buttons (encourages reflection)
 *
 * Example: "💡 IMPORTANT PROMPT"
 *          "What would help this person feel heard right now?"
 */
@Composable
fun PromptCard(
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            // .widthIn(max = 320.dp)
            .padding(start = 24.dp, end = 75.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEF3C7) // Amber
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // Using shadow instead
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
                text = "💡 IMPORTANT PROMPT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD97706), // Amber orange
                letterSpacing = 0.5.sp
            )

            // Content
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF92400E), // Dark amber brown
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}