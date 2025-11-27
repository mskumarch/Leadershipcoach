package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * TalkRatioGauge
 *
 * Visualizes the percentage of time the user has been speaking.
 * - Green zone: 30-40% (Balanced)
 * - Yellow zone: 20-30% or 40-60%
 * - Red zone: <20% or >60%
 */
@Composable
fun TalkRatioGauge(
    userTalkRatio: Int,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    strokeWidth: Dp = 8.dp
) {
    val animatedRatio by animateFloatAsState(
        targetValue = userTalkRatio / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "TalkRatioAnimation"
    )

    val color = when {
        userTalkRatio in 30..40 -> Color(0xFF10B981) // Green
        userTalkRatio in 20..60 -> Color(0xFFF59E0B) // Yellow
        else -> Color(0xFFEF4444) // Red
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            // Background Circle (Others)
            drawArc(
                color = Color(0xFFE5E7EB),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )

            // Foreground Arc (User)
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = animatedRatio * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${userTalkRatio}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "You",
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
