package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Transcript Item Component
 *
 * Purpose: Display individual utterance in conversation transcript
 *
 * Visual Style:
 * - Timestamp on left (MM:SS)
 * - Speaker name with color indicator
 * - Emotion/tone icon
 * - Text content
 * - Light card background
 *
 * Example:
 * 12:34  👤 John (😊 Calm)
 *        "I think we should consider the budget constraints..."
 */
@Composable
fun TranscriptItem(
    timestamp: String,
    speakerName: String,
    speakerColor: Color,
    emotion: String,
    emotionEmoji: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Timestamp
            Text(
                text = timestamp,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7280), // Gray
                modifier = Modifier.width(48.dp)
            )

            // Content column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Speaker info row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Color indicator dot
                    Spacer(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(speakerColor)
                    )

                    // Speaker name
                    Text(
                        text = speakerName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )

                    // Emotion indicator
                    Text(
                        text = emotionEmoji,
                        fontSize = 14.sp
                    )

                    Text(
                        text = emotion,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // Transcript text
                Text(
                    text = content,
                    fontSize = 14.sp,
                    color = Color(0xFF374151),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * Get speaker color based on name/index
 */
fun getSpeakerColor(speakerIndex: Int): Color {
    val colors = listOf(
        Color(0xFF3B82F6), // Blue
        Color(0xFF10B981), // Green
        Color(0xFF8B5CF6), // Purple
        Color(0xFFF59E0B), // Amber
        Color(0xFFEF4444), // Red
        Color(0xFF06B6D4), // Cyan
    )
    return colors[speakerIndex % colors.size]
}

/**
 * Get emotion emoji based on emotion string
 */
fun getEmotionEmoji(emotion: String): String {
    return when (emotion.lowercase()) {
        "calm" -> "😊"
        "assertive" -> "💪"
        "defensive" -> "🛡️"
        "frustrated" -> "😤"
        "curious" -> "🤔"
        "neutral" -> "😐"
        "uncertain" -> "😕"
        "anxious" -> "😰"
        "supportive" -> "🤝"
        "stressed" -> "😫"
        else -> "😐"
    }
}