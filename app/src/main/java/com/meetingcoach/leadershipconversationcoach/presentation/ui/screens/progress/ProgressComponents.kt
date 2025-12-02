package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LeadershipRadarChart(
    scores: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val labels = scores.keys.toList()
    val values = scores.values.toList()
    val numPoints = labels.size
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(250.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2 * 0.8f
            val angleStep = (2 * Math.PI / numPoints).toFloat()

            // Draw Web
            for (i in 1..4) {
                val r = radius * (i / 4f)
                drawCircle(
                    color = AppPalette.Stone200,
                    radius = r,
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Draw Spokes
            for (i in 0 until numPoints) {
                val angle = i * angleStep - Math.PI.toFloat() / 2
                val end = Offset(
                    center.x + radius * cos(angle),
                    center.y + radius * sin(angle)
                )
                drawLine(
                    color = AppPalette.Stone200,
                    start = center,
                    end = end,
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw Data Polygon
            val path = Path()
            for (i in 0 until numPoints) {
                val angle = i * angleStep - Math.PI.toFloat() / 2
                val value = values[i] / 100f
                val point = Offset(
                    center.x + radius * value * cos(angle),
                    center.y + radius * value * sin(angle)
                )
                if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
            }
            path.close()

            drawPath(
                path = path,
                color = AppPalette.Sage500.copy(alpha = 0.3f)
            )
            drawPath(
                path = path,
                color = AppPalette.Sage600,
                style = Stroke(width = 2.dp.toPx())
            )
        }
        
        // Labels (Simplified positioning for now)
        // Ideally, we would calculate exact positions based on angles
    }
}

data class StakeholderStatus(val name: String, val score: Int, val role: String)

@Composable
fun StakeholderHeatmap(stakeholders: List<StakeholderStatus>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        stakeholders.forEach { stakeholder ->
            val color = when (stakeholder.role) {
                "Ally" -> Color(0xFF4CAF50) // Green
                "Detractor" -> Color(0xFFE57373) // Red
                else -> Color(0xFFFFB74D) // Orange
            }
            
            Surface(
                color = AppPalette.Stone50,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stakeholder.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppPalette.Stone900
                            )
                            Text(
                                text = stakeholder.role,
                                style = MaterialTheme.typography.labelSmall,
                                color = AppPalette.Stone500
                            )
                        }
                    }
                    
                    Text(
                        text = "${stakeholder.score}% Alignment",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
        }
    }
}
