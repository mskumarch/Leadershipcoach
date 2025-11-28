package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object PremiumStyles {
    val StandardCardRadius = 22.dp
    val PagePadding = 20.dp
    val SectionSpacing = 24.dp
    
    val TopFadeGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE7F3ED), // Light Sage Fade
            Color(0xFFFFFFFF)  // White
        ),
        startY = 0f,
        endY = 600f // Adjust fade height as needed
    )
}

@Composable
fun StandardBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone50)
    ) {
        // Mesh Gradients (Unified Style)
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            // Top Left - Sage
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Sage600.copy(alpha = 0.15f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(0f, 0f),
                    radius = size.width * 0.9f // Increased radius for better visibility
                )
            )
            // Bottom Right - Lavender
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Lavender500.copy(alpha = 0.12f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    radius = size.width * 0.9f
                )
            )
        }
        content()
    }
}
