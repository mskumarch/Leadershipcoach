package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.recording

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun LiveWaveform(
    amplitude: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    // Normalize amplitude (0..32767) to 0..1
    val normalizedAmp = (amplitude / 32767f).coerceIn(0f, 1f)
    
    // Animated amplitude for smoothness
    val animatedAmp by animateFloatAsState(
        targetValue = normalizedAmp,
        animationSpec = tween(100, easing = LinearEasing),
        label = "amplitude"
    )

    // Store history of amplitudes for the wave effect
    val history = remember { androidx.compose.runtime.mutableStateListOf<Float>() }
    
    LaunchedEffect(animatedAmp) {
        history.add(animatedAmp)
        if (history.size > 50) { // Keep last 50 points
            history.removeAt(0)
        }
    }

    Canvas(modifier = modifier.fillMaxWidth().height(100.dp)) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f
        
        val stepX = width / 50f // Width per point
        
        history.forEachIndexed { index, amp ->
            val x = index * stepX
            // Randomize height slightly for "noise" effect + amplitude
            val barHeight = (amp * height * 0.8f) + (Random.nextFloat() * 10f)
            
            drawLine(
                color = color.copy(alpha = 0.5f + (amp * 0.5f)),
                start = Offset(x, centerY - barHeight / 2),
                end = Offset(x, centerY + barHeight / 2),
                strokeWidth = stepX * 0.6f,
                cap = StrokeCap.Round
            )
        }
    }
}
