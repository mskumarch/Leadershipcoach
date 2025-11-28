package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Leadership Coach Design System - Color Palette
 * Theme: Clean Slate (Professional, Airy, Trustworthy)
 *
 * This file serves as the single source of truth for all colors.
 * Use AppPalette for defining new semantic colors.
 */

object AppPalette {
    // Brand Colors - Modern Sage & Lavender
    val Sage600 = Color(0xFF4A7A68) // Primary Brand Color (Deep Sage)
    val Sage500 = Color(0xFF5B9A8B) // Primary Light
    val Sage400 = Color(0xFF7CB3A6) // Lighter Sage
    val Sage200 = Color(0xFFC5E0D8) // Very Light Sage
    val Sage100 = Color(0xFFE8F3EF) // Light Sage Background

    val Lavender500 = Color(0xFF8B80F9) // Modern Lavender (Secondary)
    val Lavender100 = Color(0xFFF3F0FF) // Light Lavender

    // Neutral Colors (Warm Sand/Stone)
    val Stone900 = Color(0xFF1C1917) // Primary Text (Warm Black)
    val Stone700 = Color(0xFF44403C) // Secondary Text
    val Stone500 = Color(0xFF78716C) // Disabled / Hint
    val Stone300 = Color(0xFFD6D3D1) // Medium Stone
    val Stone200 = Color(0xFFE7E5E4) // Light Stone
    val Stone100 = Color(0xFFF5F5F4) // Surface Variant / Borders
    val Stone50 = Color(0xFFFAFAF9)  // Background Alternative (Warm Off-White)
    val White = Color(0xFFFFFFFF)    // Pure White

    // Semantic Colors
    val Red500 = Color(0xFFEF4444)   // Error / Stop / Recording
    val Amber500 = Color(0xFFF59E0B) // Warning
    val Emerald500 = Color(0xFF10B981) // Success
    val Blue500 = Color(0xFF3B82F6)  // Info / Action
}

// ============================================================
// COMPATIBILITY LAYER (Do not modify unless refactoring usage)
// ============================================================

// Primary Theme Aliases
val SageGreen = AppPalette.Sage600
val WarmTaupe = AppPalette.Stone100
val SoftCream = AppPalette.White
val DeepCharcoal = AppPalette.Stone900

val Primary = AppPalette.Sage600
val Secondary = AppPalette.Stone700

// Dark Mode (Mapped to Stone/Warm Dark)
val DarkSageGreen = AppPalette.Sage600
val DarkWarmTaupe = Color(0xFF292524) // Stone 800
val DarkBackground = Color(0xFF1C1917) // Stone 900
val DarkTextPrimary = AppPalette.White
val DarkTextSecondary = AppPalette.Stone500

// Accents
val ActiveBlue = AppPalette.Blue500
val ActiveBlueLight = Color(0xFF60A5FA)
val MutedCoral = AppPalette.Red500
val SuccessGreen = AppPalette.Emerald500
val WarningYellow = AppPalette.Amber500

// Backward Compatibility Aliases
val Success = SuccessGreen
val Warning = WarningYellow
val Info = ActiveBlue
val NeutralGray = AppPalette.Stone500

// Glassmorphism & Shadows
val GlassWhite = AppPalette.White.copy(alpha = 0.95f)
val GlassTaupe = AppPalette.Stone100.copy(alpha = 0.9f)
val GlassBorderLight = Color(0xFFFFFFFF).copy(alpha = 0.6f)
val NavGlassBase = AppPalette.White.copy(alpha = 0.9f)
val NavGlassTint = AppPalette.Sage100.copy(alpha = 0.3f)
val NavGlassHighlight = Color.White.copy(alpha = 0.5f)

val ShadowLight = Color(0x1A000000)
val ShadowMedium = Color(0x26000000)
val ShadowStrong = Color(0x33000000)
val ShadowSage = AppPalette.Sage600.copy(alpha = 0.2f)
val ShadowBlue = AppPalette.Lavender500.copy(alpha = 0.2f)

// Glossy / Premium Tokens
val GlossyPrimaryStart = AppPalette.Sage500
val GlossyPrimaryEnd = AppPalette.Sage600
val GlossySecondaryStart = AppPalette.Lavender500
val GlossySecondaryEnd = Color(0xFF7C3AED) // Violet 600
val GlossyHighlight = Color.White.copy(alpha = 0.3f)
val GlossyShadow = Color.Black.copy(alpha = 0.2f)

// Component Specific
val UserBubbleBackground = ActiveBlue
val DarkUserBubbleBackground = ActiveBlue

val AIBubbleBackground = AppPalette.Stone100
val AIBubbleText = AppPalette.Stone900

// Recording & Accents
val AccentCoral = AppPalette.Red500
val AccentLavender = AppPalette.Lavender500
val AccentMint = AppPalette.Sage100
val RecordingActive = AppPalette.Red500
val RecordingPulse = AppPalette.Red500.copy(alpha = 0.5f)
val RecordingWave = AppPalette.Red500.copy(alpha = 0.2f)
val GradientCoralRose = listOf(AppPalette.Red500, AppPalette.Amber500)
val CalmGreenStart = Color(0xFFF2FBF9) // Very Light Sage
val CalmGreenEnd = AppPalette.Sage100

// Effects
val InnerGlowLight = Color.White.copy(alpha = 0.2f)
val ShadowSubtle = Color.Black.copy(alpha = 0.05f)
val ShadowColor = Color.Black.copy(alpha = 0.1f)

// Text & Surface Tokens
val TextPrimary = AppPalette.Stone900
val TextTertiary = AppPalette.Stone500
val TextDisabled = AppPalette.Stone500.copy(alpha = 0.5f)
val SurfaceVariant = AppPalette.Stone100
val BorderLight = AppPalette.Stone100
val BorderMedium = AppPalette.Stone500