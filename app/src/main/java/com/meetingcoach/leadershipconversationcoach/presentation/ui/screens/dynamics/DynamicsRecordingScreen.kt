package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.dynamics

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dynamics Mode Recording Screen
 * "The Radar" - Visualizes alignment, resistance, and power dynamics.
 */
@Composable
fun DynamicsRecordingScreen(
    viewModel: SessionViewModel,
    onStopSession: () -> Unit
) {
    val sessionState by viewModel.sessionState.collectAsState()
    val audioLevel by viewModel.audioLevel.collectAsState()
    val dynamicsAnalysis by viewModel.dynamicsAnalysis.collectAsState()
    
    // Strategic Background (Darker Sage/Slate)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B), // Slate 800
                        Color(0xFF0F172A)  // Slate 900
                    )
                )
            )
    ) {
        // Radar Visualization (Center)
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            RadarView(isActive = sessionState.isRecording)
            
            // Central Status
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = sessionState.duration ?: "00:00",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "LISTENING FOR INTENT...",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppPalette.Sage400,
                    letterSpacing = 2.sp
                )
            }
        }

        // Top Bar
        val selectedStakeholder by viewModel.selectedStakeholder.collectAsState()
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(AppPalette.Red500, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "DYNAMICS MODE",
                            style = MaterialTheme.typography.labelSmall,
                            color = AppPalette.Sage400,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedStakeholder != null) {
                            Text(
                                text = "vs. ${selectedStakeholder?.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = onStopSession,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            // Stakeholder Tendencies (Prep Card)
            if (selectedStakeholder != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    selectedStakeholder?.tendencies?.take(2)?.forEach { tendency ->
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚠️ ${tendency.type}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppPalette.Sage200
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Controls
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Button(
                onClick = onStopSession,
                colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Red500),
                modifier = Modifier
                    .height(56.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("End Session", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        // Transcript Feed (Bottom Overlay)
    val lastTranscript = sessionState.messages.lastOrNull { it.type == com.meetingcoach.leadershipconversationcoach.domain.models.MessageType.TRANSCRIPT }?.content
    val currentText = if (sessionState.partialTranscript.isNotEmpty()) sessionState.partialTranscript else lastTranscript

    if (!currentText.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .heightIn(max = 100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = currentText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(16.dp),
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }

    // Alignment Meter (Bottom Left)
    AlignmentMeter(
        alignmentScore = dynamicsAnalysis?.alignmentScore ?: 100, // Use live data, default to 100
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(bottom = 48.dp, start = 24.dp)
    )

        // Live Subtext Decoder (Signals List)
        if (!dynamicsAnalysis?.detectedSignals.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp, bottom = 100.dp) // Above Alignment Meter
                    .width(300.dp)
            ) {
                Text(
                    text = "LIVE SUBTEXT DECODER",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppPalette.Sage400,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                dynamicsAnalysis?.detectedSignals?.takeLast(3)?.forEach { signal ->
                    Surface(
                        color = Color(0xFF1E293B).copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when(signal.type) {
                                    com.meetingcoach.leadershipconversationcoach.domain.models.SignalType.DEFLECTION -> "⚠️"
                                    com.meetingcoach.leadershipconversationcoach.domain.models.SignalType.VAGUE_COMMITMENT -> "⚓"
                                    com.meetingcoach.leadershipconversationcoach.domain.models.SignalType.PASSIVE_RESISTANCE -> "🛡️"
                                    com.meetingcoach.leadershipconversationcoach.domain.models.SignalType.STRONG_ALIGNMENT -> "✅"
                                    else -> "ℹ️"
                                },
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = signal.type.name.replace("_", " "),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = signal.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppPalette.Sage200
                                )
                                signal.suggestedResponse?.let { response ->
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "👉 $response",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF86EFAC), // Light Green for action
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Strategic Advice Overlay (if urgent)
        dynamicsAnalysis?.strategicAdvice?.let { advice ->
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppPalette.Sage900.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "♟️",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = advice,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun RadarView(isActive: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "Radar")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "Rotation"
    )

    Canvas(modifier = Modifier.size(300.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.width / 2

        // Draw Concentric Circles (Grid)
        val circles = 4
        for (i in 1..circles) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = (maxRadius / circles) * i,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Draw Crosshairs
        drawLine(
            color = Color.White.copy(alpha = 0.05f),
            start = Offset(center.x, 0f),
            end = Offset(center.x, size.height),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.05f),
            start = Offset(0f, center.y),
            end = Offset(size.width, center.y),
            strokeWidth = 1.dp.toPx()
        )

        // Draw Sweep
        if (isActive) {
            val angleRad = Math.toRadians(rotation.toDouble())
            val endX = center.x + maxRadius * cos(angleRad).toFloat()
            val endY = center.y + maxRadius * sin(angleRad).toFloat()

            // Sweep Line
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, AppPalette.Sage400.copy(alpha = 0.5f)),
                    start = center,
                    end = Offset(endX, endY)
                ),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

@Composable
fun AlignmentMeter(alignmentScore: Int, modifier: Modifier = Modifier) {
    val color = when {
        alignmentScore >= 75 -> AppPalette.Sage400 // Green/Good
        alignmentScore >= 40 -> Color(0xFFFBBF24)  // Yellow/Warning
        else -> AppPalette.Red500                  // Red/Danger
    }

    Column(modifier = modifier) {
        Text(
            text = "ALIGNMENT",
            style = MaterialTheme.typography.labelSmall,
            color = AppPalette.Sage400,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = "$alignmentScore%",
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Simple Bar
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(4.dp)
                .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(alignmentScore / 100f)
                    .height(4.dp)
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
}
