package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * Animated Typing Indicator
 * Three dots pulsing in sequence
 */
@Composable
fun TypingIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    dotColor: Color = AppPalette.Lavender500,
    spacing: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")

    @Composable
    fun animateDot(delayMillis: Int): Float {
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = delayMillis, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot"
        )
        return scale
    }

    val scale1 = animateDot(0)
    val scale2 = animateDot(200)
    val scale3 = animateDot(400)

    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Dot(scale1, dotSize, dotColor)
        Dot(scale2, dotSize, dotColor)
        Dot(scale3, dotSize, dotColor)
    }
}

@Composable
private fun Dot(scale: Float, size: Dp, color: Color) {
    Box(
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(color, CircleShape)
    )
}

// Need to import graphicsLayer
import androidx.compose.ui.graphics.graphicsLayer
