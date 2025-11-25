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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (sessionState.isRecording && transcriptItems.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 88.dp) // Space for stop button above nav
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📝 Live Transcript",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = sessionState.duration,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
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
                    // ✅ Get speaker index for color (map Speaker enum to index)
                    val speakerIndex = when (item.speaker) {
                        Speaker.USER -> 0
                        Speaker.OTHER -> 1
                        Speaker.SYSTEM -> 2
                        else -> 0
                    }

                    // Get emotion
                    val emotionName = item.metadata?.emotion?.getDisplayName() ?: "Neutral"
                    val emotionEmoji = getEmotionEmoji(emotionName)

                    TranscriptItem(
                        speakerName = speakerName,
                        timestamp = timestamp,
                        content = item.content,
                        speakerColor = getSpeakerColor(speakerIndex),
                        emotion = emotionName,
                        emotionEmoji = emotionEmoji
                    )
                }
            }
            
            // Floating Stop Button at bottom
            FloatingActionButton(
                onClick = { viewModel.stopSession() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp), // Above nav bar (72dp + 16dp)
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop Recording"
                    )
                    Text(
                        text = "Stop Recording",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else if (!sessionState.isRecording) {
            // Empty state - no recording
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "No Active Recording",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Start a coaching session to see the live transcript",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Recording but no transcript yet
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Listening...",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Waiting for conversation to begin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


// Format timestamp from milliseconds to MM:SS
private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}