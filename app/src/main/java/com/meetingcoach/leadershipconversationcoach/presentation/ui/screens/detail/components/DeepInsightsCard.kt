package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun DeepInsightsCard(
    commitments: List<String>,
    openQuestions: Int,
    closedQuestions: Int,
    talkRatio: Int,
    interruptions: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "📊 Deep Insights",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppPalette.Stone900
        )

        // Question Analysis
        InsightRow(
            icon = Icons.Default.QuestionAnswer,
            label = "Question Quality",
            value = if (openQuestions + closedQuestions > 0) {
                val ratio = (openQuestions.toFloat() / (openQuestions + closedQuestions) * 100).toInt()
                "$ratio% Open-Ended ($openQuestions/${ openQuestions + closedQuestions})"
            } else "No questions asked",
            color = if (openQuestions > closedQuestions) AppPalette.Sage600 else AppPalette.Stone500
        )

        // Talk Ratio
        InsightRow(
            icon = Icons.Default.RecordVoiceOver,
            label = "Talk Balance",
            value = "${100 - talkRatio}% You / $talkRatio% Them",
            color = when {
                talkRatio in 60..70 -> AppPalette.Sage600
                talkRatio in 40..80 -> AppPalette.Stone500
                else -> AppPalette.Stone400
            }
        )

        // Interruptions
        InsightRow(
            icon = Icons.Default.PanTool,
            label = "Interruptions",
            value = when {
                interruptions == 0 -> "None detected ✓"
                interruptions <= 2 -> "$interruptions (Minimal)"
                else -> "$interruptions (Consider pausing more)"
            },
            color = when {
                interruptions == 0 -> AppPalette.Sage600
                interruptions <= 2 -> AppPalette.Stone500
                else -> AppPalette.Stone400
            }
        )

        // Commitments
        if (commitments.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AppPalette.Sage600,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Action Items (${commitments.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppPalette.Stone700
                    )
                }

                commitments.forEach { commitment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppPalette.Sage100, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = AppPalette.Sage600,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = commitment,
                            fontSize = 13.sp,
                            color = AppPalette.Stone800,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightRow(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AppPalette.Stone700
            )
        }
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}
