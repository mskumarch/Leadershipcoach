package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.recording

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.domain.models.ChatMessage
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * Live Transcript Studio
 * 
 * A Pixel Recorder-inspired UI for real-time transcription.
 * Features:
 * - Dark, high-contrast theme
 * - Continuous text flow
 * - Speaker separation
 * - Dynamic waveform
 */
@Composable
fun LiveTranscriptMode(
    messages: List<ChatMessage>,
    partialTranscript: String,
    amplitude: Int,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Auto-scroll to bottom
    LaunchedEffect(messages.size, partialTranscript) {
        if (messages.isNotEmpty() || partialTranscript.isNotEmpty()) {
            val totalItems = messages.count { it.type == MessageType.TRANSCRIPT } + (if (partialTranscript.isNotEmpty()) 1 else 0)
            if (totalItems > 0) {
                listState.animateScrollToItem(totalItems - 1)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E)) // Deep dark background like Pixel Recorder
    ) {
        // 1. Transcript Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Filter only transcript messages for this view
            val transcriptMessages = messages.filter { it.type == MessageType.TRANSCRIPT }
            
            items(transcriptMessages) { message ->
                TranscriptBlock(
                    text = message.content,
                    speaker = message.speaker?.name ?: "Speaker",
                    isFinal = true
                )
            }

            // Live Partial Segment
            if (partialTranscript.isNotEmpty()) {
                item {
                    TranscriptBlock(
                        text = partialTranscript,
                        speaker = "You", // Assume user is speaking for partials usually
                        isFinal = false
                    )
                }
            }
        }

        // 2. Bottom Waveform Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1C1C1E).copy(alpha = 0f),
                            Color(0xFF1C1C1E),
                            Color(0xFF1C1C1E)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            LiveWaveformBar(amplitude = amplitude)
        }
    }
}

@Composable
private fun TranscriptBlock(
    text: String,
    speaker: String,
    isFinal: Boolean
) {
    val isUser = speaker.equals("You", ignoreCase = true) || speaker.equals("User", ignoreCase = true)
    val speakerColor = if (isUser) AppPalette.Sage400 else Color(0xFFA5B4FC) // Sage for User, Indigo for Others

    Column(modifier = Modifier.fillMaxWidth()) {
        // Speaker Label
        Text(
            text = speaker.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = speakerColor,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Text Content
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                lineHeight = 32.sp
            ),
            color = if (isFinal) Color.White else Color.White.copy(alpha = 0.6f),
            fontWeight = if (isFinal) FontWeight.Normal else FontWeight.Light
        )
    }
}

@Composable
private fun LiveWaveformBar(amplitude: Int) {
    // Simulate a multi-bar waveform based on single amplitude
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(60.dp)
    ) {
        val bars = 7
        for (i in 0 until bars) {
            // Create a "wave" effect where center bars are taller
            val centerOffset = kotlin.math.abs(i - bars / 2)
            val scaleFactor = 1f - (centerOffset * 0.15f)
            
            // Animate height
            val heightPercent by animateFloatAsState(
                targetValue = (amplitude / 32767f) * scaleFactor * (0.5f + Math.random().toFloat() * 0.5f), // Add some jitter
                animationSpec = tween(100)
            )
            
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight(0.1f + heightPercent * 0.9f) // Min height 10%
                    .background(Color.White, RoundedCornerShape(50))
            )
        }
    }
}
