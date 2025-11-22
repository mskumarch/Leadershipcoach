package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Progress Rings Component
 *
 * Purpose: Apple Watch-style circular progress indicators for coaching metrics
 *
 * Three concentric rings:
 * 1. Empathy Ring (Green) - Empathetic language detected
 * 2. Listening Ring (Blue) - Talk/listen ratio balance
 * 3. Clarity Ring (Purple) - Clear, open questions asked
 *
 * Visual Style:
 * - Concentric circles
 * - Animated fill (smooth transitions)
 * - Color-coded: Green when good, Yellow when needs work
 * - Percentage labels
 * - Goal indicators
 *
 * Example: [Three rings with different fill levels]
 */
@Composable
fun ProgressRings(
    empathyScore: Int,      // 0-100
    listeningScore: Int,    // 0-100
    clarityScore: Int,      // 0-100
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Concentric Rings Display
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Ring - Empathy (Green)
            ProgressRing(
                progress = empathyScore,
                color = Color(0xFF10B981), // Green
                strokeWidth = 14.dp,
                size = 240.dp
            )

            // Middle Ring - Listening (Blue)
            ProgressRing(
                progress = listeningScore,
                color = Color(0xFF3B82F6), // Blue
                strokeWidth = 14.dp,
                size = 200.dp
            )

            // Inner Ring - Clarity (Purple)
            ProgressRing(
                progress = clarityScore,
                color = Color(0xFF8B5CF6), // Purple
                strokeWidth = 14.dp,
                size = 160.dp
            )

            // Center text - Overall performance
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val averageScore = (empathyScore + listeningScore + clarityScore) / 3

                Text(
                    text = "$averageScore%",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Overall",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        // Legend with individual scores
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RingLegendItem(
                color = Color(0xFF10B981),
                label = "Empathy",
                score = empathyScore,
                goal = 70,
                description = "Empathetic language"
            )

            RingLegendItem(
                color = Color(0xFF3B82F6),
                label = "Listening",
                score = listeningScore,
                goal = 60,
                description = "Balanced talk ratio"
            )

            RingLegendItem(
                color = Color(0xFF8B5CF6),
                label = "Clarity",
                score = clarityScore,
                goal = 50,
                description = "Open questions"
            )
        }
    }
}

/**
 * Individual Progress Ring
 */
@Composable
private fun ProgressRing(
    progress: Int,
    color: Color,
    strokeWidth: Dp,  // ← Fixed: Changed from 'dp' to 'Dp'
    size: Dp          // ← Fixed: Changed from 'dp' to 'Dp'
) {
    // Clamp progress between 0 and 100
    val clampedProgress = progress.coerceIn(0, 100)

    // Animate the ring fill
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "ring_progress"
    )

    Canvas(modifier = Modifier.size(size)) {
        // Background ring (light gray)
        drawArc(
            color = Color(0xFFE5E7EB),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // Foreground arc (colored based on progress)
        drawArc(
            color = color,
            startAngle = -90f, // Start at top
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

/**
 * Legend item for each ring
 */
@Composable
private fun RingLegendItem(
    color: Color,
    label: String,
    score: Int,
    goal: Int,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color indicator circle
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .padding(2.dp)
            ) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color = color)
                }
            }

            // Label and description
            Column {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        // Score and goal
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$score%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (score >= goal) Color(0xFF10B981) else Color(0xFF6B7280)
            )

            Text(
                text = "Goal: $goal%",
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}