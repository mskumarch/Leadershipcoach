package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Session Mode Item Component
 *
 * Purpose: Clickable row item for session mode selection (bottom sheet style)
 *
 * Visual Style:
 * - Icon on left (blue background circle)
 * - Title and description in center
 * - Chevron arrow on right
 * - Full width clickable
 * - Light background on press
 *
 * Example:
 * [👤] 1-on-1 Coaching          [→]
 *      Perfect for individual...
 */
@Composable
fun SessionModeItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with blue background
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFDCE4FF) // Light blue
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6), // Blue
                    modifier = Modifier
                        .size(56.dp)
                        .padding(14.dp)
                )
            }

            // Title and description
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 18.sp
                )
            }

            // Chevron arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Select",
                tint = Color(0xFF9CA3AF), // Light gray
                modifier = Modifier.size(24.dp)
            )
        }
    }
}