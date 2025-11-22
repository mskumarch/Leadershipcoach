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
 * Comprehensive Material 3 theme with support for:
 * - Light and dark modes
 * - Dynamic colors (Android 12+)
 * - Custom color schemes
 * - System UI appearance
 */

// ============================================================
// LIGHT COLOR SCHEME
// ============================================================

private val LightColorScheme = lightColorScheme(
    // Primary colors - Calm Sage Green
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = Color(0xFFD4E4DA),     // Light sage
    onPrimaryContainer = Color(0xFF3E5247),   // Dark sage

    // Secondary colors - Soft Lavender
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Color(0xFFE0DEEF),   // Light lavender
    onSecondaryContainer = Color(0xFF4A4662), // Dark lavender

    // Tertiary colors (using soft blue)
    tertiary = Info,
    onTertiary = OnPrimary,
    tertiaryContainer = Color(0xFFD9E7F1),
    onTertiaryContainer = Color(0xFF3A4E5C),

    // Error colors - Soft Coral
    error = Error,
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF5E5E5),       // Light coral
    onErrorContainer = Color(0xFF6B4545),     // Dark coral

    // Background - Warm Neutrals
    background = Background,
    onBackground = OnBackground,

    // Surface
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,

    // Other surface tones
    inverseSurface = TextPrimary,
    inverseOnSurface = Surface,

    // Outline
    outline = BorderMedium,
    outlineVariant = BorderLight,

    // Scrim (overlay)
    scrim = Overlay,

    // Surface tints
    surfaceTint = Primary
)

// ============================================================
// DARK COLOR SCHEME
// ============================================================

private val DarkColorScheme = darkColorScheme(
    // Primary colors - Calm Sage Green
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = Color(0xFF3E5247),     // Dark sage container
    onPrimaryContainer = Color(0xFFD4E4DA),   // Light sage

    // Secondary colors - Soft Lavender
    secondary = Secondary,
    onSecondary = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF4A4662),   // Dark lavender
    onSecondaryContainer = Color(0xFFE0DEEF), // Light lavender

    // Tertiary colors - Soft Blue
    tertiary = Info,
    onTertiary = Color(0xFF1A1A1A),
    tertiaryContainer = Color(0xFF3A4E5C),
    onTertiaryContainer = Color(0xFFD9E7F1),

    // Error colors - Soft Coral
    error = Error,
    onError = Color(0xFF1A1A1A),
    errorContainer = Color(0xFF6B4545),       // Dark coral
    onErrorContainer = Color(0xFFF5E5E5),     // Light coral

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