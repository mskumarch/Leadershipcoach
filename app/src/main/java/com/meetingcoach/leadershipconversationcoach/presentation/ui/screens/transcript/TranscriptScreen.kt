package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.transcript

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
            // Empty state - Before Session Starts Panel
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                
                Text(
                    text = "Before Session Starts",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Sage900
                )

                // 1. Quick Tip Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AppPalette.Sage100),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("💡", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "QUICK TIP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppPalette.Sage600
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Take a deep breath before you start. A calm mind leads to clearer communication.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppPalette.Sage900
                            )
                        }
                    }
                }

                // 2. What Good Communication Looks Like
                Text(
                    text = "What good communication looks like",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Sage900
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CommunicationMicroCard(
                        icon = "👂",
                        title = "Active Listening",
                        modifier = Modifier.weight(1f)
                    )
                    CommunicationMicroCard(
                        icon = "🎯",
                        title = "Clear Intent",
                        modifier = Modifier.weight(1f)
                    )
                    CommunicationMicroCard(
                        icon = "🤝",
                        title = "Empathy",
                        modifier = Modifier.weight(1f)
                    )
                }

                // 3. Warm-up Questions
                Text(
                    text = "Warm-up Questions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Sage900
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    WarmUpQuestionCard("What is the main goal of this conversation?")
                    WarmUpQuestionCard("How do I want the other person to feel?")
                }
                
                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
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