package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

/**
 * Modern Recording Interface
 * 
 * Beautiful recording UI with pulsing circles and waveform
 */
@Composable
fun ModernRecordingInterface(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    sessionMode: SessionMode?,
    duration: String,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F0FF), // Light lavender
                        Color(0xFFFFF5F0)  // Light peach
                    )
                )
            )
    ) {
        // Decorative organic shapes
        OrganicShapeDecoration(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(AccentCoral, AccentLavender, AccentMint)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isRecording) {
                // Recording state
                RecordingActiveView(
                    sessionMode = sessionMode,
                    duration = duration,
                    onStop = onStopRecording
                )
            } else {
                // Idle state
                RecordingIdleView(
                    sessionMode = sessionMode,
                    onStart = onStartRecording
                )
            }
        }
    }
}

@Composable
private fun RecordingActiveView(
    sessionMode: SessionMode?,
    duration: String,
    onStop: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Session mode badge
        sessionMode?.let {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AccentLavender.copy(alpha = 0.3f)
            ) {
                Text(
                    text = it.getDisplayName(),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        // Pulsing concentric circles
        PulsingConcentricCircles(
            isActive = true,
            centerColor = RecordingActive,
            ringColor = RecordingPulse,
            size = 240.dp
        )
        
        // Duration
        Text(
            text = duration,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // Waveform
        WaveformVisualization(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp),
            isActive = true,
            color = RecordingWave
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stop button
        GradientButton(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp),
            colors = listOf(
                Color(0xFFEF5350),
                Color(0xFFE53935)
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Stop Recording",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RecordingIdleView(
    sessionMode: SessionMode?,
    onStart: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Title
        Text(
            text = "Ready to begin?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        // Subtitle with session mode
        sessionMode?.let {
            Text(
                text = it.getDisplayName(),
                style = MaterialTheme.typography.titleMedium,
                color = AccentLavender,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Microphone icon with breathing animation
        Box(
            modifier = Modifier
                .size(180.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentLavender.copy(alpha = 0.3f),
                            AccentLavender.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Microphone",
                tint = AccentLavender,
                modifier = Modifier.size(80.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Start button
        GradientButton(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp),
            colors = GradientCoralRose
        ) {
            Text(
                text = "Start Recording",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        // Tip
        Text(
            text = "Tap to start your coaching session",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
