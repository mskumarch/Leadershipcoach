package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    COACH(
        route = "coach",
        selectedIcon = Icons.Filled.Lightbulb,
        unselectedIcon = Icons.Outlined.Lightbulb,
        contentDescription = "Coach"
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
 * Modern Glossy Bottom Navigation Bar
 * 
 * Features:
 * - Pill-shaped selected indicator with gradient
 * - Glossy highlight effect
 * - Smooth animations
 * - Elevated design with shadow
 */
@Composable
fun CoachBottomNavigationBar(
    currentDestination: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                spotColor = GlossyShadow,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ),
        color = Surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationDestination.entries.forEach { destination ->
                GlossyNavItem(
                    selected = currentDestination == destination.route,
                    onClick = { onNavigate(destination.route) },
                    icon = if (currentDestination == destination.route) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    },
                    contentDescription = destination.contentDescription
                )
            }
        }
    }
}

@Composable
private fun GlossyNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color.Transparent else Color.Transparent,
        animationSpec = tween(300),
        label = "backgroundColor"
    )
    
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else TextSecondary,
        animationSpec = tween(300),
        label = "iconColor"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (selected) 6.dp else 0.dp,
        animationSpec = tween(300),
        label = "elevation"
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(28.dp),
                spotColor = GlossyShadow
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = if (selected) {
                    Brush.verticalGradient(
                        colors = listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(backgroundColor, backgroundColor)
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Glossy highlight for selected state
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlossyHighlight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
            )
        }
        
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
