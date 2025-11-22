package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
 * User Question Bubble Component
 *
 * Purpose: Display user's questions to the AI coach
 *
 * Visual Style:
 * - Background: Blue (#3B82F6)
 * - Text color: White
 * - Corner radius: 16dp
 * - Max width: 260dp
 * - Padding: 12dp
 * - Elevation: 2dp
 * - Alignment: Right with 24dp margin
 * - No speaker label needed (it's obviously the user)
 *
 * Example: [User's question appears in blue bubble on right side]
 */
@Composable
fun UserBubble(
    content: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Card(
            modifier = Modifier
                //.widthIn(max = 260.dp)
                //.padding(end = 24.dp, start = 48.dp)
                .padding(start = 5.dp, end = 24.dp)

                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.10f),
                    spotColor = Color.Black.copy(alpha = 0.10f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3B82F6) // Blue
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp // Using shadow instead
            )
        ) {
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}