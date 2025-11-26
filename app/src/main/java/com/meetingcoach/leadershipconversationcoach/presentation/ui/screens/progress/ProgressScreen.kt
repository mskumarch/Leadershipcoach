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

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier
) {
    var selectedTimeRange by remember { mutableStateOf("7D") }
    val scrollState = rememberScrollState()

    // Mock Data for "WOW" effect
    val overallScore = 85
    val empathyScore = 92
    val clarityScore = 78
    val listeningScore = 88

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC), // Slate 50
                        Color(0xFFF1F5F9)  // Slate 100
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // Header
        Text(
            text = "Your Growth",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppPalette.Slate900,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Track your leadership journey",
            fontSize = 16.sp,
            color = AppPalette.Slate500,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Time Range Selector (Glassmorphic Pill)
        TimeRangeSelector(
            selectedRange = selectedTimeRange,
            onRangeSelected = { selectedTimeRange = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Main Score Card (Hero)
        HeroScoreCard(score = overallScore)

        Spacer(modifier = Modifier.height(24.dp))

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MetricCard(
                title = "Empathy",
                score = empathyScore,
                color = Color(0xFF10B981), // Emerald
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Clarity",
                score = clarityScore,
                color = Color(0xFF3B82F6), // Blue
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Listening",
                score = listeningScore,
                color = Color(0xFF8B5CF6), // Violet
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Activity Chart
        ActivityChartCard(timeRange = selectedTimeRange)
        
        Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
    }
}

// Components moved to DashboardComponents.kt
