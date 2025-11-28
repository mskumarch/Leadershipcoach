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
    // ======================================================
    // 1. PRIMARY COLORS (Exact from mock)
    // ======================================================
    primary = AppPalette.Sage700,              // active buttons, score ring
    onPrimary = AppPalette.White,
    primaryContainer = AppPalette.Sage100,     // soft mint card tint
    onPrimaryContainer = AppPalette.Sage900,

    // ======================================================
    // 2. SECONDARY (lavender highlights)
    // ======================================================
    secondary = AppPalette.Lavender500,
    onSecondary = AppPalette.White,
    secondaryContainer = AppPalette.Lavender100,
    onSecondaryContainer = AppPalette.Sage900,

    // ======================================================
    // 3. TERTIARY / NEUTRALS (text + subtle icons)
    // ======================================================
    tertiary = AppPalette.Stone500,
    onTertiary = AppPalette.White,
    tertiaryContainer = AppPalette.Stone100,
    onTertiaryContainer = AppPalette.Stone900,

    // ======================================================
    // 4. BACKGROUND & SURFACE (Exact from mock)
    // ======================================================
    background = AppPalette.Sage50,       // gradient top color
    onBackground = AppPalette.Sage900,

    surface = AppPalette.White,           // cards look pure white
    onSurface = AppPalette.Sage900,

    surfaceVariant = AppPalette.Sage100,  // card tint (mint-cream)
    onSurfaceVariant = AppPalette.Sage700,

    // ======================================================
    // 5. ERROR
    // ======================================================
    error = AppPalette.Red500,
    onError = AppPalette.White,
    errorContainer = AppPalette.Red500.copy(alpha = 0.10f),
    onErrorContainer = AppPalette.Red500,

    // ======================================================
    // 6. OUTLINES
    // ======================================================
    outline = AppPalette.Stone300,
    outlineVariant = AppPalette.Sage100,
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
    dynamicColor: Boolean = false, // brand-enforced colors
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) 
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()  // edge-to-edge
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography.scale(fontScale),
        content = content
    )
}

@Composable
fun getUserBubbleBackground(): Color {
    return if (isSystemInDarkTheme()) DarkUserBubbleBackground else UserBubbleBackground
}