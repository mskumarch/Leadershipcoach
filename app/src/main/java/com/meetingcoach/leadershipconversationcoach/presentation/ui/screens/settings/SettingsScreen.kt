package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.GradientBackground
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.SettingsCard
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
    val dailyNudgeTime by viewModel.dailyNudgeTime.collectAsState()

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
            SectionHeader(
                title = "AI Coaching",
                icon = androidx.compose.material.icons.Icons.Rounded.SmartToy
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Modern Card with Dropdown
            // Modern Card with Dropdown
            com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = AppPalette.Sage600,
                        modifier = Modifier.padding(end = 8.dp).size(24.dp)
                    )
                    Text(
                        text = "Analysis Frequency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepCharcoal
                    )
                }
                
                Text(
                    text = "How often the AI analyzes your conversation for coaching insights.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeutralGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Modern Dropdown
                AnalysisIntervalDropdown(
                    selectedInterval = analysisInterval,
                    onIntervalSelected = { viewModel.setAnalysisInterval(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Appearance Section
            SectionHeader(
                title = "Appearance",
                icon = androidx.compose.material.icons.Icons.Rounded.Palette
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Text Size",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepCharcoal
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("A", style = MaterialTheme.typography.bodySmall, color = DeepCharcoal)
                    Slider(
                        value = fontSizeScale,
                        onValueChange = { viewModel.setFontSizeScale(it) },
                        valueRange = 0.8f..1.4f,
                        steps = 5,
                        modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = AppPalette.Sage600,
                            activeTrackColor = AppPalette.Sage500,
                            inactiveTrackColor = AppPalette.Stone300
                        )
                    )
                    Text("A", style = MaterialTheme.typography.headlineSmall, color = DeepCharcoal)
                }
                
                Text(
                    text = "Preview: The quick brown fox jumps over the lazy dog.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeutralGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Preferences Section
            SectionHeader(
                title = "Preferences",
                icon = androidx.compose.material.icons.Icons.Rounded.Settings
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(modifier = Modifier.fillMaxWidth()) {
                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Haptic Feedback",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepCharcoal
                        )
                        Text(
                            text = "Vibrate on interactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeutralGray
                        )
                    }
                    Switch(
                        checked = hapticEnabled,
                        onCheckedChange = { viewModel.setHapticEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AppPalette.Sage500,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = AppPalette.Stone300
                        )
                    )
                }
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = AppPalette.Stone300
                )
                
                // Coaching Style
                Text(
                    text = "Coaching Style",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepCharcoal,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                val styles = listOf("EMPATHETIC" to "Empathetic Friend", "EXECUTIVE" to "Executive Coach", "SOCRATIC" to "Socratic Mentor")
                
                styles.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = coachingStyle == key,
                            onClick = { viewModel.setCoachingStyle(key) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppPalette.Sage600,
                                unselectedColor = AppPalette.Stone500
                            )
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepCharcoal,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
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
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppPalette.Sage600,
            modifier = Modifier.padding(end = 8.dp).size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AppPalette.Sage600
        )
    }
}

@Composable
private fun AnalysisIntervalDropdown(
    selectedInterval: Long,
    onIntervalSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    val options = listOf(
        30_000L to "⚡ 30 Seconds - Fast",
        60_000L to "⚖️ 60 Seconds - Balanced",
        120_000L to "🧘 120 Seconds - Relaxed",
        240_000L to "🌙 240 Seconds - Calm"
    )
    
    val selectedOption = options.find { it.first == selectedInterval } ?: options[1]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = AppPalette.Sage500.copy(alpha = 0.1f)
            )
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = DeepCharcoal
            ),
            border = null
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption.second,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = AppPalette.Sage600
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            options.forEach { (value, label) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (value == selectedInterval) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                            color = if (value == selectedInterval) {
                                AppPalette.Sage600
                            } else {
                                DeepCharcoal
                            }
                        )
                    },
                    onClick = {
                        onIntervalSelected(value)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (value == selectedInterval) {
                                AppPalette.Sage100.copy(alpha = 0.5f)
                            } else {
                                Color.Transparent
                            }
                        )
                )
            }
        }
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
                        imageVector = androidx.compose.material.icons.Icons.Rounded.Lightbulb,
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