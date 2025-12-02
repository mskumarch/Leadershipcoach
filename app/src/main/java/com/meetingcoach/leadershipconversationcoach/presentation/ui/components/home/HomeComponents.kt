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
            // Top Right Profile (Mock)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .size(40.dp)
                    .background(AppPalette.Stone100, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 20.sp)
            }

            // Center Content
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 60.dp), // Offset for bottom nav
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                // Header
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Good Morning,",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppPalette.Stone500,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Ready to Lead?",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Stone900
                    )
                }

                // The Orb
                com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StartSessionOrb(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onStartSession()
                    }
                )
                
                // Daily Tip Teaser
                DailyTipTeaser(
                    tip = dailyTip,
                    onClick = { /* TODO: Navigate to Wisdom Tab */ }
                )
                
                // Smart Prep Section
                SmartPrepSection(
                    recentTags = recentTags,
                    onTagSelected = onTagSelected
                )
            }
            
            // Bottom Navigation - Handled by NavigationScreen
        }
    }
}

@Composable
fun QuickTipChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
