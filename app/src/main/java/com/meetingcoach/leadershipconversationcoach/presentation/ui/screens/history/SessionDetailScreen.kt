package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
    val averageMetrics = uiState.averageMetrics
    
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
                    0 -> InsightsTab(sessionDetails, averageMetrics)
                    1 -> TranscriptTab(sessionDetails.messages, sessionDetails.metrics?.summary)
                    2 -> CoachingTab(sessionDetails.messages)
                }
            }
        }
    }


    // Follow-Up Dialog
    if (uiState.generatedFollowUp != null) {
        FollowUpDialog(
            content = uiState.generatedFollowUp!!,
            onDismiss = { viewModel.clearFollowUpDraft() },
            onCopy = { 
                // Copy to clipboard logic would go here
                viewModel.clearFollowUpDraft()
            }
        )
    }
}

@Composable
fun FollowUpDialog(
    content: String,
    onDismiss: () -> Unit,
    onCopy: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("One-Tap Follow-Up") },
        text = {
            Column {
                Text("Ready to send to your team member:", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = content,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onCopy) {
                Text("Copy to Clipboard")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun InsightsTab(
    sessionDetails: com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails,
    averageMetrics: com.meetingcoach.leadershipconversationcoach.data.local.AverageMetricsTuple?,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Session Health Header (Traffic Light)
        item {
            SessionHealthHeader(metrics = sessionDetails.metrics)
        }

        // 2. Game Film (Timeline Analysis)
        item {
            ExpandableMasterCoachCard(title = "Game Film", icon = "🎬", defaultExpanded = true) {
                GameFilmTimeline(
                    durationSeconds = sessionDetails.session.durationSeconds,
                    messages = sessionDetails.messages,
                    startedAt = sessionDetails.session.startedAt
                )
            }
        }

        // 2. Game Film (Timeline Analysis)
        item {
            ExpandableMasterCoachCard(title = "Game Film", icon = "🎬", defaultExpanded = true) {
                GameFilmTimeline(
                    durationSeconds = sessionDetails.session.durationSeconds,
                    messages = sessionDetails.messages,
                    startedAt = sessionDetails.session.startedAt
                )
            }
        }

        // 3. Session Summary (The "Executive Brief") - Expandable
        item {
            ExpandableMasterCoachCard(title = "Session Summary", icon = "📝", defaultExpanded = false) {
                Text(
                    text = sessionDetails.metrics?.summary ?: "No summary available yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // 3. Scorecard (Quantitative) - Expandable
        item {
            ExpandableMasterCoachCard(title = "Performance Scorecard", icon = "📊", defaultExpanded = false) {
                ScorecardSection(
                    metrics = sessionDetails.metrics, 
                    sessionMode = sessionDetails.session.mode,
                    averageMetrics = averageMetrics,
                    isEmbedded = true
                )
            }
        }

        // 4. Strengths & Improvements (The "Feedback Loop") - Expandable
        item {
            ExpandableMasterCoachCard(title = "Feedback Loop", icon = "🔄", defaultExpanded = false) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Strengths", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        // Mock data or parse from metrics
                        BulletPoint("Clear communication")
                        BulletPoint("Empathetic listening")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Improvements", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = sessionDetails.metrics?.improvements ?: "None identified.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 5. Decisions & Action Items (The "Accountability") - Always Expanded
        item {
            MasterCoachCard(title = "Action Plan", icon = "✅") {
                Text(
                    text = "Decisions Made:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                BulletPoint("Schedule follow-up with team")
                BulletPoint("Review project timeline")
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Action Items:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ActionItemRow("Send email update", "You", "Tomorrow")
                ActionItemRow("Prepare slide deck", "Mentee", "Fri")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // One-Tap Follow-Up Button
                Button(
                    onClick = { viewModel.generateFollowUpDraft(sessionDetails.session.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = !uiState.isGeneratingFollowUp
                ) {
                    if (uiState.isGeneratingFollowUp) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Drafting...")
                    } else {
                        Text("✉️ Draft Follow-Up Email")
                    }
                }
            }
        }

        // 6. Next Agenda (The "Forward Look") - Expandable
        item {
            ExpandableMasterCoachCard(title = "Next Session Agenda", icon = "📅", defaultExpanded = false) {
                BulletPoint("Review progress on action items")
                BulletPoint("Deep dive into 'Strategic Thinking'")
                BulletPoint("Feedback on recent presentation")
            }
        }
    }
}

@Composable
fun SessionHealthHeader(metrics: SessionMetricsEntity?) {
    val healthStatus = when {
        metrics == null -> Triple("Analyzing...", Color.Gray, "Waiting for data...")
        metrics.empathyScore > 70 && metrics.clarityScore > 70 -> Triple("Clarity Mode", Color(0xFF4CAF50), "High alignment & clarity detected.")
        metrics.empathyScore < 50 || metrics.interruptionCount > 5 -> Triple("Intervention Mode", Color(0xFFE57373), "Tension or disengagement detected.")
        else -> Triple("Calibration Mode", Color(0xFFFFB74D), "Good progress, but expectations vague.")
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = healthStatus.second.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, healthStatus.second.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(healthStatus.second, androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = healthStatus.first,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = healthStatus.second
                )
                Text(
                    text = healthStatus.third,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ExpandableMasterCoachCard(
    title: String,
    icon: String,
    defaultExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            ) {
                Text(text = icon, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            androidx.compose.animation.AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun MasterCoachCard(
    title: String,
    icon: String,
    color: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Text(text = icon, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(text = "•", modifier = Modifier.padding(end = 8.dp), color = MaterialTheme.colorScheme.primary)
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ActionItemRow(task: String, owner: String, due: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = task, style = MaterialTheme.typography.bodyMedium)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(
                    text = owner,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(text = due, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
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
        // Summary Section
        if (!summary.isNullOrBlank()) {
            val parts = summary.split("### Takeaways")
            val mainSummary = parts.getOrNull(0)?.trim()
            val takeaways = parts.getOrNull(1)?.trim()

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
                        
                        // Render Main Summary
                        if (mainSummary != null) {
                            FormattedBulletPoints(mainSummary, MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        
                        // Render Takeaways if present
                        if (!takeaways.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "🚀 Key Takeaways",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FormattedBulletPoints(takeaways, MaterialTheme.colorScheme.onPrimaryContainer)
                        }
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
fun GameFilmTimeline(
    durationSeconds: Int,
    messages: List<SessionMessageEntity>,
    startedAt: Long
) {
    // Filter for key moments
    val keyMoments = messages.filter {
        it.messageType.contains("NUDGE") || 
        it.messageType == "USER_QUESTION" ||
        it.messageType == "AI_RESPONSE"
    }

    if (keyMoments.isEmpty()) {
        Text("No key moments detected in this session.", style = MaterialTheme.typography.bodySmall)
        return
    }

    var selectedMoment by remember { mutableStateOf<SessionMessageEntity?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Session Timeline (${durationSeconds / 60}m ${durationSeconds % 60}s)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Timeline Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(2.dp))
            )
            
            // Canvas for precise drawing of markers
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { selectedMoment = null } // Deselect on background click
            ) {
                val width = size.width
                val trackY = size.height / 2
                
                keyMoments.forEach { moment ->
                    val relativeTime = (moment.createdAt - startedAt) / 1000f
                    val progress = if (durationSeconds > 0) (relativeTime / durationSeconds).coerceIn(0f, 1f) else 0f
                    val x = width * progress
                    
                    val color = when {
                        moment.messageType.contains("URGENT") -> Color(0xFFE57373)
                        moment.messageType.contains("IMPORTANT") -> Color(0xFFFFB74D)
                        moment.messageType == "USER_QUESTION" -> Color(0xFF64B5F6)
                        else -> Color(0xFFAED581)
                    }
                    
                    drawCircle(
                        color = color,
                        radius = 6.dp.toPx(),
                        center = Offset(x, trackY)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            keyMoments.forEach { moment ->
                val relativeSeconds = ((moment.createdAt - startedAt) / 1000).coerceAtLeast(0)
                val timeString = String.format("%02d:%02d", relativeSeconds / 60, relativeSeconds % 60)
                
                val (icon, color, label) = when {
                    moment.messageType.contains("URGENT") -> Triple("🔴", MaterialTheme.colorScheme.error, "Tension/Risk")
                    moment.messageType.contains("IMPORTANT") -> Triple("🟡", Color(0xFFFFB74D), "Coaching Opportunity")
                    moment.messageType == "USER_QUESTION" -> Triple("🟣", MaterialTheme.colorScheme.primary, "Key Question")
                    else -> Triple("🟢", MaterialTheme.colorScheme.secondary, "Insight")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMoment = moment }
                        .background(
                            if (selectedMoment == moment) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(40.dp)
                    )
                    Text(text = icon, modifier = Modifier.padding(horizontal = 8.dp))
                    Column {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                        Text(
                            text = moment.content,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = if (selectedMoment == moment) 10 else 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScorecardSection(
    metrics: SessionMetricsEntity?, 
    sessionMode: String = "ONE_ON_ONE",
    averageMetrics: com.meetingcoach.leadershipconversationcoach.data.local.AverageMetricsTuple? = null,
    isEmbedded: Boolean = false
) {
    val labels = when (sessionMode) {
        "TEAM_MEETING" -> Triple("Alignment", "Participation", "Clarity")
        "DIFFICULT_CONVERSATION" -> Triple("Empathy", "Objectivity", "De-escalation")
        else -> Triple("Empathy", "Clarity", "Listening")
    }

    val containerColor = if (isEmbedded) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant
    
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth(),
        elevation = if (isEmbedded) CardDefaults.cardElevation(defaultElevation = 0.dp) else CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = if (isEmbedded) Modifier else Modifier.padding(16.dp)) {
            if (!isEmbedded) {
                Text(
                    text = "Performance Scorecard",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (metrics != null) {
                MetricRow(labels.first, metrics.empathyScore, averageMetrics?.avgEmpathy)
                MetricRow(labels.second, metrics.clarityScore, averageMetrics?.avgClarity)
                MetricRow(labels.third, metrics.listeningScore, averageMetrics?.avgListening)
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
fun MetricRow(label: String, score: Int, average: Double? = null) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
        if (average != null) {
            val diff = score - average
            val diffText = if (diff >= 0) "+${diff.toInt()}" else "${diff.toInt()}"
            val diffColor = if (diff >= 0) Color(0xFF4CAF50) else Color.Red
            
            Text(
                text = "vs Avg: $diffText",
                style = MaterialTheme.typography.labelSmall,
                color = diffColor,
                modifier = Modifier.padding(start = 100.dp) // Align with progress bar
            )
        }
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

@Composable
fun FormattedBulletPoints(text: String, color: Color) {
    val lines = text.lines()
    Column {
        lines.forEach { line ->
            val trimmed = line.trim()
            if (trimmed.startsWith("*") || trimmed.startsWith("-") || trimmed.startsWith("•")) {
                Row(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = color,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = trimmed.drop(1).trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = color
                    )
                }
            } else if (trimmed.isNotBlank()) {
                Text(
                    text = trimmed,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}
