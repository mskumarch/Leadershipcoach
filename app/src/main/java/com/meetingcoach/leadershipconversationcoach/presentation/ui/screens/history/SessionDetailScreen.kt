package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMessageEntity
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMetricsEntity
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val tabs = listOf("Insights", "Transcript", "Coaching")

    Scaffold(
        topBar = {
            Column {
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
                
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { 
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (sessionDetails == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(padding)
            ) { page ->
                when (page) {
                    0 -> InsightsTab(sessionDetails)
                    1 -> TranscriptTab(sessionDetails.messages, sessionDetails.metrics?.summary)
                    2 -> CoachingTab(sessionDetails.messages)
                }
            }
        }
    }
}

@Composable
fun InsightsTab(sessionDetails: com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Scorecard Section
        item {
            ScorecardSection(metrics = sessionDetails.metrics, sessionMode = sessionDetails.session.mode)
        }

        // Summary Section
        item {
            InsightCard(title = "Summary", content = sessionDetails.metrics?.summary)
        }
        
        // Pace Analysis
        item {
            InsightCard(title = "Speaking Pace", content = sessionDetails.metrics?.paceAnalysis)
        }
        
        // Wording Analysis
        item {
            InsightCard(title = "Wording Style", content = sessionDetails.metrics?.wordingAnalysis)
        }
        
        // Improvements
        item {
            InsightCard(title = "Places to Improve", content = sessionDetails.metrics?.improvements, isHighlight = true)
        }
    }
}

@Composable
fun InsightCard(title: String, content: String?, isHighlight: Boolean = false) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlight) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isHighlight) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content ?: "Analysis not available.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isHighlight) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TranscriptTab(messages: List<SessionMessageEntity>, summary: String?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Summary Section
        if (!summary.isNullOrBlank()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "✨ Executive Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Filter only transcript messages
        val transcriptMessages = messages.filter { 
            it.messageType == "TRANSCRIPT" || (it.speaker != null && it.speaker != "AI") 
        }
        
        if (transcriptMessages.isEmpty()) {
            item { Text("No transcript available.") }
        } else {
            items(transcriptMessages) { message ->
                TranscriptItem(message)
            }
        }
    }
}

@Composable
fun CoachingTab(messages: List<SessionMessageEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Show only AI responses, Nudges, and User Questions (exclude raw transcript)
        val coachingMessages = messages.filter { 
            it.messageType == "AI_RESPONSE" || 
            it.messageType.contains("NUDGE") || 
            it.messageType == "USER_QUESTION" ||
            it.messageType == "CONTEXT"
        }
        
        if (coachingMessages.isEmpty()) {
            item { Text("No coaching insights available for this session.") }
        } else {
            items(coachingMessages) { message ->
                ChatHistoryItem(message)
            }
        }
    }
}

@Composable
fun ChatHistoryItem(message: SessionMessageEntity) {
    val isUser = message.speaker == "USER" || message.messageType == "USER_QUESTION"
    val isAi = message.messageType == "AI_RESPONSE" || message.messageType.contains("NUDGE") || message.messageType == "CONTEXT"
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isUser -> MaterialTheme.colorScheme.primaryContainer
                    isAi -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant // Transcript
                }
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Text(
                        text = when {
                            isAi -> "AI Coach"
                            else -> message.speaker ?: "Speaker"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ScorecardSection(metrics: SessionMetricsEntity?, sessionMode: String = "ONE_ON_ONE") {
    val labels = when (sessionMode) {
        "TEAM_MEETING" -> Triple("Alignment", "Participation", "Clarity")
        "DIFFICULT_CONVERSATION" -> Triple("Empathy", "Objectivity", "De-escalation")
        else -> Triple("Empathy", "Clarity", "Listening")
    }

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
                MetricRow(labels.first, metrics.empathyScore)
                MetricRow(labels.second, metrics.clarityScore)
                MetricRow(labels.third, metrics.listeningScore)
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
    // Check if content starts with [Speaker Name]
    val speakerRegex = Regex("^\\[(.+?)\\]\\s*(.*)")
    val match = speakerRegex.find(message.content)
    
    val speakerName = match?.groupValues?.get(1) ?: (message.speaker ?: "Speaker")
    val contentText = match?.groupValues?.get(2) ?: message.content
    
    // Clean up "Speaker" vs "Unknown"
    val displaySpeaker = if (speakerName == "UNKNOWN" || speakerName == "null") "Speaker" else speakerName

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = displaySpeaker,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = contentText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
