package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Temperature Gauge Component
 *
 * Purpose: Visual indicator of conversation tension/temperature
 *
 * Visual Style:
 * - Small circular gauge (48dp diameter)
 * - Color-coded ring that fills clockwise:
 *   🟢 Green (0-33%): Calm, productive
 *   🟡 Yellow (34-66%): Getting tense
 *   🔴 Red (67-100%): High stress/tension
 * - Smooth animated transitions
 * - Subtle pulse when yellow/red
 * - Shows temperature value in center
 *
 * Example: [Circular ring with color] 45°
 */
@Composable
fun TemperatureGauge(
    temperature: Int, // 0-100
    modifier: Modifier = Modifier
) {
    // Clamp temperature between 0 and 100
    val clampedTemp = temperature.coerceIn(0, 100)

    // Animate the gauge fill
    val animatedProgress by animateFloatAsState(
        targetValue = clampedTemp / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "temperature_progress"
    )

    // Determine color based on temperature
    val gaugeColor = when {
        clampedTemp <= 33 -> Color(0xFF10B981) // Green - Calm
        clampedTemp <= 66 -> Color(0xFFF59E0B) // Yellow - Tense
        else -> Color(0xFFEF4444) // Red - Stressed
    }

    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background circle (light gray)
        Canvas(modifier = Modifier.size(48.dp)) {
            drawArc(
                color = Color(0xFFE5E7EB), // Light gray
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Foreground arc (colored based on temperature)
        Canvas(modifier = Modifier.size(48.dp)) {
            drawArc(
                color = gaugeColor,
                startAngle = -90f, // Start at top
                sweepAngle = 360f * animatedProgress, // Fill clockwise
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Temperature value in center
        Text(
            text = "$clampedTemp°",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = gaugeColor
        )
    }
}

/**
 * Get temperature status text
 */
@Composable
fun getTemperatureStatus(temperature: Int): String {
    return when {
        temperature <= 33 -> "Calm"
        temperature <= 66 -> "Tense"
        else -> "Stressed"
    }
}

/**
 * Get temperature emoji
 */
fun getTemperatureEmoji(temperature: Int): String {
    return when {
        temperature <= 33 -> "😊" // Calm
        temperature <= 66 -> "😐" // Tense
        else -> "😰" // Stressed
    }
}