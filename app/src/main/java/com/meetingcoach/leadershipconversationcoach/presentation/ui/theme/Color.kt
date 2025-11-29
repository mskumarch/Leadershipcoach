package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Leadership Coach Design System - Premium Mint/Sage Palette
 * EXACT colors extracted from reference mock.
 */

object AppPalette {

    // =============================
    // Brand Sage (Exact From Mock)
    // =============================
    val Sage900 = Color(0xFF0E1F16) // Text Dark
    val Sage700 = Color(0xFF2F6E5A) // Primary Buttons / Active
    val Sage600 = Color(0xFF3C7C65)
    val Sage500 = Color(0xFF4F8F77) // Mid Sage UI elements
    val Sage400 = Color(0xFF7CB6A2)
    val Sage300 = Color(0xFFA9D4C6) // Added for compatibility
    val Sage200 = Color(0xFFD5F3EA)
    val Sage100 = Color(0xFFE9F3EE) // Card tint / soft mint
    val Sage50  = Color(0xFFEEF7F3) // Top gradient
    val Sage25  = Color(0xFFC9DED6) // Bottom gradient

    // =============================
    // Lavender (From Mock)
    // =============================
    val Lavender500 = Color(0xFFC9B9F8)
    val Lavender100 = Color(0xFFF4F1FF)

    // =============================
    // Neutrals (Sampled from mock)
    // =============================
    val Stone900 = Color(0xFF0E1F16)
    val Stone700 = Color(0xFF51655B)
    val Stone500 = Color(0xFF7E8E85)
    val Stone300 = Color(0xFFD4D7D5)
    val Stone200 = Color(0xFFE8E9E8)
    val Stone100 = Color(0xFFF5F5F5)
    val Stone50 = Color(0xFFF9FBFA)
    val White = Color(0xFFFFFFFF)

    // =============================
    // Semantic (unchanged)
    // =============================
    val Red500 = Color(0xFFEF4444)
    val Amber500 = Color(0xFFF4D96F) // exact soft gold from mock
    val Emerald500 = Color(0xFF5BA88A)
    val Blue500 = Color(0xFFA4C9E8)
}

// ============================================================
// Backward compatibility (do not modify logic)
// ============================================================

val SageGreen = AppPalette.Sage700
val WarmTaupe = AppPalette.Stone100
val SoftCream = AppPalette.White
val DeepCharcoal = AppPalette.Stone900

val Primary = AppPalette.Sage700
val Secondary = AppPalette.Stone700

val DarkSageGreen = AppPalette.Sage600
val DarkWarmTaupe = Color(0xFF292524)
val DarkBackground = Color(0xFF111512)
val DarkTextPrimary = AppPalette.White
val DarkTextSecondary = AppPalette.Stone500

val ActiveBlue = AppPalette.Blue500
val ActiveBlueLight = Color(0xFFA4C9E8)
val MutedCoral = AppPalette.Red500
val SuccessGreen = AppPalette.Emerald500
val WarningYellow = AppPalette.Amber500

val Success = SuccessGreen
val Warning = WarningYellow
val Info = ActiveBlue
val NeutralGray = AppPalette.Stone500

// Glassmorphism & Shadows
val GlassWhite = AppPalette.White.copy(alpha = 0.95f)
val GlassTaupe = AppPalette.Sage100.copy(alpha = 0.60f)
val GlassBorderLight = Color.White.copy(alpha = 0.40f)

val NavGlassBase = AppPalette.White.copy(alpha = 0.90f)
val NavGlassTint = AppPalette.Sage100.copy(alpha = 0.30f)
val NavGlassHighlight = Color.White.copy(alpha = 0.40f)

val ShadowLight = Color(0x1A000000)
val ShadowMedium = Color(0x26000000)
val ShadowStrong = Color(0x33000000)
val ShadowSage = AppPalette.Sage600.copy(alpha = 0.15f)
val ShadowBlue = AppPalette.Lavender500.copy(alpha = 0.20f)

// Glossy / Premium
val GlossyPrimaryStart = AppPalette.Sage500
val GlossyPrimaryEnd = AppPalette.Sage700
val GlossySecondaryStart = AppPalette.Lavender500
val GlossySecondaryEnd = Color(0xFF9F8CF1)
val GlossyHighlight = Color.White.copy(alpha = 0.30f)
val GlossyShadow = Color.Black.copy(alpha = 0.15f)

// Component Colors
val UserBubbleBackground = ActiveBlue
val DarkUserBubbleBackground = ActiveBlue

val AIBubbleBackground = AppPalette.Sage100
val AIBubbleText = AppPalette.Sage900

// Recording & Gradients
val AccentCoral = AppPalette.Red500
val AccentLavender = AppPalette.Lavender500
val AccentMint = AppPalette.Sage100
val RecordingActive = AppPalette.Red500
val RecordingPulse = AppPalette.Red500.copy(alpha = 0.5f)
val RecordingWave = AppPalette.Red500.copy(alpha = 0.2f)

val GradientCoralRose = listOf(AppPalette.Red500, AppPalette.Amber500)

// Premium mint background (exact stops)
val CalmGreenStart = AppPalette.Sage50
val CalmGreenEnd = AppPalette.Sage25

// Effects
val InnerGlowLight = Color.White.copy(alpha = 0.20f)
val ShadowSubtle = Color.Black.copy(alpha = 0.05f)
val ShadowColor = Color.Black.copy(alpha = 0.10f)

// Text tokens
val TextPrimary = AppPalette.Sage900
val TextTertiary = AppPalette.Stone500
val TextDisabled = AppPalette.Stone500.copy(alpha = 0.5f)
val SurfaceVariant = AppPalette.Sage100
val BorderLight = AppPalette.Sage100
val BorderMedium = AppPalette.Stone500