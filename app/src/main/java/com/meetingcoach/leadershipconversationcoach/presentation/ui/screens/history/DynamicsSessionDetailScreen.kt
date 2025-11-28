package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * DynamicsSessionDetailScreen
 * "The Shadow Report" - Specialized insights for Dynamics Mode sessions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicsSessionDetailScreen(
    sessionDetails: SessionWithDetails,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Power Dynamics Score (Summary)
                item {
                    PowerDynamicsScoreCard(score = 85) // Mock score for now
                }

                // 2. Alignment Map (Timeline)
                item {
                    AlignmentMapCard()
                }

                // 3. Subtext Decoder (The "Meat")
                item {
                    SubtextDecoderSection()
                }
            }
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

@Composable
fun AlignmentMapCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Alignment Map",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Visualization of alignment over time (Mock)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            // Placeholder for Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📈 Chart Placeholder", color = Color.White.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun SubtextDecoderSection() {
    Column {
        Text(
            text = "Subtext Decoder",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Mock Signals
        val signals = listOf(
            Triple("Let's take this offline", "Deflection", "They avoided the decision."),
            Triple("I'll try to see if...", "Vague Commitment", "Low accountability detected."),
            Triple("I completely agree", "Strong Alignment", "You have their buy-in.")
        )
        
        signals.forEach { (quote, type, analysis) ->
            SubtextCard(quote, type, analysis)
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
