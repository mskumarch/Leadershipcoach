package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val analysisInterval by viewModel.analysisInterval.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val coachingStyle by viewModel.coachingStyle.collectAsState()
    val hapticEnabled by viewModel.hapticEnabled.collectAsState()

    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = DeepCharcoal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Customize your coaching experience",
                style = MaterialTheme.typography.bodyMedium,
                color = NeutralGray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // AI Coaching Section
            SettingsSectionHeader("AI COACHING")
            
            SettingsGroup {
                SettingsRow(
                    label = "Analysis Frequency",
                    value = when (analysisInterval) {
                        30_000L -> "30s"
                        60_000L -> "60s"
                        120_000L -> "2m"
                        240_000L -> "4m"
                        else -> "60s"
                    },
                    onClick = { 
                        val next = when(analysisInterval) {
                            30_000L -> 60_000L
                            60_000L -> 120_000L
                            120_000L -> 240_000L
                            else -> 30_000L
                        }
                        viewModel.setAnalysisInterval(next)
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Appearance Section
            SettingsSectionHeader("APPEARANCE")
            
            SettingsGroup {
                SettingsRow(
                    label = "Text Size",
                    value = "${(fontSizeScale * 100).toInt()}%",
                    onClick = {
                         val next = if (fontSizeScale >= 1.4f) 0.8f else fontSizeScale + 0.1f
                         viewModel.setFontSizeScale(next)
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Preferences Section
            SettingsSectionHeader("PREFERENCES")
            
            SettingsGroup {
                SettingsRowSwitch(
                    label = "Haptic Feedback",
                    checked = hapticEnabled,
                    onCheckedChange = { viewModel.setHapticEnabled(it) }
                )
                HorizontalDivider(color = AppPalette.Stone200, thickness = 0.5.dp, modifier = Modifier.padding(start = 16.dp))
                SettingsRow(
                    label = "Coaching Style",
                    value = coachingStyle.lowercase().capitalize(),
                    onClick = {
                        val styles = listOf("EMPATHETIC", "EXECUTIVE", "SOCRATIC")
                        val currentIndex = styles.indexOf(coachingStyle)
                        val nextIndex = (currentIndex + 1) % styles.size
                        viewModel.setCoachingStyle(styles[nextIndex])
                    }
                )
            }

            // Info Card
            Spacer(modifier = Modifier.height(24.dp))
            InfoCard()
            
            // Bottom padding for navigation
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = AppPalette.Stone500,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
    )
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        content()
    }
}

@Composable
fun SettingsRow(
    label: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AppPalette.Stone900
        )
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppPalette.Stone500,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AppPalette.Stone300,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsRowSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AppPalette.Stone900
        )
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AppPalette.Sage500,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = AppPalette.Stone300
            )
        )
    }
}

@Composable
private fun InfoCard() {
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            AppPalette.Sage100.copy(alpha = 0.5f),
                            AppPalette.Lavender100.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lightbulb,
                        contentDescription = null,
                        tint = AppPalette.Sage600,
                        modifier = Modifier.padding(end = 8.dp).size(24.dp)
                    )
                    Text(
                        text = "Pro Tip",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Sage600
                    )
                }
                
                Text(
                    text = "Faster analysis provides more frequent insights but may interrupt your flow. Choose a frequency that matches your conversation style.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeutralGray,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.4f)
                )
            }
        }
    }
}