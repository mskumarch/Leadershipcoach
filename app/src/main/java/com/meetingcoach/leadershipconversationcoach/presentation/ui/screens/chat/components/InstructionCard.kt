package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Instruction Card Component
 *
 * Purpose: Welcome screen showing how to get started (idle state)
 *
 * Visual Style:
 * - Centered on screen
 * - Max width: 320dp
 * - Background: Light blue (#DCE4FF)
 * - Corner radius: 16dp
 * - Elevation: 2dp
 * - Content: Title, numbered steps, footer
 *
 * Example:
 * 🎯 Welcome to AI Coach
 * 1. Tap Coach icon 🧠
 * 2. Choose session type
 * 3. Get real-time nudges
 * Let's get started!
 */
@Composable
fun InstructionCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .widthIn(max = 320.dp)
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDCE4FF) // Light blue
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // Using shadow instead
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title with emoji
            Text(
                text = "🎯 Welcome to AI Coach",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E40AF), // Indigo
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Instructions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InstructionStep(
                    number = "1",
                    text = "Tap Coach icon 🧠"
                )

                InstructionStep(
                    number = "2",
                    text = "Choose session type"
                )

                InstructionStep(
                    number = "3",
                    text = "Get real-time nudges"
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Footer
            Text(
                text = "Let's get started!",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4F46E5), // Indigo
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Individual instruction step
 */
@Composable
private fun InstructionStep(
    number: String,
    text: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$number. $text",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937), // Dark gray
            lineHeight = 22.sp
        )
    }
}