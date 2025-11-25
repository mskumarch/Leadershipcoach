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

// PRIMARY SURFACES - Serene Mist Theme (Relaxing & Calm)
val SageGreen = Color(0xFFA0C1B8)              // Soft Eucalyptus/Teal - Calming primary
val WarmTaupe = Color(0xFFE8E4D9)              // Sand Beige - Warm secondary
val SoftCream = Color(0xFFF5F7F8)              // Mist White - Light airy background
val DeepCharcoal = Color(0xFF374151)           // Soft Charcoal - Readable text

// DARK MODE PALETTE - Deep Sanctuary
val DarkSageGreen = Color(0xFF2D443E)          // Deep Teal
val DarkWarmTaupe = Color(0xFF4A453A)          // Deep Sand
val DarkBackground = Color(0xFF111827)         // Deep Night Blue/Gray
val DarkSurface = Color(0xFF1F2937)            // Dark Surface
val DarkTextPrimary = Color(0xFFF3F4F6)        // Off-white text
val DarkTextSecondary = Color(0xFF9CA3AF)      // Muted text

// ACCENT COLORS - Nature Inspired
val ActiveBlue = Color(0xFF6B8E9B)             // Ocean Blue - Focus/Action
val ActiveBlueLight = Color(0xFF8FB3C0)        // Lighter Ocean Blue
val MutedCoral = Color(0xFFE0AFA0)             // Soft Coral - Alerts/Recording
val SuccessGreen = Color(0xFF86A873)           // Leaf Green - Success
val WarningYellow = Color(0xFFE6C785)          // Sun Yellow - Warning
val Success = SuccessGreen                     // Alias for backward compatibility
val Warning = WarningYellow                    // Alias for backward compatibility
val Info = Color(0xFF64B5F6)                   // Info / Tips
val NeutralGray = Color(0xFF6B7280)            // Secondary Text / Inactive Icons

// GLASSMORPHISM - Translucent Layers
val GlassWhite = Color(0xB3FFFFFF)             // 70% white for glass surfaces
val GlassTaupe = Color(0xF2E8E4D9)             // 95% Sand Beige (New Taupe)
val GlassBorderLight = Color(0x4DFFFFFF)       // 30% white border
val NavGlassBase = Color(0xB3FFFFFF)           // 70% white
val NavGlassTint = Color(0x1AA0C1B8)           // 10% Soft Teal (New Sage)
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
// val DarkTextPrimary = SoftCream // Removed duplicate
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
// val DarkTextSecondary = NeutralGray // Removed duplicate

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