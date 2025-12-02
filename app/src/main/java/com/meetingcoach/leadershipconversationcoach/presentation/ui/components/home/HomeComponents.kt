package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PulsingConcentricCircles
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.GradientBackground

@Composable
fun HomeIdleState(
    dailyTip: String,
    recentTags: List<String> = emptyList(),
    onStartSession: () -> Unit,
    onTagSelected: (String) -> Unit = {}
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good Morning,",
                        style = MaterialTheme.typography.titleMedium,
                        color = AppPalette.Stone500
                    )
                    Text(
                        text = "Leadership Coach",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Stone900
                    )
                }
                
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(AppPalette.Stone100, RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 20.sp)
                }
            }

            // Main Content Scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp, bottom = 80.dp) // Space for header and bottom nav
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Performance Pulse Widget
                PerformancePulseWidget()

                // 2. The Orb (Start Session)
                Box(contentAlignment = Alignment.Center) {
                    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StartSessionOrb(
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onStartSession()
                        }
                    )
                }
                
                // 3. Smart Prep Card (Prominent)
                if (recentTags.isNotEmpty()) {
                    SmartPrepCard(recentTags, onTagSelected)
                }

                // 4. Quick Actions
                QuickActionsRow()
                
                // 5. Daily Tip (Footer)
                DailyTipTeaser(
                    tip = dailyTip,
                    onClick = { /* TODO: Navigate to Wisdom Tab */ }
                )
            }
        }
    }
}

@Composable
fun PerformancePulseWidget() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Weekly Pulse",
                    style = MaterialTheme.typography.labelMedium,
                    color = AppPalette.Stone500,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "+15% Empathy",
                    style = MaterialTheme.typography.titleLarge,
                    color = AppPalette.Sage500,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "vs. last week",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppPalette.Stone500
                )
            }
            
            // Mini Trend Graph (Mock)
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp)
                    .background(AppPalette.Sage100.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Simple Path drawing for trend
                Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, size.height)
                        cubicTo(
                            size.width * 0.3f, size.height * 0.8f,
                            size.width * 0.6f, size.height * 0.2f,
                            size.width, size.height * 0.4f
                        )
                    }
                    drawPath(
                        path = path,
                        color = AppPalette.Sage500,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                    )
                }
            }
        }
    }
}

@Composable
fun SmartPrepCard(recentTags: List<String>, onTagSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)), // Dark Slate
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🚀", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Smart Prep",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Prepare for your next meeting based on recent context.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppPalette.Sage200
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Horizontal Scroll for Tags
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentTags.forEach { tag ->
                    SmartPrepChip(tag) { onTagSelected(tag) }
                }
            }
        }
    }
}

@Composable
fun QuickActionsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickActionButton(icon = "🎯", label = "Practice")
        QuickActionButton(icon = "📊", label = "History") // Placeholder action
        QuickActionButton(icon = "⚙️", label = "Settings")
    }
}

@Composable
fun QuickActionButton(icon: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, CircleShape)
                .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = AppPalette.Stone500,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DailyTipTeaser(tip: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("💡", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = tip,
            style = MaterialTheme.typography.bodyMedium,
            color = AppPalette.Stone500,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}
