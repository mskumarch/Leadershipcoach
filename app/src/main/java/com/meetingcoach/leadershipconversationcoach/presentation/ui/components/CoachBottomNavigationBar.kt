package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

enum class NavigationDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String
) {
    CHAT(
        route = "chat",
        selectedIcon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        contentDescription = "Chat"
    ),
    TRANSCRIPT(
        route = "transcript",
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unselectedIcon = Icons.AutoMirrored.Outlined.Article,
        contentDescription = "Transcript"
    ),
    PROGRESS(
        route = "progress",
        selectedIcon = Icons.Filled.Lightbulb, // Or BarChart if available
        unselectedIcon = Icons.Outlined.Lightbulb,
        contentDescription = "Progress"
    ),
    HISTORY(
        route = "history",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        contentDescription = "History"
    ),
    SETTINGS(
        route = "settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        contentDescription = "Settings"
    )
}

/**
 * Premium Glassmorphic Bottom Navigation Bar
 *
 * Features:
 * - Floating horizontal pill with 70% white glass effect
 * - Backdrop blur (20px equivalent)
 * - Layered shadows for depth
 * - Active indicator with sage green pill background
 * - 28px icons with smooth transitions
 * - 90% screen width, centered, 16dp from bottom
 */
@Composable
fun CoachBottomNavigationBar(
    currentDestination: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val destinations = NavigationDestination.values()
    
    // We split destinations into left and right of the FAB
    // Assuming 5 items, index 2 is the center (FAB)
    val leftDestinations = destinations.take(2)
    val rightDestinations = destinations.takeLast(2)
    val centerDestination = destinations[2] // Progress / Lightbulb

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp), // Increased height to accommodate pop-up
        contentAlignment = Alignment.BottomCenter
    ) {
        // The Pill Background
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(80.dp) // Increased from 72.dp
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(percent = 50),
            color = Color.White.copy(alpha = 0.9f),
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Items
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    leftDestinations.forEach { destination ->
                        NavigationItem(
                            destination = destination,
                            isSelected = currentDestination == destination.route,
                            onClick = { onNavigate(destination.route) }
                        )
                    }
                }

                // Spacer for FAB
                Spacer(modifier = Modifier.width(64.dp))

                // Right Items
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rightDestinations.forEach { destination ->
                        NavigationItem(
                            destination = destination,
                            isSelected = currentDestination == destination.route,
                            onClick = { onNavigate(destination.route) }
                        )
                    }
                }
            }
        }

        // The Pop-up FAB
        FloatingActionButton(
            onClick = { onNavigate(centerDestination.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp) // Push it up
                .size(64.dp)
                .shadow(8.dp, CircleShape),
            shape = CircleShape,
            containerColor = SageGreen,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = if (currentDestination == centerDestination.route) centerDestination.selectedIcon else centerDestination.unselectedIcon,
                contentDescription = centerDestination.contentDescription,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun NavigationItem(
    destination: NavigationDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) SageGreen else DeepCharcoal,
        animationSpec = tween(300),
        label = "iconColor"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
            contentDescription = destination.contentDescription,
            tint = iconColor,
            modifier = Modifier.size(26.dp)
        )
    }
}
