package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Timeline Scrubber Component
 *
 * Purpose: Visual timeline showing key moments in the conversation
 *
 * Visual Style:
 * - Horizontal timeline bar
 * - Markers for key moments:
 *   💡 Nudge appeared
 *   ❓ Question asked
 *   🔴 Tension spike
 *   ⭐ Great moment
 * - Duration labels (start/end)
 * - Clickable markers to jump to that point
 *
 * Example:
 * 00:00 ━━━━💡━━❓━━🔴━━━⭐━━━━ 15:30
 */

data class TimelineMarker(
    val time: String,          // "12:34"
    val timeInSeconds: Int,    // For positioning
    val type: MarkerType,
    val description: String
)

enum class MarkerType {
    NUDGE,           // 💡
    QUESTION,        // ❓
    TENSION_SPIKE,   // 🔴
    GREAT_MOMENT     // ⭐
}

@Composable
fun TimelineScrubber(
    durationSeconds: Int,
    markers: List<TimelineMarker>,
    onMarkerClick: (TimelineMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = "Session Timeline",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            // Timeline visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                // Background line
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.Center)
                ) {
                    drawLine(
                        color = Color(0xFFE5E7EB), // Light gray
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 4.dp.toPx()
                    )
                }

                // Markers
                markers.forEach { marker ->
                    val position = (marker.timeInSeconds.toFloat() / durationSeconds.toFloat())
                        .coerceIn(0f, 1f)

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = (position * (350.dp.value)).dp) // Approximate width
                    ) {
                        TimelineMarkerIcon(
                            marker = marker,
                            onClick = { onMarkerClick(marker) }
                        )
                    }
                }
            }

            // Duration labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "00:00",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )

                Text(
                    text = formatTime(durationSeconds),
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LegendItem("💡", "Nudge")
                LegendItem("❓", "Question")
                LegendItem("🔴", "Tension")
                LegendItem("⭐", "Great")
            }
        }
    }
}

/**
 * Individual marker icon on timeline
 */
@Composable
private fun TimelineMarkerIcon(
    marker: TimelineMarker,
    onClick: () -> Unit
) {
    val emoji = when (marker.type) {
        MarkerType.NUDGE -> "💡"
        MarkerType.QUESTION -> "❓"
        MarkerType.TENSION_SPIKE -> "🔴"
        MarkerType.GREAT_MOMENT -> "⭐"
    }

    val backgroundColor = when (marker.type) {
        MarkerType.NUDGE -> Color(0xFFFEF3C7) // Amber
        MarkerType.QUESTION -> Color(0xFFDCE4FF) // Blue
        MarkerType.TENSION_SPIKE -> Color(0xFFFEE2E2) // Red
        MarkerType.GREAT_MOMENT -> Color(0xFFD1FAE5) // Green
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 16.sp
        )
    }
}

/**
 * Legend item
 */
@Composable
private fun LegendItem(emoji: String, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 12.sp
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
        )
    }
}

/**
 * Format seconds to MM:SS
 */
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}