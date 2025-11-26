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
    // Brand Colors
    val Teal600 = Color(0xFF0D9488) // Primary Brand Color
    val Teal700 = Color(0xFF0F766E) // Darker Teal for states
    val Teal100 = Color(0xFFCCFBF1) // Light Teal for backgrounds

    // Neutral Colors (Slate)
    val Slate900 = Color(0xFF0F172A) // Primary Text
    val Slate700 = Color(0xFF334155) // Secondary Text
    val Slate500 = Color(0xFF64748B) // Disabled / Hint
    val Slate100 = Color(0xFFF1F5F9) // Surface Variant / Borders
    val Slate50 = Color(0xFFF8FAFC)  // Background Alternative
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
val SageGreen = AppPalette.Teal600
val WarmTaupe = AppPalette.Slate100
val SoftCream = AppPalette.White
val DeepCharcoal = AppPalette.Slate900

val Primary = AppPalette.Teal600
val Secondary = AppPalette.Slate700

// Dark Mode (Mapped to Slate for now, can be expanded)
val DarkSageGreen = AppPalette.Teal700
val DarkWarmTaupe = Color(0xFF1E293B)
val DarkBackground = Color(0xFF0F172A)
val DarkTextPrimary = AppPalette.White
val DarkTextSecondary = AppPalette.Slate500

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
val NeutralGray = AppPalette.Slate500

// Glassmorphism & Shadows
val GlassWhite = AppPalette.White.copy(alpha = 0.95f)
val GlassTaupe = AppPalette.Slate100.copy(alpha = 0.9f)
val GlassBorderLight = Color(0xFFFFFFFF).copy(alpha = 0.6f)
val NavGlassBase = AppPalette.White.copy(alpha = 0.9f)
val NavGlassTint = AppPalette.Teal100.copy(alpha = 0.1f)
val NavGlassHighlight = Color.White.copy(alpha = 0.5f)

val ShadowLight = Color(0x1A000000)
val ShadowMedium = Color(0x26000000)
val ShadowStrong = Color(0x33000000)
val ShadowSage = AppPalette.Teal600.copy(alpha = 0.2f)
val ShadowBlue = AppPalette.Blue500.copy(alpha = 0.2f)

// Glossy / Premium Tokens (Restored for compatibility)
val GlossyPrimaryStart = AppPalette.Teal600
val GlossyPrimaryEnd = AppPalette.Teal700
val GlossySecondaryStart = AppPalette.Slate700
val GlossySecondaryEnd = AppPalette.Slate900
val GlossyHighlight = Color.White.copy(alpha = 0.3f)
val GlossyShadow = Color.Black.copy(alpha = 0.2f)

// Component Specific
val UserBubbleBackground = ActiveBlue
val DarkUserBubbleBackground = ActiveBlue

val AIBubbleBackground = AppPalette.Slate100
val AIBubbleText = AppPalette.Slate900

// Recording & Accents
val AccentCoral = AppPalette.Red500
val AccentLavender = AppPalette.Blue500
val AccentMint = AppPalette.Teal100
val RecordingActive = AppPalette.Red500
val RecordingPulse = AppPalette.Red500.copy(alpha = 0.5f)
val RecordingWave = AppPalette.Red500.copy(alpha = 0.2f)
val GradientCoralRose = listOf(AppPalette.Red500, AppPalette.Amber500)
val CalmGreenStart = Color(0xFFE6FFFA) // Teal 50
val CalmGreenEnd = Color(0xFFCCFBF1)   // Teal 100

// Effects
val InnerGlowLight = Color.White.copy(alpha = 0.2f)
val ShadowSubtle = Color.Black.copy(alpha = 0.05f)
val ShadowColor = Color.Black.copy(alpha = 0.1f)

// Text & Surface Tokens
val TextPrimary = AppPalette.Slate900
val TextTertiary = AppPalette.Slate500
val TextDisabled = AppPalette.Slate500.copy(alpha = 0.5f)
val SurfaceVariant = AppPalette.Slate100
val BorderLight = AppPalette.Slate100
val BorderMedium = AppPalette.Slate500