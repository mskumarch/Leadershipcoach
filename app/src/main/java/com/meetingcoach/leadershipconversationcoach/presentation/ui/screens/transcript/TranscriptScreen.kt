package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.domain.models.MessageType
import com.meetingcoach.leadershipconversationcoach.domain.models.Speaker
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components.TranscriptItem
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components.getEmotionEmoji
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript.components.getSpeakerColor
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SessionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Transcript Screen - Simple Live Transcript View with SharedViewModel
 *
 * Design Philosophy:
 * - Show ONLY the live conversation transcript
 * - Clean, minimal, easy to read
 * - No analysis during live session
 * - Timeline, stats, and analysis moved to History screen (post-session)
 *
 * Features:
 * - Real-time transcript display
 * - Speaker identification with color coding
 * - Timestamps for each utterance
 * - Emotion indicators
 * - Auto-scroll to latest
 * - Synced with SessionViewModel
 *
 * Post-session features (moved to History):
 * - Timeline scrubber with markers
 * - Speaker statistics panel
 * - Export options
 * - Full analysis and insights
 */
@Composable
fun TranscriptScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // Observe session state from SharedViewModel
    val sessionState by viewModel.sessionState.collectAsState()

    // Filter messages to show only transcript items
    val transcriptItems = sessionState.messages.filter {
        it.type == MessageType.TRANSCRIPT
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7F5)) // Soft sage
    ) {
        if (sessionState.isRecording && transcriptItems.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📝 Live Transcript",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Recording indicator
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        Color(0xFFEF4444),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )

                            Text(
                                text = "Recording",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = sessionState.duration,
                            fontSize = 14.sp,
                            color = Color(0xFF1F2937),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Transcript items
                transcriptItems.forEach { item ->
                    val timestamp = formatTimestamp(item.timestamp)

                    // ✅ Use domain model properties correctly
                    val speakerName = item.speaker?.getDisplayName() ?: "Unknown"

                    // ✅ Get speaker index for color (map Speaker enum to index)
                    val speakerIndex = when (item.speaker) {
                        Speaker.USER -> 0
                        Speaker.OTHER -> 1
                        Speaker.SYSTEM -> 2
                        Speaker.UNKNOWN -> 3
                        null -> 0
                    }

                    // ✅ Get emotion from metadata
                    val emotionName = item.metadata?.emotion?.getDisplayName() ?: "Neutral"
                    val emotionEmoji = getEmotionEmoji(emotionName)

                    TranscriptItem(
                        timestamp = timestamp,
                        speakerName = speakerName,
                        speakerColor = getSpeakerColor(speakerIndex),
                        emotion = emotionName,
                        emotionEmoji = emotionEmoji,
                        content = item.content
                    )
                }

                // Live indicator at bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "✓ Updating in real-time",
                        fontSize = 13.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (sessionState.isRecording && transcriptItems.isEmpty()) {
            // Recording but no transcript yet
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📝",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Listening...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Transcript will appear here\nas people speak",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(0xFFEF4444),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )

                    Text(
                        text = "Recording ${sessionState.duration}",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            // Empty state - no recording
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📝",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "No Active Session",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Start a recording to see\nthe live transcript here",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "💡 Tip: Go to Chat or Coach tab to start",
                    fontSize = 13.sp,
                    color = Color(0xFF3B82F6),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Format timestamp from milliseconds to MM:SS
 */
private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    return formatter.format(date)
}