package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun TimeRangeSelector(
    selectedRange: String,
    onRangeSelected: (String) -> Unit
) {
    val ranges = listOf("1D", "7D", "30D")
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(8.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.05f))
            .background(Color.White, CircleShape)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ranges.forEach { range ->
            val isSelected = selectedRange == range
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(if (isSelected) AppPalette.Sage600 else Color.Transparent)
                    .clickable { onRangeSelected(range) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range,
                    color = if (isSelected) Color.White else AppPalette.Stone500,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun HeroScoreCard(score: Int) {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient Blob
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (-50).dp, y = (-50).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AppPalette.Sage100.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Overall\nScore",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Stone900,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppPalette.Stone900
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("View Details")
                    }
                }

                // Circular Progress
                Box(contentAlignment = Alignment.Center) {
                    val animatedScore by animateFloatAsState(
                        targetValue = score / 100f,
                        animationSpec = tween(1500)
                    )
                    
                    Canvas(modifier = Modifier.size(140.dp)) {
                        // Background Circle
                        drawArc(
                            color = AppPalette.Stone100,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                        )
                        // Progress Circle
                        drawArc(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    AppPalette.Sage600,
                                    AppPalette.Sage500, // Light Sage
                                    AppPalette.Sage600
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = 360 * animatedScore,
                            useCenter = false,
                            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppPalette.Stone900
                        )
                        Text(
                            text = "/100",
                            fontSize = 12.sp,
                            color = AppPalette.Stone500
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    score: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = modifier.height(140.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon/Ring
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(20.dp)) {
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360 * (score / 100f),
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
            
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = AppPalette.Stone500,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$score%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Stone900
                )
            }
        }
    }
}

@Composable
fun ActivityChartCard(timeRange: String) {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
            Text(
                text = "Sessions per day",
                fontSize = 14.sp,
                color = AppPalette.Stone500
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Mock Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val data = listOf(2, 5, 3, 7, 4, 6, 8) // Mock data
                    val max = data.max().toFloat()
                    
                    data.forEachIndexed { index, value ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            // Bar
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .fillMaxHeight(value / max)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                AppPalette.Sage600,
                                                AppPalette.Sage100
                                            )
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when(index) {
                                    0 -> "M"
                                    1 -> "T"
                                    2 -> "W"
                                    3 -> "T"
                                    4 -> "F"
                                    5 -> "S"
                                    6 -> "S"
                                    else -> ""
                                },
                                fontSize = 12.sp,
                                color = AppPalette.Stone500
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpeakingTimeDistributionCard(userTalkRatio: Int) {
    val othersTalkRatio = 100 - userTalkRatio
    
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Speaking Time",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Bar
                if (userTalkRatio > 0) {
                    Column(modifier = Modifier.weight(userTalkRatio / 100f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                                .background(AppPalette.Sage600)
                        )
                        Text("You ($userTalkRatio%)", style = MaterialTheme.typography.labelSmall, color = AppPalette.Stone500, modifier = Modifier.padding(top = 4.dp))
                    }
                }
                
                // Others Bar
                if (othersTalkRatio > 0) {
                    Column(modifier = Modifier.weight(othersTalkRatio / 100f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                .background(AppPalette.Lavender500)
                        )
                        Text("Others ($othersTalkRatio%)", style = MaterialTheme.typography.labelSmall, color = AppPalette.Stone500, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}


