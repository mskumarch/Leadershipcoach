package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
    onStartSession: () -> Unit
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    
    GradientBackground {
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
                        color = AppPalette.Stone500
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
                
                // Quick Tips Carousel (Auto-Scrolling Marquee)
                val scrollState = androidx.compose.foundation.lazy.rememberLazyListState()
                
                // Auto-scroll logic
                LaunchedEffect(Unit) {
                    while (true) {
                        scrollState.animateScrollBy(
                            value = 1000f, // Scroll distance
                            animationSpec = tween(10000, easing = LinearEasing) // Slow, linear speed
                        )
                        // Reset if needed or infinite list logic (simplified here by just scrolling)
                        // For true infinite, we'd need a huge list or custom layout. 
                        // For now, let's just make it a long list by repeating items.
                    }
                }

                androidx.compose.foundation.lazy.LazyRow(
                    state = scrollState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    userScrollEnabled = true // Allow user to override
                ) {
                    // Repeat items to simulate infinite scroll
                    items(100) { index ->
                        val tips = listOf(
                            "⚡ Real-time Nudges",
                            "📊 Post-Session Analytics",
                            "🎭 Roleplay Practice",
                            "🎯 Goal Tracking",
                            "🧠 AI Wisdom"
                        )
                        QuickTipChip(tips[index % tips.size])
                    }
                }
                
                // Daily Tip Teaser
                DailyTipTeaser(
                    tip = "When delegating, define the 'What' and 'When,' but let them define the 'How'.",
                    onClick = { /* TODO: Navigate to Wisdom Tab */ }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AppPalette.Stone200.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💡", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "DAILY TIP",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppPalette.Sage600,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppPalette.Stone900,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}
