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
 * Comprehensive Material 3 theme with sea green and lavender colors:
 * - Light and dark modes with sea green and lavender accents
 * - Dynamic colors (Android 12+)
 * - Custom sea green and lavender color schemes
 * - System UI appearance with themed status bar
 */

// ============================================================
// LIGHT COLOR SCHEME
// ============================================================

private val LightColorScheme = lightColorScheme(
    // Primary colors - Sage Green (Primary Surface)
    primary = SageGreen,
    onPrimary = SoftCream,
    primaryContainer = Color(0xFFCDE0DB),     // Light Soft Teal
    onPrimaryContainer = DeepCharcoal,

    // Secondary colors - Warm Taupe
    secondary = WarmTaupe,
    onSecondary = DeepCharcoal,
    secondaryContainer = Color(0xFFF2F0EA),   // Light Sand Beige
    onSecondaryContainer = DeepCharcoal,

    // Tertiary - Active Blue
    tertiary = ActiveBlue,
    onTertiary = Color.White,

    // Background - Sage Green (60% coverage)
    // Background - Clean White
    background = SoftCream,
    onBackground = DeepCharcoal,

    // Surface - Soft Cream
    surface = SoftCream,
    onSurface = DeepCharcoal,
    surfaceVariant = WarmTaupe,
    onSurfaceVariant = DeepCharcoal,

    // Other surface tones
    inverseSurface = DeepCharcoal,
    inverseOnSurface = SoftCream,

    // Outline
    outline = NeutralGray,
    outlineVariant = Color(0xFFD1D5DB),

    // Scrim (overlay)
    scrim = Color(0x80000000),

    // Surface tints
    surfaceTint = SageGreen
)

// ============================================================
// DARK COLOR SCHEME
// ============================================================

private val DarkColorScheme = darkColorScheme(
    // Primary colors - Sea Green
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = Color(0xFF00695C),     // Dark sea green container
    onPrimaryContainer = Color(0xFFB2DFDB),   // Light sea green

    // Secondary colors - Lavender
    secondary = Secondary,
    onSecondary = Color(0xFF1A202C),
    secondaryContainer = Color(0xFF512DA8),   // Dark lavender
    onSecondaryContainer = Color(0xFFD1C4E9), // Light lavender

    // Tertiary colors - Sky Blue
    tertiary = Info,
    onTertiary = Color(0xFF1A202C),
    tertiaryContainer = Color(0xFF0D47A1),
    onTertiaryContainer = Color(0xFFBBDEFB),

    // Error colors - Coral
    error = Error,
    onError = Color(0xFF1A202C),
    errorContainer = Color(0xFFC62828),       // Dark coral
    onErrorContainer = Color(0xFFFFCDD2),     // Light coral

    // Background - Calm Night
    background = DarkBackground,
    onBackground = DarkOnBackground,

    // Surface
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,

    // Other surface tones
    inverseSurface = Surface,
    inverseOnSurface = TextPrimary,

    // Outline
    outline = BorderMedium,
    outlineVariant = BorderLight,

    // Scrim (overlay)
    scrim = Overlay,

    // Surface tints
    surfaceTint = DarkPrimary
)

// ============================================================
// MAIN THEME COMPOSABLE
// ============================================================

/**
 * Main theme for the Leadership Conversation Coach app
 *
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param dynamicColor Whether to use dynamic colors on Android 12+ (defaults to false)
 * @param content The content to be themed
 */
@Composable
fun LeadershipConversationCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to true to enable dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic color is available on Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        // Use dark theme
        darkTheme -> DarkColorScheme

        // Use light theme (default)
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

// ============================================================
// THEME UTILITIES
// ============================================================

/**
 * Extension property to check if current theme is dark
 */
val MaterialTheme.isDark: Boolean
    @Composable
    get() = isSystemInDarkTheme()

/**
 * Get appropriate color based on theme
 */
@Composable
fun getThemedColor(
    lightColor: Color,
    darkColor: Color
): Color {
    return if (isSystemInDarkTheme()) darkColor else lightColor
}

/**
 * Get coaching card background color based on theme
 */
@Composable
fun getContextCardBackground(): Color {
    return if (isSystemInDarkTheme()) DarkContextCardBackground else ContextCardBackground
}

@Composable
fun getPromptCardBackground(): Color {
    return if (isSystemInDarkTheme()) DarkPromptCardBackground else PromptCardBackground
}

@Composable
fun getUserBubbleBackground(): Color {
    return if (isSystemInDarkTheme()) DarkUserBubbleBackground else UserBubbleBackground
}

@Composable
fun getAIBubbleBackground(): Color {
    return if (isSystemInDarkTheme()) DarkAIBubbleBackground else AIBubbleBackground
}

/**
 * Preview theme for Compose previews
 */
@Composable
fun PreviewTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    LeadershipConversationCoachTheme(
        darkTheme = darkTheme,
        dynamicColor = false,
        content = content
    )
}