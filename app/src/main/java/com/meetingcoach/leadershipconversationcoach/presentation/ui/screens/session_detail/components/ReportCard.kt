package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.session_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun ReportCard(
    score1: Int,
    score2: Int,
    score3: Int,
    summary: String,
    pace: String,
    wording: String,
    improvements: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppPalette.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text(
                text = "Session Report Card",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )

            // Scores Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreCircle(score = score1, label = "Empathy")
                ScoreCircle(score = score2, label = "Clarity")
                ScoreCircle(score = score3, label = "Listening")
            }

            HorizontalDivider(color = AppPalette.Stone100)

            // Summary Section
            Section(title = "Summary", content = summary)

            // Analysis Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnalysisBox(
                    title = "Pace",
                    content = pace,
                    modifier = Modifier.weight(1f),
                    color = AppPalette.Sage100
                )
                AnalysisBox(
                    title = "Wording",
                    content = wording,
                    modifier = Modifier.weight(1f),
                    color = AppPalette.Lavender100
                )
            }

            // Improvements Section
            Section(
                title = "Improvements",
                content = improvements,
                isList = true
            )
        }
    }
}

@Composable
private fun ScoreCircle(score: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = getScoreColor(score).copy(alpha = 0.1f),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        ) {
            Text(
                text = "$score",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = getScoreColor(score)
            )
        }
        Text(
            text = label,
            fontSize = 14.sp,
            color = AppPalette.Stone500
        )
    }
}

@Composable
private fun Section(title: String, content: String, isList: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppPalette.Stone900
        )
        if (isList) {
            val items = content.split("|", "\n").filter { it.isNotBlank() }
            items.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("• ", color = AppPalette.Sage600)
                    Text(
                        text = item.trim(),
                        fontSize = 14.sp,
                        color = AppPalette.Stone700,
                        lineHeight = 20.sp
                    )
                }
            }
        } else {
            Text(
                text = content,
                fontSize = 14.sp,
                color = AppPalette.Stone700,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun AnalysisBox(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Column(
        modifier = modifier
            .background(color, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppPalette.Stone900
        )
        Text(
            text = content,
            fontSize = 13.sp,
            color = AppPalette.Stone700
        )
    }
}

private fun getScoreColor(score: Int): Color {
    return when {
        score >= 80 -> AppPalette.Sage600
        score >= 60 -> AppPalette.Amber500
        else -> AppPalette.Red500
    }
}
