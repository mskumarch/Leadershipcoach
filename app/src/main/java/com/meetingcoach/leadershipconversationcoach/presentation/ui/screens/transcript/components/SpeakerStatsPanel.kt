package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Speaker Stats Panel Component
 *
 * Purpose: Collapsible panel showing statistics for each speaker
 *
 * Features:
 * - Talk time per person (horizontal bars)
 * - Speaking pace (words per minute)
 * - Interruption count
 * - Sentiment timeline graph (placeholder)
 * - Expandable/collapsible
 *
 * Example:
 * 📊 Speaker Statistics [▼]
 *   Sarah: ████████ 45% | 120 wpm | 2 interruptions
 *   John:  ████ 25% | 95 wpm | 0 interruptions
 *   You:   ██████ 30% | 110 wpm | 1 interruption
 */

data class SpeakerStats(
    val name: String,
    val color: Color,
    val talkTimePercentage: Int,  // 0-100
    val wordsPerMinute: Int,
    val interruptionCount: Int,
    val averageSentiment: String  // "Calm", "Neutral", "Stressed"
)

@Composable
fun SpeakerStatsPanel(
    speakers: List<SpeakerStats>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

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
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header (clickable to expand/collapse)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📊",
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Speaker Statistics",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF6B7280)
                )
            }

            // Stats content (expandable)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(color = Color(0xFFE5E7EB))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        speakers.forEach { speaker ->
                            SpeakerStatItem(speaker = speaker)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual speaker stat row
 */
@Composable
private fun SpeakerStatItem(speaker: SpeakerStats) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Name and color indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(speaker.color)
                )

                Text(
                    text = speaker.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Text(
                text = "${speaker.talkTimePercentage}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = speaker.color
            )
        }

        // Talk time progress bar
        LinearProgressIndicator(
            progress = speaker.talkTimePercentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = speaker.color,
            trackColor = Color(0xFFE5E7EB)
        )

        // Additional stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatChip(
                label = "Pace",
                value = "${speaker.wordsPerMinute} wpm"
            )

            StatChip(
                label = "Interruptions",
                value = speaker.interruptionCount.toString()
            )

            StatChip(
                label = "Tone",
                value = speaker.averageSentiment
            )
        }
    }
}

/**
 * Small stat chip
 */
@Composable
private fun StatChip(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF9CA3AF)
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151)
        )
    }
}