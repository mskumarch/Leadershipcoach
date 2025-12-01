package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
 * Transcript Screen - Live Transcript View
 *
 * Refactored for performance (LazyColumn) and stability.
 */
@Composable
fun TranscriptScreen(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val sessionState by viewModel.sessionState.collectAsState()
    val listState = rememberLazyListState()

    // Filter messages to show only transcript items
    val transcriptItems = remember(sessionState.messages) {
        sessionState.messages.filter { it.type == MessageType.TRANSCRIPT }
    }

    // Auto-scroll to bottom when new items arrive
    LaunchedEffect(transcriptItems.size) {
        if (transcriptItems.isNotEmpty()) {
            listState.animateScrollToItem(transcriptItems.size - 1)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
        // Background removed to allow cloud wallpaper to show through
    ) {
        if (sessionState.isRecording && transcriptItems.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
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
                                            MaterialTheme.colorScheme.error,
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
                }

                // Transcript List
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 100.dp, top = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(transcriptItems) { item ->
                        val timestamp = formatTimestamp(item.timestamp)
                        val speakerName = item.speaker?.getDisplayName() ?: "Unknown"
                        
                        val speakerIndex = when (item.speaker) {
                            Speaker.USER -> 0
                            Speaker.OTHER -> 1
                            Speaker.SYSTEM -> 2
                            else -> 0
                        }

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
            }
            
            // Stop Button
            FloatingActionButton(
                onClick = { viewModel.stopSession() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp),
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Active Session",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Start a recording from the Coach tab to see the live transcript here.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Recording but no transcript yet
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
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