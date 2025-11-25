package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMessageEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMetricsEntity
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    sessionId: Long,
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    LaunchedEffect(sessionId) {
        viewModel.loadSessionDetails(sessionId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val sessionDetails = uiState.selectedSession

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Session Insights") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        if (sessionDetails == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Scorecard Section
                item {
                    ScorecardSection(metrics = sessionDetails.metrics)
                }

                // Transcript Section
                item {
                    Text(
                        text = "Transcript",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(sessionDetails.messages) { message ->
                    TranscriptItem(message)
                }
            }
        }
    }
}

@Composable
fun ScorecardSection(metrics: SessionMetricsEntity?) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Performance Scorecard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (metrics != null) {
                MetricRow("Empathy", metrics.empathyScore)
                MetricRow("Clarity", metrics.clarityScore)
                MetricRow("Listening", metrics.listeningScore)
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Talk Ratio: ${metrics.talkRatioUser}% You / ${100 - metrics.talkRatioUser}% Others")
            } else {
                Text("No metrics available for this session.")
            }
        }
    }
}

@Composable
fun MetricRow(label: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.width(100.dp))
        LinearProgressIndicator(
            progress = score / 100f,
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = if (score > 70) Color(0xFF4CAF50) else Color(0xFFFFC107),
            trackColor = Color.LightGray
        )
        Text(
            text = "$score/100",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TranscriptItem(message: SessionMessageEntity) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = message.speaker ?: "Unknown",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
