package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun GrowStageIndicator(
    currentStage: String,
    modifier: Modifier = Modifier
) {
    val stages = listOf("GOAL", "REALITY", "OPTIONS", "WAY_FORWARD")
    val currentIndex = stages.indexOf(currentStage).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stages.forEachIndexed { index, stage ->
            val isActive = index == currentIndex
            val isPast = index < currentIndex
            
            Box(
                modifier = Modifier
                    .background(
                        color = when {
                            isActive -> AppPalette.Sage600
                            isPast -> AppPalette.Sage500
                            else -> AppPalette.Stone300
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stage.replace("_", " "),
                    fontSize = 11.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive || isPast) Color.White else AppPalette.Stone700
                )
            }
        }
    }
}
