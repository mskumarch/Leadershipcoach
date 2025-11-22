package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AIBubbleBackground
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AIBubbleText
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.Secondary

/**
 * AI Response Bubble Component - Modern Lavender Design
 *
 * Purpose: Display AI coach's responses to user questions
 *
 * Visual Style:
 * - Background: Soft lavender tint with subtle gradient
 * - Text color: Dark blue-gray for readability
 * - Corner radius: 20dp (top) and 4dp (bottom-left pointer)
 * - Max width: 80% of screen (slightly wider than user)
 * - Padding: 16dp
 * - Elevation: 3dp with shadow
 * - Alignment: Left with margin
 * - AI Avatar: Lavender circle with robot emoji
 * - Clean, modern coaching feel
 *
 * Example: [AI's answer appears in lavender bubble on left side]
 */
@Composable
fun AIBubble(
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Secondary.copy(alpha = 0.2f),
                            Secondary.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🤖",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 20.dp
                    ),
                    spotColor = Color.Black.copy(alpha = 0.08f)
                )
                .background(
                    color = AIBubbleBackground,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 20.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "AI Coach",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Secondary,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = content,
                    fontSize = 15.sp,
                    color = AIBubbleText,
                    lineHeight = 22.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}