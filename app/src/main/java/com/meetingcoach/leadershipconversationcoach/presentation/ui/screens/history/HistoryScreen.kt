package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.components.SessionCard
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.HistoryViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5F3),
                        Color(0xFFF3E5F5)
                    )
                )
            )
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF4DB6AC)
                    )
                }
            }

            uiState.sessions.isEmpty() -> {
                HistoryEmptyState(modifier = Modifier.fillMaxSize())
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = "📚 Session History",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${uiState.sessions.size} coaching sessions completed",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    items(uiState.sessions) { session ->
                        SessionCard(
                            session = session,
                            onClick = {
                                session.id?.let { viewModel.loadSessionDetails(it) }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.error != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFEF4444),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun HistoryEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📚",
            fontSize = 72.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Sessions Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Start a coaching session to see\nyour history and track your progress",
            fontSize = 15.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "💡 Tip: Your completed sessions will appear here\nwith detailed insights and transcripts",
            fontSize = 13.sp,
            color = Color(0xFF9575CD),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}