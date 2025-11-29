package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlassCard
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlassDesign

@Composable
fun HomeIdleState(
    onStartSession: () -> Unit
) {
    // Main Background
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp), // Status bar spacing
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Header
            HeaderSection()

            // 2. Main Metric Card (Readiness/Impact)
            MainMetricCard()

            // 3. Quick Actions Row
            QuickActionsRow(onStartSession = onStartSession)

            // 4. Daily Insight Card
            DailyInsightCard()
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good Morning, Leader!",
                style = MaterialTheme.typography.titleLarge,
                color = AppPalette.Sage900,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Friday, October 31", // TODO: Dynamic Date
                style = MaterialTheme.typography.bodyMedium,
                color = AppPalette.Sage700
            )
        }
        
        // Profile / Weather Pill
        GlassCard(
            shape = RoundedCornerShape(50),
            alpha = 0.5f,
            modifier = Modifier.height(40.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🌤️ +10°", style = MaterialTheme.typography.labelMedium, color = AppPalette.Sage900)
            }
        }
    }
}

@Composable
private fun MainMetricCard() {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekly Impact",
                    style = MaterialTheme.typography.titleMedium,
                    color = AppPalette.Sage900,
                    fontWeight = FontWeight.SemiBold
                )
                
                Icon(
                    imageVector = Icons.Default.School, // Placeholder icon
                    contentDescription = null,
                    tint = AppPalette.Sage700,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Circular Progress
            Box(contentAlignment = Alignment.Center) {
                // Background Circle
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawCircle(
                        color = AppPalette.Sage100,
                        style = Stroke(width = 25.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                // Progress Circle (Gradient)
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(AppPalette.Sage400, AppPalette.Lavender500, AppPalette.Sage400)
                        ),
                        startAngle = -90f,
                        sweepAngle = 280f, // 80% roughly
                        useCenter = false,
                        style = Stroke(width = 25.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Good",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppPalette.Sage500
                    )
                    Text(
                        text = "85%",
                        style = MaterialTheme.typography.displayMedium,
                        color = AppPalette.Sage900,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Better than 73% of users",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppPalette.Sage500,
                        modifier = Modifier.width(80.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // View Report Button
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppPalette.Sage700
                ),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("View Full Report")
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onStartSession: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Start Session
        QuickActionCard(
            modifier = Modifier.weight(1f),
            title = "Start\nSession",
            icon = "🎙️",
            gradient = GlassDesign.ActiveGradient,
            onClick = onStartSession
        )
        
        // 2. Practice
        QuickActionCard(
            modifier = Modifier.weight(1f),
            title = "Practice\nScenario",
            icon = "🎭",
            gradient = GlassDesign.GlassCardGradient, // Subtle
            onClick = { /* TODO */ }
        )
        
        // 3. Ask Coach
        QuickActionCard(
            modifier = Modifier.weight(1f),
            title = "Ask\nCoach",
            icon = "💬",
            gradient = GlassDesign.LavenderGradient,
            onClick = { /* TODO */ }
        )
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient.copy(alpha = 0.1f)) // Subtle tint
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 20.sp)
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = AppPalette.Sage900,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
private fun DailyInsightCard() {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(AppPalette.Lavender100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✨", fontSize = 24.sp)
            }
            
            Column {
                Text(
                    text = "AI Insight",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppPalette.Lavender500,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "You asked 20% more open questions this week. Great job!",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppPalette.Sage900,
                    maxLines = 2
                )
            }
        }
    }
}
