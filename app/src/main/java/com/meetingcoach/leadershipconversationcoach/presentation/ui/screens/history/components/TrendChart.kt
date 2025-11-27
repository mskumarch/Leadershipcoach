package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun TrendChart(
    dataPoints: List<Int>,
    title: String,
    modifier: Modifier = Modifier,
    lineColor: Color = AppPalette.Sage600
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppPalette.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (dataPoints.size < 2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Not enough data yet",
                        color = AppPalette.Stone500,
                        fontSize = 14.sp
                    )
                }
            } else {
                ChartCanvas(
                    dataPoints = dataPoints,
                    lineColor = lineColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }
    }
}

@Composable
private fun ChartCanvas(
    dataPoints: List<Int>,
    lineColor: Color,
    modifier: Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacing = width / (dataPoints.size - 1)
        
        // Normalize data to 0-100 range fitting in height
        // Assuming scores are 0-100
        val maxScore = 100f
        val minScore = 0f
        
        val path = Path()
        val points = mutableListOf<Offset>()
        
        dataPoints.forEachIndexed { index, score ->
            val x = index * spacing
            val y = height - (score / maxScore * height)
            points.add(Offset(x, y))
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                // Smooth curve (cubic bezier)
                val prevPoint = points[index - 1]
                val controlPoint1 = Offset(prevPoint.x + spacing / 2, prevPoint.y)
                val controlPoint2 = Offset(x - spacing / 2, y)
                path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, x, y)
            }
        }
        
        // Draw gradient fill
        val fillPath = Path()
        fillPath.addPath(path)
        fillPath.lineTo(width, height)
        fillPath.lineTo(0f, height)
        fillPath.close()
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.2f),
                    lineColor.copy(alpha = 0.0f)
                )
            )
        )
        
        // Draw line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        
        // Draw points
        points.forEach { point ->
            drawCircle(
                color = AppPalette.White,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = point,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}
