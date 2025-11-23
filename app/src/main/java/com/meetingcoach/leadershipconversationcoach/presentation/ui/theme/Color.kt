package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Premium Design System - Depth-Rich Color Palette
 * 
 * Personal growth sanctuary aesthetic with warm, intimate colors
 */

// ============================================================
// PRIMARY SURFACES - Sage & Taupe (60% coverage)
// ============================================================

val SageGreen = Color(0xFF8FA894)              // Primary surface - warm, grounding
val WarmTaupe = Color(0xFFC6A884)              // Secondary surface - elevated cards
val SoftCream = Color(0xFFF7F8F6)              // Text and highlights on dark surfaces
val DeepCharcoal = Color(0xFF2E2E2E)           // Text on light surfaces

// ============================================================
// ACCENT COLORS - Active & Interactive
// ============================================================

val ActiveBlue = Color(0xFF4285F4)             // CTAs and user messages
val ActiveBlueLight = Color(0xFF5294FF)        // Gradient end for user messages
val MutedCoral = Color(0xFFE57373)             // Error/Stop states
val NeutralGray = Color(0xFF6B7280)            // Inactive icons and secondary text

// ============================================================
// GLASSMORPHISM - Translucent Layers
// ============================================================

val GlassWhite = Color(0xB3FFFFFF)             // 70% white for glass surfaces
val GlassWhiteStrong = Color(0xF2FFFFFF)       // 95% white for strong glass
val GlassTaupe = Color(0xF2C6A884)             // 95% taupe for AI cards
val GlassCream = Color(0xE6F7F8F6)             // 90% cream for backgrounds

// Border colors for glass
val GlassBorderLight = Color(0x4DFFFFFF)       // 30% white border
val GlassBorderMedium = Color(0x33FFFFFF)      // 20% white border
val GlassBorderSage = Color(0x338FA894)        // 20% sage border

// ============================================================
// SENTIMENT & STATUS COLORS
// ============================================================

val SentimentPositive = Color(0xFF4CAF50)      // Green for positive sentiment
val SentimentNeutral = Color(0xFFFFA726)       // Amber for neutral
val SentimentNegative = Color(0xFFE57373)      // Coral for negative

// Tone check icons
val ToneSuccess = Color(0xFF4CAF50)            // Green circle with checkmark
val ToneWarning = Color(0xFFFFA726)            // Amber triangle
val ToneTip = Color(0xFF4285F4)                // Blue lightbulb

// ============================================================
// SHADOW COLORS - Layered Depth
// ============================================================

val ShadowLight = Color(0x1F000000)            // 12% black for light shadows
val ShadowMedium = Color(0x14000000)           // 8% black for medium shadows
val ShadowDeep = Color(0x0A000000)             // 4% black for subtle shadows
val ShadowSage = Color(0x268FA894)             // 15% sage for colored shadows
val ShadowBlue = Color(0x4D4285F4)             // 30% blue for user message shadows
val ShadowCoral = Color(0x26E57373)            // 15% coral for recording shadows

// ============================================================
// GRADIENT DEFINITIONS
// ============================================================

// User message gradient
val UserMessageGradientStart = ActiveBlue
val UserMessageGradientEnd = ActiveBlueLight

// Card gradients
val CardGradientTop = Color(0xF2FFFFFF)        // 95% white
val CardGradientBottom = Color(0xE6F7F8F6)     // 90% cream

// Background gradients
val BackgroundRadialCenter = Color(0x1AFFFFFF) // 10% white overlay
val BackgroundRadialEdge = Color(0x00FFFFFF)   // Transparent

// ============================================================
// OVERLAY & TEXTURE
// ============================================================

val NoiseOverlay = Color(0x08000000)           // 3% black for texture
val InnerGlowLight = Color(0x4DFFFFFF)         // 30% white for inner glow
val InnerGlowSage = Color(0x268FA894)          // 15% sage for halos

// ============================================================
// DARK MODE VARIANTS
// ============================================================

val DarkSageGreen = Color(0xFF6B8070)          // Darker sage for dark mode
val DarkWarmTaupe = Color(0xFF9B8468)          // Darker taupe
val DarkBackground = Color(0xFF1A1F1E)         // Deep green-black
val DarkSurface = Color(0xFF252B2A)            // Elevated dark surface

// ============================================================
// LEGACY COLORS (Keep for compatibility)
// ============================================================

// Keep existing colors for components not yet updated
val Primary = SageGreen                         // Map to new sage
val Secondary = WarmTaupe                       // Map to new taupe
val Background = SoftCream                      // Map to new cream
val Surface = Color(0xFFFFFFFE)                // Pure white
val OnPrimary = SoftCream                      // Cream on sage
val OnSecondary = DeepCharcoal                 // Charcoal on taupe
val OnBackground = DeepCharcoal                // Charcoal on cream
val OnSurface = DeepCharcoal                   // Charcoal on white

// Semantic colors
val Success = SentimentPositive
val Warning = SentimentNeutral
val Error = MutedCoral
val Info = ActiveBlue

// Text colors
val TextPrimary = DeepCharcoal
val TextSecondary = NeutralGray
val TextTertiary = Color(0xFF9CA3AF)
val TextDisabled = Color(0xFFCBD5E0)

// Glossy colors (for bottom nav)
val GlossyPrimaryStart = Color(0xFF80CBC4)
val GlossyPrimaryEnd = Color(0xFF26A69A)
val GlossySecondaryStart = Color(0xFFB39DDB)
val GlossySecondaryEnd = Color(0xFF7E57C2)
val GlossyHighlight = Color(0x50FFFFFF)
val GlossyShadow = Color(0x40000000)
val GlossyNavBarStart = Color(0xFFE0F2F1)
val GlossyNavBarEnd = Color(0xFFF3E5F5)

// Modern UI accents (keep for compatibility)
val AccentCoral = Color(0xFFFF9B71)
val AccentPeach = Color(0xFFFFB4A2)
val AccentLavender = Color(0xFFB39DDB)
val AccentMint = Color(0xFF80CBC4)
val AccentRose = Color(0xFFFF7B9C)
val AccentSky = Color(0xFF90CAF9)

val GradientCoralRose = listOf(AccentCoral, AccentRose)
val GradientLavenderSky = listOf(AccentLavender, AccentSky)
val GradientMintTeal = listOf(AccentMint, Primary)
val GradientPeachCoral = listOf(AccentPeach, AccentCoral)

val RecordingPulse = Color(0xFF9575CD)
val RecordingWave = Color(0xFFB39DDB)
val RecordingActive = AccentCoral

// Dark theme colors
val DarkPrimary = Color(0xFF80CBC4)
val DarkSecondary = Color(0xFFB39DDB)
val DarkOnPrimary = Color(0xFF003D33)
val DarkOnSecondary = Color(0xFF4A148C)
val DarkOnBackground = Color(0xFFE2E8F0)
val DarkOnSurface = Color(0xFFE2E8F0)

val DarkTextPrimary = Color(0xFFE2E8F0)
val DarkTextSecondary = Color(0xFFA0AEC0)
val DarkTextTertiary = Color(0xFF718096)

val DarkSurfaceVariant = Color(0xFF374151)
val DarkOutline = Color(0xFF4B5563)

val ShadowColor = Color(0x1A000000)
val DarkShadowColor = Color(0x33000000)

// Missing colors for Theme.kt
val BorderMedium = NeutralGray
val BorderLight = Color(0xFFD1D5DB)
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
val SurfaceVariant = Color(0xFFE0D4C4)  // Light taupe
val AIBubbleText = DeepCharcoal