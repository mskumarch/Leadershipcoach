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
 * Transcript Item Component - Modern Design
 *
 * Purpose: Display individual utterance in conversation transcript
 *
 * Visual Style:
 * - Clean card with subtle gradient background
 * - Timestamp on left (MM:SS) with sea green tint
 * - Speaker name with vibrant color indicator dot
 * - Emotion/tone icon with lavender accent
 * - Text content with excellent readability
 * - Soft shadow for depth
 * - Rounded corners for modern feel
 *
 * Example:
 * 12:34  • John 😊 Calm
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
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = timestamp,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4DB6AC),
                modifier = Modifier.width(52.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(speakerColor)
                    )

                    Text(
                        text = speakerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Text(
                        text = emotionEmoji,
                        fontSize = 16.sp
                    )

                    Text(
                        text = emotion,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9575CD)
                    )
                }

                Text(
                    text = content,
                    fontSize = 15.sp,
                    color = Color(0xFF2D3748),
                    lineHeight = 22.sp
                )
            }
        }
    }
}

/**
 * Get speaker color based on name/index - Sea Green & Lavender Theme
 */
fun getSpeakerColor(speakerIndex: Int): Color {
    val colors = listOf(
        Color(0xFF4DB6AC),
        Color(0xFF9575CD),
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFB74D),
        Color(0xFF7986CB),
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