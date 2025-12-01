package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.DismissValue
import androidx.compose.material.DismissDirection
import androidx.compose.material.rememberDismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.HistoryViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    onSessionClick: (Long) -> Unit,
    onStartSession: () -> Unit,
    viewModel: HistoryViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    
    // Group sessions by date
    val groupedSessions = remember(uiState.sessions) {
        uiState.sessions.groupBy { session ->
            val instant = java.time.Instant.ofEpochMilli(session.startedAt)
            val zonedDateTime = instant.atZone(java.time.ZoneId.systemDefault())
            val today = java.time.LocalDate.now()
            val yesterday = today.minusDays(1)
            
            when (zonedDateTime.toLocalDate()) {
                today -> "Today"
                yesterday -> "Yesterday"
                else -> java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(zonedDateTime)
            }
        }
    }

    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground(
        modifier = modifier
    ) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPalette.Sage600)
            }
        } else if (uiState.sessions.isEmpty()) {
            EmptyHistoryState(onStartSession)
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Text(
                            text = "History",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppPalette.Stone900,
                            letterSpacing = (-1).sp
                        )

                        // Minimal Search Bar
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = { Text("Search", color = AppPalette.Stone500) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppPalette.Stone500) },
                            trailingIcon = if (uiState.searchQuery.isNotEmpty()) {
                                {
                                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = AppPalette.Stone500)
                                    }
                                }
                            } else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = AppPalette.Stone100,
                                unfocusedContainerColor = AppPalette.Stone100,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = AppPalette.Sage600
                            ),
                            singleLine = true
                        )
                    }
                }

                groupedSessions.forEach { (dateHeader, sessions) ->
                    item {
                        Text(
                            text = dateHeader.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = AppPalette.Stone500,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }

                    items(
                        items = sessions,
                        key = { it.id }
                    ) { session ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                    viewModel.deleteSession(session.id)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                    else -> Color.Transparent
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            },
                            dismissContent = {
                                MinimalSessionRow(
                                    session = session,
                                    onClick = { onSessionClick(session.id) }
                                )
                            },
                            directions = setOf(DismissDirection.EndToStart)
                        )
                        
                        HorizontalDivider(
                            color = AppPalette.Stone200,
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(start = 60.dp) // Indented divider
                        )
                    }
                }
                
                // Bottom padding for navigation bar
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun MinimalSessionRow(
    session: com.meetingcoach.leadershipconversationcoach.data.local.SessionEntity,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time / Duration
        Column(
            modifier = Modifier.width(60.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val instant = java.time.Instant.ofEpochMilli(session.startedAt)
            val timeFormatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a")
            val timeStr = instant.atZone(java.time.ZoneId.systemDefault()).format(timeFormatter)
            
            Text(
                text = timeStr,
                style = MaterialTheme.typography.bodySmall,
                color = AppPalette.Stone900,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${session.durationSeconds / 60}m",
                style = MaterialTheme.typography.labelSmall,
                color = AppPalette.Stone500
            )
        }
        
        // Title / Topic
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = session.title ?: "Untitled Session",
                style = MaterialTheme.typography.bodyLarge,
                color = AppPalette.Stone900,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = session.mode,
                style = MaterialTheme.typography.bodySmall,
                color = AppPalette.Stone500
            )
        }
        
        // Chevron
        Icon(
            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = AppPalette.Stone300,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun EmptyHistoryState(onStartSession: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Icon with gradient background
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(60.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlossyPrimaryStart,
                            GlossyPrimaryEnd
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "No Sessions Yet",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Start a coaching session to see your history and insights here.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartSession,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Start New Session",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}