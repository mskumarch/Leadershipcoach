package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlassDesign
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.glass

@Composable
fun FloatingPillNav(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Glass Pill Container
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .height(72.dp)
            .glass(
                shape = RoundedCornerShape(100.dp), // Full pill shape
                alpha = GlassDesign.NavigationAlpha
            )
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTab(
                icon = Icons.Outlined.ChatBubbleOutline,
                selectedIcon = Icons.Filled.ChatBubble,
                label = "Coach",
                isSelected = currentTab == 0,
                onClick = { onTabSelected(0) }
            )
            
            NavTab(
                icon = Icons.Outlined.Article,
                selectedIcon = Icons.Filled.Article,
                label = "Transcript",
                isSelected = currentTab == 1,
                onClick = { onTabSelected(1) }
            )
            
            // Center Action Button (Progress/Insights)
            NavCenterButton(
                icon = Icons.Outlined.Insights,
                selectedIcon = Icons.Filled.Insights,
                isSelected = currentTab == 2,
                onClick = { onTabSelected(2) }
            )
            
            NavTab(
                icon = Icons.Outlined.History,
                selectedIcon = Icons.Filled.History,
                label = "History",
                isSelected = currentTab == 3,
                onClick = { onTabSelected(3) }
            )
            
            NavTab(
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings,
                label = "Settings",
                isSelected = currentTab == 4,
                onClick = { onTabSelected(4) }
            )
        }
    }
}

@Composable
private fun NavTab(
    icon: ImageVector,
    selectedIcon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val selectedColor = AppPalette.Sage700
    val unselectedColor = AppPalette.Sage900.copy(alpha = 0.6f)
    
    val color by animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor,
        label = "Tab Color"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        label = "Tab Scale"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No ripple for cleaner look
                onClick = onClick
            )
            .padding(8.dp)
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
private fun NavCenterButton(
    icon: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                brush = if (isSelected) GlassDesign.ActiveGradient else Brush.linearGradient(
                    colors = listOf(AppPalette.Sage500, AppPalette.Sage600)
                ),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else icon,
            contentDescription = "Center",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}
