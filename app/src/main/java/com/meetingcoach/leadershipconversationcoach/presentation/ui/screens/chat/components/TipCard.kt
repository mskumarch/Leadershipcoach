package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Helpful Tip Card Component
 *
 * Purpose: Optional suggestions to improve communication
 *
 * Visual Style:
 * - Background: White
 * - Border: 2dp solid blue (#3B82F6)
 * - 2dp elevation
 * - 12dp corner radius
 * - Max width: 320dp
 * - Optional dismiss button (small, top-right corner)
 *
 * Example: "✨ HELPFUL TIP"
 *          "Try paraphrasing what they said to show understanding."
 *          [X] (dismiss button)
 */
@Composable
fun TipCard(
    content: String,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .widthIn(max = 320.dp)
            .padding(start = 24.dp, end = 48.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, Color(0xFF3B82F6)), // Blue border
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // Using shadow instead
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(end = if (onDismiss != null) 24.dp else 0.dp), // Extra padding if dismiss button present
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header
                Text(
                    text = "✨ HELPFUL TIP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B82F6), // Blue
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

            // Optional Dismiss Button (Top-Right Corner)
            if (onDismiss != null) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss tip",
                        tint = Color(0xFF6B7280), // Gray
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}