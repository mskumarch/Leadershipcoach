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
    // Primary - Teal
    primary = AppPalette.Teal600,
    onPrimary = AppPalette.White,
    primaryContainer = AppPalette.Teal100,
    onPrimaryContainer = AppPalette.Teal700,

    // Secondary - Slate
    secondary = AppPalette.Slate700,
    onSecondary = AppPalette.White,
    secondaryContainer = AppPalette.Slate100,
    onSecondaryContainer = AppPalette.Slate900,

    // Tertiary - Blue
    tertiary = AppPalette.Blue500,
    onTertiary = AppPalette.White,
    tertiaryContainer = AppPalette.Blue500.copy(alpha = 0.1f),
    onTertiaryContainer = AppPalette.Blue500,

    // Background & Surface
    background = AppPalette.White,
    onBackground = AppPalette.Slate900,
    surface = AppPalette.White,
    onSurface = AppPalette.Slate900,
    surfaceVariant = AppPalette.Slate50,
    onSurfaceVariant = AppPalette.Slate700,

    // Error
    error = AppPalette.Red500,
    onError = AppPalette.White,
    errorContainer = AppPalette.Red500.copy(alpha = 0.1f),
    onErrorContainer = AppPalette.Red500,

    outline = AppPalette.Slate500,
    outlineVariant = AppPalette.Slate100
)

private val DarkColorScheme = darkColorScheme(
    primary = AppPalette.Teal600,
    onPrimary = AppPalette.White,
    primaryContainer = AppPalette.Teal700,
    onPrimaryContainer = AppPalette.Teal100,

    secondary = AppPalette.Slate500,
    onSecondary = AppPalette.White,
    secondaryContainer = AppPalette.Slate700,
    onSecondaryContainer = AppPalette.Slate100,

    background = AppPalette.Slate900,
    onBackground = AppPalette.White,
    surface = AppPalette.Slate900,
    onSurface = AppPalette.White,
    surfaceVariant = AppPalette.Slate700,
    onSurfaceVariant = AppPalette.Slate100,

    error = AppPalette.Red500,
    onError = AppPalette.White
)

@Composable
fun LeadershipConversationCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to enforce brand colors
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
        typography = AppTypography,
        content = content
    )
}

// Helper to get user bubble background based on theme
@Composable
fun getUserBubbleBackground(): Color {
    return if (isSystemInDarkTheme()) DarkUserBubbleBackground else UserBubbleBackground
}