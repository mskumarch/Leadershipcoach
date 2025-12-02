package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.clickable
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import kotlinx.coroutines.launch

/**
 * DynamicsSessionDetailScreen
 * "The Shadow Report" - Specialized insights for Dynamics Mode sessions.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DynamicsSessionDetailScreen(
    sessionDetails: SessionWithDetails,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val tabs = listOf("Analysis", "Transcript", "Coaching")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Column {
                            Text("Strategic Analysis", fontWeight = FontWeight.Bold)
                            Text(
                                "Dynamics Mode", 
                                style = MaterialTheme.typography.labelSmall, 
                                color = AppPalette.Sage400
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1E293B), // Slate 800
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
                
                Surface(
                    shadowElevation = 4.dp,
                    color = Color(0xFF1E293B) // Slate 800
                ) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color(0xFF1E293B),
                        contentColor = AppPalette.Sage400,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                height = 3.dp,
                                color = AppPalette.Sage400
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
                                text = { 
                                    Text(
                                        text = title,
                                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Medium,
                                        color = if (pagerState.currentPage == index) Color.White else Color.Gray
                                    ) 
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E293B), // Slate 800
                            Color(0xFF0F172A)  // Slate 900
                        )
                    )
                )
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> DynamicsAnalysisTab(sessionDetails)
                    1 -> TranscriptTab(sessionDetails.messages, sessionDetails.metrics?.summary)
                    2 -> CoachingTab(sessionDetails.messages)
                }
            }
        }
    }
}

@Composable
fun DynamicsAnalysisTab(sessionDetails: SessionWithDetails) {
    val analysis = remember(sessionDetails.metrics?.dynamicsAnalysisJson) {
        parseDynamicsAnalysis(sessionDetails.metrics?.dynamicsAnalysisJson)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (analysis != null) {
            // 1. Power Dynamics Score (Summary)
            item {
                PowerDynamicsScoreCard(score = analysis.powerDynamicsScore)
            }

            // 2. Alignment Map (Stakeholder Analysis)
            if (analysis.stakeholders.isNotEmpty()) {
                item {
                    AlignmentMapCard(analysis.stakeholders)
                }
            }

            // 3. Subtext Decoder (The "Meat")
            if (analysis.subtextSignals.isNotEmpty()) {
                item {
                    SubtextDecoderSection(analysis.subtextSignals)
                }
            }
            
            // 4. Objection Battle Cards
            if (analysis.battleCards.isNotEmpty()) {
                item {
                    BattleCardsSection(analysis.battleCards)
                }
            }

            // 5. Next Meeting Strategy
            if (analysis.strategies.isNotEmpty()) {
                item {
                    NextMeetingStrategyCard(analysis.strategies)
                }
            }
        } else {
            item {
                Text(
                    text = "No dynamics analysis available for this session.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

data class DynamicsAnalysis(
    val powerDynamicsScore: Int,
    val subtextSignals: List<SubtextSignal>,
    val strategies: List<Strategy>,
    val stakeholders: List<StakeholderMapItem> = emptyList(),
    val battleCards: List<BattleCardItem> = emptyList()
)

data class SubtextSignal(
    val quote: String,
    val type: String,
    val analysis: String
)

data class Strategy(
    val title: String,
    val description: String
)

data class StakeholderMapItem(
    val speaker: String,
    val role: String,
    val influenceScore: Int,
    val reason: String
)

data class BattleCardItem(
    val objection: String,
    val rebuttal: String
)

private fun parseDynamicsAnalysis(jsonString: String?): DynamicsAnalysis? {
    if (jsonString.isNullOrBlank()) return null
    try {
        val json = org.json.JSONObject(jsonString)
        val score = json.optInt("power_dynamics_score", 50)
        
        val signals = mutableListOf<SubtextSignal>()
        val signalsArray = json.optJSONArray("subtext_signals")
        if (signalsArray != null) {
            for (i in 0 until signalsArray.length()) {
                val obj = signalsArray.getJSONObject(i)
                signals.add(SubtextSignal(
                    quote = obj.optString("quote"),
                    type = obj.optString("type"),
                    analysis = obj.optString("analysis")
                ))
            }
        }
        
        val strategies = mutableListOf<Strategy>()
        val strategiesArray = json.optJSONArray("strategies")
        if (strategiesArray != null) {
            for (i in 0 until strategiesArray.length()) {
                val obj = strategiesArray.getJSONObject(i)
                strategies.add(Strategy(
                    title = obj.optString("title"),
                    description = obj.optString("description")
                ))
            }
        }

        val stakeholders = mutableListOf<StakeholderMapItem>()
        val stakeholdersArray = json.optJSONArray("stakeholder_map")
        if (stakeholdersArray != null) {
            for (i in 0 until stakeholdersArray.length()) {
                val obj = stakeholdersArray.getJSONObject(i)
                stakeholders.add(StakeholderMapItem(
                    speaker = obj.optString("speaker"),
                    role = obj.optString("role"),
                    influenceScore = obj.optInt("influence_score", 50),
                    reason = obj.optString("reason")
                ))
            }
        }

        val battleCards = mutableListOf<BattleCardItem>()
        val battleCardsArray = json.optJSONArray("objection_battle_cards")
        if (battleCardsArray != null) {
            for (i in 0 until battleCardsArray.length()) {
                val obj = battleCardsArray.getJSONObject(i)
                battleCards.add(BattleCardItem(
                    objection = obj.optString("objection"),
                    rebuttal = obj.optString("rebuttal")
                ))
            }
        }
        
        return DynamicsAnalysis(score, signals, strategies, stakeholders, battleCards)
    } catch (e: Exception) {
        return null
    }
}

@Composable
fun NextMeetingStrategyCard(strategies: List<Strategy>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D3748)), // Darker Slate
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Next Meeting Strategy",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            strategies.forEach { strategy ->
                StrategyItem(strategy.title, strategy.description)
            }
        }
    }
}

@Composable
fun StrategyItem(title: String, description: String) {
    Row(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(text = "👉", modifier = Modifier.padding(end = 8.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = AppPalette.Sage400)
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
        }
    }
}

@Composable
fun PowerDynamicsScoreCard(score: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(AppPalette.Sage400, RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = "Power Dynamics Score",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "You maintained strong influence.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppPalette.Sage200
                )
            }
        }
    }
}



// ... (Parsing logic update inside parseDynamicsAnalysis)
// stakeholders.add(StakeholderMapItem(
//     speaker = obj.optString("speaker"),
//     role = obj.optString("role"),
//     influenceScore = obj.optInt("influence_score", 50),
//     reason = obj.optString("reason")
// ))

@Composable
fun AlignmentMapCard(stakeholders: List<StakeholderMapItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Stakeholder Landscape",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Influence vs. Alignment",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            // 2D Scatter Plot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 8.dp)
            ) {
                StakeholderScatterPlot(stakeholders)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Legend / List below
            stakeholders.forEach { item ->
                StakeholderLegendItem(item)
            }
        }
    }
}

@Composable
fun StakeholderScatterPlot(stakeholders: List<StakeholderMapItem>) {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val padding = 40.dp.toPx()
        
        // Draw Axes
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = androidx.compose.ui.geometry.Offset(padding, height - padding),
            end = androidx.compose.ui.geometry.Offset(width - padding, height - padding),
            strokeWidth = 2f
        ) // X-Axis (Alignment)
        
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = androidx.compose.ui.geometry.Offset(padding, height - padding),
            end = androidx.compose.ui.geometry.Offset(padding, padding),
            strokeWidth = 2f
        ) // Y-Axis (Influence)
        
        // Labels
        // drawContext.canvas.nativeCanvas.drawText... (Too complex for simple canvas, using Text composables outside is better, but simple lines work for now)
        
        stakeholders.forEach { item ->
            // Map Role to X (0..100)
            val alignmentX = when(item.role.lowercase()) {
                "detractor" -> 20f // Left
                "ally" -> 80f      // Right
                else -> 50f        // Center (Neutral)
            }
            
            // Map Influence to Y (0..100)
            val influenceY = item.influenceScore.toFloat()
            
            val x = padding + (alignmentX / 100f) * (width - 2 * padding)
            val y = (height - padding) - (influenceY / 100f) * (height - 2 * padding)
            
            val color = when(item.role.lowercase()) {
                "ally" -> AppPalette.Sage400
                "detractor" -> AppPalette.Red500
                else -> Color.Gray
            }
            
            drawCircle(
                color = color,
                radius = 20f,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
            
            // Draw initial inside circle
            // (Simplified: just a circle for now)
        }
    }
    
    // Overlay Text Labels (Since Canvas text is hard in Compose without native access)
    // We can do this by overlaying a Box with absolute offsets, but for simplicity in this iteration, we rely on the Legend below.
}

@Composable
fun StakeholderLegendItem(item: StakeholderMapItem) {
    val color = when (item.role.lowercase()) {
        "ally" -> AppPalette.Sage400
        "detractor" -> AppPalette.Red500
        else -> Color.Gray
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = item.speaker,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "(${item.role}, Influence: ${item.influenceScore}%)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun SubtextDecoderSection(signals: List<SubtextSignal>) {
    Column {
        Text(
            text = "Subtext Decoder",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        signals.forEach { signal ->
            SubtextCard(signal.quote, signal.type, signal.analysis)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SubtextCard(quote: String, type: String, analysis: String) {
    val color = when(type) {
        "Deflection" -> AppPalette.Red500
        "Vague Commitment" -> Color(0xFFFBBF24) // Yellow
        "Strong Alignment" -> AppPalette.Sage400
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D3748)), // Darker Slate
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = type.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "\"$quote\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "🕵️ AI Analysis: $analysis",
                style = MaterialTheme.typography.bodyMedium,
                color = AppPalette.Sage200
            )
        }
    }
}

@Composable
fun BattleCardsSection(battleCards: List<BattleCardItem>) {
    Column {
        Text(
            text = "Objection Battle Cards",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Horizontal scroll for cards
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(battleCards) { card ->
                BattleCard(card)
            }
        }
    }
}

@Composable
fun BattleCard(card: BattleCardItem) {
    var isFlipped by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(320.dp)
            .clickable { isFlipped = !isFlipped },
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) AppPalette.Sage400 else Color(0xFF2D3748)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!isFlipped) {
                // Front: The Objection
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "OBJECTION",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppPalette.Red500,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "\"${card.objection}\"",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Tap to flip ↻",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            } else {
                // Back: The Rebuttal
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "WINNING STRATEGY",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = card.rebuttal,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
