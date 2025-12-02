package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

data class Milestone(
    val date: String,
    val title: String,
    val description: String,
    val isAchieved: Boolean
)

@Composable
fun GrowthTimeline(
    milestones: List<Milestone>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "GROWTH JOURNEY",
            style = MaterialTheme.typography.labelSmall,
            color = AppPalette.Stone500,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        milestones.forEachIndexed { index, milestone ->
            TimelineItem(
                milestone = milestone,
                isLast = index == milestones.lastIndex
            )
        }
    }
}

@Composable
fun TimelineItem(
    milestone: Milestone,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline Line & Dot
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (milestone.isAchieved) AppPalette.Sage500 else AppPalette.Stone200,
                        shape = CircleShape
                    )
                    .then(
                        if (milestone.isAchieved) {
                            Modifier.border(2.dp, AppPalette.Sage100, CircleShape)
                        } else Modifier
                    )
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp) // Fixed height for simplicity
                        .background(AppPalette.Stone200)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = milestone.date,
                style = MaterialTheme.typography.labelSmall,
                color = AppPalette.Stone500
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (milestone.isAchieved) AppPalette.Stone900 else AppPalette.Stone500,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = milestone.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppPalette.Stone500
            )
        }
    }
}
