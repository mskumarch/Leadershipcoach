package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.practice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.domain.models.PracticeScenario
import com.meetingcoach.leadershipconversationcoach.domain.models.ScenarioLibrary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeModeScreen(
    onBackClick: () -> Unit,
    onScenarioClick: (PracticeScenario) -> Unit
) {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Custom Top Bar to match
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone900)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Roleplay Practice",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone900
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Text(
                        text = "Choose a Scenario",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone900
                    )
                    Text(
                        text = "Practice difficult conversations in a safe environment with AI.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone500
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(ScenarioLibrary.scenarios) { scenario ->
                    ScenarioCard(scenario = scenario, onClick = { onScenarioClick(scenario) })
                }
            }
        }
    }
}

@Composable
fun ScenarioCard(scenario: PracticeScenario, onClick: () -> Unit) {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = scenario.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone900
            )
            Badge(
                containerColor = when (scenario.difficulty) {
                    "Easy" -> com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Sage500
                    "Medium" -> com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Amber500
                    else -> com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Red500
                }
            ) {
                Text(
                    text = scenario.difficulty,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = scenario.description,
            style = MaterialTheme.typography.bodyMedium,
            color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Stone500
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Sage600,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Start Practice",
                style = MaterialTheme.typography.labelLarge,
                color = com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette.Sage600
            )
        }
    }
}
