package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.dashboard.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.ProgressViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onAchievementsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTimeRange by remember { mutableStateOf("7D") }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadProgressData()
    }

    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header - Minimal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Growth",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Stone900,
                    letterSpacing = (-1).sp
                )
                
                // Minimal Achievements Icon
                IconButton(onClick = onAchievementsClick) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "Achievements",
                        tint = AppPalette.Sage600
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 1. Overall Score - Large Thin Ring
            Box(contentAlignment = Alignment.Center) {
                // Background Ring
                Canvas(modifier = Modifier.size(200.dp)) {
                    drawCircle(
                        color = AppPalette.Sage100,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                // Progress Ring
                val animatedScore by animateFloatAsState(
                    targetValue = uiState.overallScore / 100f,
                    animationSpec = tween(1500)
                )
                Canvas(modifier = Modifier.size(200.dp)) {
                    drawArc(
                        color = AppPalette.Sage600,
                        startAngle = -90f,
                        sweepAngle = 360 * animatedScore,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${uiState.overallScore}",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Light,
                        color = AppPalette.Stone900,
                        letterSpacing = (-2).sp
                    )
                    Text(
                        text = "OVERALL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Sage600,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // 2. Minimal Metrics (No Cards)
            // 2. Leadership Fingerprint (Radar Chart)
            Text(
                text = "Leadership Style",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            LeadershipRadarChart(
                scores = mapOf(
                    "Empathy" to uiState.empathyScore,
                    "Clarity" to uiState.clarityScore,
                    "Listening" to uiState.listeningScore,
                    "Influence" to uiState.influenceScore,
                    "Pace" to uiState.paceScore
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 3. Stakeholder Heatmap (Dynamics)
            if (uiState.stakeholders.isNotEmpty()) {
                Text(
                    text = "Stakeholder Alignment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Stone900,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                StakeholderHeatmap(
                    stakeholders = uiState.stakeholders
                )
            } else {
                Text(
                    text = "Stakeholder Alignment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Stone900,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No stakeholder data yet. Record a Dynamics session to see alignment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppPalette.Stone500
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // 3. Activity (Minimal Chart)
            Text(
                text = "Activity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // Simple Bar Chart
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Mock data for visual - connect to real data later
                val weeklyActivity = listOf(0.4f, 0.6f, 0.3f, 0.8f, 0.5f, 0.9f, 0.7f)
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                
                weeklyActivity.forEachIndexed { index, value ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .fillMaxHeight(value)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (value > 0.7f) AppPalette.Sage600 else AppPalette.Sage200)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = days[index],
                            style = MaterialTheme.typography.labelSmall,
                            color = AppPalette.Stone500
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun MinimalMetricRow(label: String, score: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(80.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppPalette.Stone700
        )
        
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(AppPalette.Stone100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
        
        Text(
            text = "$score",
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = AppPalette.Stone900
        )
    }
}

// Components moved to DashboardComponents.kt
