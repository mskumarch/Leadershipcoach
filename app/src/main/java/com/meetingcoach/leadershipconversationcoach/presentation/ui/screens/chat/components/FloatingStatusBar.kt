package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Floating Status Bar Component
 *
 * Purpose: Show real-time session metrics when recording
 *
 * Visual Style:
 * - Position: Top center, detached with 16dp margin
 * - Shape: Rounded pill (24dp corner radius)
 * - Shadow: 8dp elevation with blur
 * - Background: White with subtle transparency
 * - Content: Pulsing red dot, timer, talk ratio, pace, sentiment, stop button
 *
 * Example: 🔴 12:34 • 68% ⚡ Fast 😊 Focused • Stop
 */
@Composable
fun FloatingStatusBar(
    duration: String,
    talkRatio: Int,
    pace: String,
    sentiment: String,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulsing animation for red dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        modifier = modifier
            .padding(top = 16.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pulsing red recording dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(alpha)
                    .background(
                        color = Color(0xFFEF4444), // Red
                        shape = CircleShape
                    )
            )

            // Duration timer
            Text(
                text = duration,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )

            // Bullet separator
            Text(
                text = "•",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF)
            )

            // Talk ratio
            Text(
                text = "$talkRatio%",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3B82F6) // Blue
            )

            // Pace indicator
            Text(
                text = getPaceEmoji(pace),
                fontSize = 13.sp
            )

            Text(
                text = pace,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )

            // Sentiment emoji
            Text(
                text = getSentimentEmoji(sentiment),
                fontSize = 14.sp
            )

            Text(
                text = sentiment,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )

            // Bullet separator
            Text(
                text = "•",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF)
            )

            // Stop button
            TextButton(
                onClick = onStop,
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Stop",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEF4444) // Red
                )
            }
        }
    }
}

/**
 * Get pace emoji based on pace string
 */
private fun getPaceEmoji(pace: String): String {
    return when (pace.lowercase()) {
        "fast" -> "⚡"
        "slow" -> "🐌"
        else -> "➡️"
    }
}

/**
 * Get sentiment emoji based on sentiment string
 */
private fun getSentimentEmoji(sentiment: String): String {
    return when (sentiment.lowercase()) {
        "calm" -> "😊"
        "focused" -> "🎯"
        "stressed" -> "😰"
        "anxious" -> "😟"
        else -> "😐"
    }
}