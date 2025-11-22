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
 * Modern Glossy Pill-Shaped Bottom Navigation Bar
 *
 * Features:
 * - Full pill-shaped container with sea green to lavender gradient
 * - Glossy highlight overlay for premium glass effect
 * - Individual pill items with enhanced depth
 * - Smooth color transitions and animations
 * - Floating elevated design with dramatic shadow
 */
@Composable
fun CoachBottomNavigationBar(
    currentDestination: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 16.dp,
                    spotColor = GlossyShadow,
                    shape = RoundedCornerShape(36.dp)
                ),
            color = Color.Transparent,
            shape = RoundedCornerShape(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                GlossyNavBarStart,
                                GlossyNavBarEnd
                            )
                        ),
                        shape = RoundedCornerShape(36.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    GlossyHighlight,
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
    }
}

@Composable
private fun GlossyNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.White else TextSecondary,
        animationSpec = tween(350),
        label = "iconColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) 8.dp else 0.dp,
        animationSpec = tween(350),
        label = "elevation"
    )

    val pillSize by animateDpAsState(
        targetValue = if (selected) 56.dp else 48.dp,
        animationSpec = tween(350),
        label = "pillSize"
    )

    Box(
        modifier = Modifier
            .size(pillSize)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(pillSize / 2),
                spotColor = if (selected) GlossyShadow else Color.Transparent
            )
            .clip(RoundedCornerShape(pillSize / 2))
            .background(
                brush = if (selected) {
                    Brush.verticalGradient(
                        colors = listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(20.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlossyHighlight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = pillSize / 2, topEnd = pillSize / 2)
                    )
            )
        }

        IconButton(
            onClick = onClick,
            modifier = Modifier.size(pillSize)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(if (selected) 26.dp else 24.dp)
            )
        }
    }
}
