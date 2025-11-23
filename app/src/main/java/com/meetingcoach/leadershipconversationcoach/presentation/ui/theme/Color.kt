package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// USAGE GUIDE
// ============================================================
//
// PRIMARY SURFACES - Use for:
// - SageGreen: App canvas background, primary cards, nav active state
// - WarmTaupe: Elevated cards (AI responses), settings panels
// - SoftCream: Text on dark surfaces, card highlights
// - DeepCharcoal: All text on light surfaces
//
// GLASSMORPHISM - Use for:
// - GlassWhite: Bottom nav bar, floating headers
// - GlassTaupe: AI response cards with transparency
// - GlassBorderLight: Top/left edges of glass elements
//
// INTERACTIVE - Use for:
// - ActiveBlue: User message bubbles, CTAs, links
// - MutedCoral: Stop button, delete actions, errors
// - NeutralGray: Inactive icons, secondary text

// ============================================================
// PREMIUM COLOR PALETTE
// ============================================================

// PRIMARY SURFACES - Sage & Taupe (60% coverage)
val SageGreen = Color(0xFF8FA894)              // Primary surface - warm, grounding
val WarmTaupe = Color(0xFFC6A884)              // Secondary surface - elevated cards
val SoftCream = Color(0xFFF7F8F6)              // Text and highlights on dark surfaces
val DeepCharcoal = Color(0xFF2E2E2E)           // Text on light surfaces

// DARK MODE PALETTE
val DarkSageGreen = Color(0xFF6B8070)          // Darker sage for dark mode background
val DarkWarmTaupe = Color(0xFF9B8468)          // Darker taupe for dark mode surfaces
val DarkBackground = Color(0xFF1A1F1E)         // Deep sage-tinted black
val DarkSurface = Color(0xFF252B2A)            // Slightly lighter sage-tinted black

// ACCENT COLORS
val ActiveBlue = Color(0xFF4285F4)             // Primary Action / User Bubble
val ActiveBlueLight = Color(0xFF6EA0F8)        // Gradient End
val MutedCoral = Color(0xFFE57373)             // Error / Stop / Alert
val Success = Color(0xFF81C784)                // Success / Positive Sentiment
val Warning = Color(0xFFFFB74D)                // Warning / Neutral Sentiment
val Info = Color(0xFF64B5F6)                   // Info / Tips
val NeutralGray = Color(0xFF6B7280)            // Secondary Text / Inactive Icons

// GLASSMORPHISM - Translucent Layers
val GlassWhite = Color(0xB3FFFFFF)             // 70% white for glass surfaces
val GlassTaupe = Color(0xF2C6A884)             // 95% taupe for AI cards
val GlassBorderLight = Color(0x4DFFFFFF)       // 30% white border
val NavGlassBase = Color(0xB3FFFFFF)           // 70% white
val NavGlassTint = Color(0x1A8FA894)           // 10% sage tint
val NavGlassHighlight = Color(0x4DFFFFFF)      // 30% white edge highlight

// SHADOW COLORS - Layered Depth
val ShadowSubtle = Color(0x0A000000)           // 4% - barely visible
val ShadowMedium = Color(0x14000000)           // 8% - standard cards
val ShadowStrong = Color(0x1F000000)           // 12% - floating elements
val ShadowSage = Color(0x268FA894)             // 15% sage for colored shadows
val ShadowBlue = Color(0x334285F4)             // Blue shadow for active elements

// INTERACTIVE STATES
val FocusRing = SageGreen
val DisabledBackground = Color(0xFFE5E7EB)
val DisabledText = Color(0xFF9CA3AF)

// LOADING STATES
val SkeletonBase = Color(0xFFE5E7EB)
val SkeletonShimmer = Color(0xFFF3F4F6)

// NOTIFICATIONS
val ToastBackground = Color(0xF2FFFFFF)        // Glass white
val ToastBorder = GlassBorderLight

// GRADIENTS
val InnerGlowLight = Color(0x4DFFFFFF)         // 30% white
val SurfaceHighlight = Color(0x1AFFFFFF)       // 10% white

// ============================================================
// LEGACY COLORS (Deprecated - Do not use in new code)
// ============================================================
val Primary = SageGreen
val Secondary = WarmTaupe
val Background = SageGreen // Fixed to match design
val Surface = SoftCream
val Error = MutedCoral
val OnPrimary = SoftCream
val OnSecondary = DeepCharcoal
val OnBackground = SoftCream
val OnSurface = DeepCharcoal
val OnError = Color.White

// Kept for backward compatibility if needed, but should be replaced
val AccentCoral = MutedCoral
val AccentPeach = Warning
val AccentLavender = Color(0xFFB39DDB)
val AccentMint = Success
val AccentRose = Color(0xFFF48FB1)
val AccentSky = Info

// Dark Theme Colors (Legacy aliases for Theme.kt compatibility)
val DarkPrimary = DarkSageGreen
val DarkSecondary = DarkWarmTaupe
val DarkOnPrimary = SoftCream
val DarkOnSecondary = SoftCream
val DarkOnBackground = SoftCream
val DarkOnSurface = SoftCream

val TextPrimary = DeepCharcoal
val DarkTextPrimary = SoftCream
val DarkSurfaceVariant = DarkWarmTaupe
val DarkOutline = NeutralGray
val ShadowColor = ShadowMedium
val DarkShadowColor = ShadowStrong
val BorderMedium = NeutralGray
val BorderLight = GlassBorderLight
val Overlay = Color(0x80000000)

// Card backgrounds
val ContextCardBackground = WarmTaupe
val DarkContextCardBackground = DarkWarmTaupe
val PromptCardBackground = GlassTaupe
val DarkPromptCardBackground = Color(0xE69B8468)
val UserBubbleBackground = ActiveBlue
val DarkUserBubbleBackground = ActiveBlue
val AIBubbleBackground = WarmTaupe
val DarkAIBubbleBackground = DarkWarmTaupe

// Additional missing colors
val SurfaceVariant = WarmTaupe
val AIBubbleText = DeepCharcoal
val DarkTextSecondary = NeutralGray

// More Legacy Colors for Compatibility
val GlossyPrimaryStart = ActiveBlue
val GlossyPrimaryEnd = ActiveBlueLight
val GlossyHighlight = InnerGlowLight
val GlossyShadow = ShadowMedium

val RecordingActive = MutedCoral
val RecordingPulse = Color(0x4DE57373)
val RecordingWave = Color(0x26E57373)

val GradientCoralRose = listOf(MutedCoral, Color(0xFFF48FB1))

val TextTertiary = NeutralGray
val TextDisabled = DisabledText

val GlossySecondaryStart = WarmTaupe
val GlossySecondaryEnd = Color(0xFFD7BFA1)