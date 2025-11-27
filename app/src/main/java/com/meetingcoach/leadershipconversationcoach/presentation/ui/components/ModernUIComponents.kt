package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * Modern UI Components Library
 * 
 * Beautiful, modern components inspired by contemporary app design
 */

/**
 * Pulsing Concentric Circles - Like voice recording visualization
 * 
 * Creates beautiful concentric circles that pulse outward
 */
@Composable
fun PulsingConcentricCircles(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,

    centerColor: Color = AppPalette.Lavender500, // Lavender
    ringColor: Color = AppPalette.Lavender500.copy(alpha = 0.3f),
    size: Dp = 200.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale1"
    )
    
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale2"
    )
    
    val scale3 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale3"
    )
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            // Outer ring
            Box(
                modifier = Modifier
                    .size(size * scale3)
                    .clip(CircleShape)
                    .background(ringColor.copy(alpha = 0.1f))
            )
            
            // Middle ring
            Box(
                modifier = Modifier
                    .size(size * scale2)
                    .clip(CircleShape)
                    .background(ringColor.copy(alpha = 0.2f))
            )
            
            // Inner ring
            Box(
                modifier = Modifier
                    .size(size * scale1)
                    .clip(CircleShape)
                    .background(ringColor.copy(alpha = 0.3f))
            )
        }
        
        // Center circle with gradient
        Box(
            modifier = Modifier
                .size(size * 0.5f)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            centerColor.copy(alpha = 0.8f),
                            centerColor
                        )
                    )
                )
        )
    }
}

/**
 * Waveform Visualization
 * 
 * Animated waveform like voice recording apps
 */
@Composable
fun WaveformVisualization(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    color: Color = AppPalette.Lavender500,
    barCount: Int = 40
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    
    Canvas(modifier = modifier) {
        val barWidth = size.width / barCount
        val centerY = size.height / 2
        
        for (i in 0 until barCount) {
            val x = i * barWidth + barWidth / 2
            
            // Create wave pattern
            val amplitude = if (isActive) {
                (sin(Math.toRadians((phase + i * 10).toDouble())) * 0.5 + 0.5).toFloat()
            } else {
                0.1f
            }
            
            val barHeight = size.height * amplitude * 0.8f
            
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(x, centerY - barHeight / 2),
                end = androidx.compose.ui.geometry.Offset(x, centerY + barHeight / 2),
                strokeWidth = barWidth * 0.6f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}

/**
 * Glassmorphic Card Background
 * 
 * Creates a frosted glass effect
 */
@Composable
fun GlassmorphicBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
    blurRadius: Dp = 10.dp
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.2f),
                        backgroundColor.copy(alpha = 0.05f)
                    )
                )
            )
            .blur(blurRadius)
    )
}

/**
 * Organic Shape Decoration
 * 
 * Decorative organic circles like in the design images
 */
@Composable
fun OrganicShapeDecoration(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        AppPalette.Sage500, // Sage
        AppPalette.Lavender500, // Lavender
        AppPalette.Sage100  // Light Sage
    )
) {
    Box(modifier = modifier) {
        // Large circle - top left
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .clip(CircleShape)
                .background(colors[0].copy(alpha = 0.3f))
        )
        
        // Medium circle - bottom right
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 40.dp, y = 40.dp)
                .clip(CircleShape)
                .background(colors[1].copy(alpha = 0.3f))
        )
        
        // Small circle - middle
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
                .offset(x = 60.dp, y = (-80).dp)
                .clip(CircleShape)
                .background(colors[2].copy(alpha = 0.2f))
        )
    }
}

/**
 * Gradient Button
 * 
 * Beautiful gradient button with shadow
 */
@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        AppPalette.Sage500,
        AppPalette.Sage600
    ),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
            .background(
                brush = Brush.horizontalGradient(colors)
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
