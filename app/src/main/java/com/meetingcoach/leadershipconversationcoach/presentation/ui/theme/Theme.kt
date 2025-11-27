package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Leadership Conversation Coach - Main Theme
 *
 * Implements the "Clean Slate" design system using Material 3.
 */

private val LightColorScheme = lightColorScheme(
    // Primary - Modern Sage
    primary = AppPalette.Sage600,
    onPrimary = AppPalette.White,
    primaryContainer = AppPalette.Sage100,
    onPrimaryContainer = AppPalette.Sage600,

    // Secondary - Lavender
    secondary = AppPalette.Lavender500,
    onSecondary = AppPalette.White,
    secondaryContainer = AppPalette.Lavender100,
    onSecondaryContainer = AppPalette.Stone900,

    // Tertiary - Stone/Neutral
    tertiary = AppPalette.Stone500,
    onTertiary = AppPalette.White,
    tertiaryContainer = AppPalette.Stone100,
    onTertiaryContainer = AppPalette.Stone900,

    // Background & Surface
    background = AppPalette.Stone50,
    onBackground = AppPalette.Stone900,
    surface = AppPalette.White,
    onSurface = AppPalette.Stone900,
    surfaceVariant = AppPalette.Stone100,
    onSurfaceVariant = AppPalette.Stone700,

    // Error
    error = AppPalette.Red500,
    onError = AppPalette.White,
    errorContainer = AppPalette.Red500.copy(alpha = 0.1f),
    onErrorContainer = AppPalette.Red500,

    outline = AppPalette.Stone500,
    outlineVariant = AppPalette.Stone100
)

private val DarkColorScheme = darkColorScheme(
    primary = AppPalette.Sage500,
    onPrimary = AppPalette.White,
    primaryContainer = AppPalette.Sage600,
    onPrimaryContainer = AppPalette.Sage100,

    secondary = AppPalette.Lavender500,
    onSecondary = AppPalette.White,
    secondaryContainer = AppPalette.Stone700,
    onSecondaryContainer = AppPalette.Lavender100,

    background = DarkBackground,
    onBackground = AppPalette.White,
    surface = DarkBackground,
    onSurface = AppPalette.White,
    surfaceVariant = AppPalette.Stone700,
    onSurfaceVariant = AppPalette.Stone100,

    error = AppPalette.Red500,
    onError = AppPalette.White
)

@Composable
fun LeadershipConversationCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to enforce brand colors
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb() // Transparent for edge-to-edge
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography.scale(fontScale),
        content = content
    )
}

// Helper to get user bubble background based on theme
@Composable
fun getUserBubbleBackground(): Color {
    return if (isSystemInDarkTheme()) DarkUserBubbleBackground else UserBubbleBackground
}