package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(72.dp),
            shape = RoundedCornerShape(percent = 50), // Fully rounded pill
            color = Color.Transparent, // Transparent surface, we draw background manually
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Background Layer (Glass & Shadows)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(GlassWhite, RoundedCornerShape(percent = 50))
                        .border(
                            width = 1.dp,
                            color = GlassBorderLight, // 30% white border
                            shape = RoundedCornerShape(percent = 50)
                        )
                        .drawBehind {
                            // Layered shadows
                            // Outer shadow
                            drawRoundRect(
                                color = ShadowStrong, // 12% black
                                topLeft = Offset(0f, 8.dp.toPx()),
                                size = size,
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2)
                            )
                            // Inner shadow
                            drawRoundRect(
                                color = ShadowMedium, // 8% black
                                topLeft = Offset(0f, 2.dp.toPx()),
                                size = size,
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2)
                            )
                            // Inner highlight (top edge)
                            drawRoundRect(
                                color = Color(0xCCFFFFFF), // 80% white
                                topLeft = Offset(0f, 0f),
                                size = size.copy(height = 1.dp.toPx()),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2)
                            )
                        }
                )

                // Content Layer (Icons) - NOT BLURRED
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    destinations.forEach { destination ->
                        NavigationItem(
                            destination = destination,
                            isSelected = currentDestination == destination.route,
                            onClick = { onNavigate(destination.route) }
                        )
                    }
                }
            }
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
        targetValue = if (isSelected) Color.White else DeepCharcoal,
        animationSpec = tween(300),
        label = "iconColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) SageGreen else Color.Transparent,
        animationSpec = tween(300),
        label = "backgroundColor"
    )



    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(backgroundColor)
            .padding(if (isSelected) 12.dp else 0.dp)
    ) {
        Icon(
            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
            contentDescription = destination.contentDescription,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}
