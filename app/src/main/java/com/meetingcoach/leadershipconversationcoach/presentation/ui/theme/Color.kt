package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Leadership Conversation Coach - Calm & Soothing Color System
 *
 * Redesigned with calming colors inspired by nature and wellness
 * Features soft blues, lavenders, sage greens, and warm neutrals
 */

// ============================================================
// PRIMARY COLORS - Calm Sage Green
// ============================================================

val Primary = Color(0xFF8FA998)        // Soft sage green - Main brand color
val PrimaryVariant = Color(0xFF6B8E7F) // Deeper sage for emphasis
val OnPrimary = Color(0xFFFFFFFF)      // White text on primary

// ============================================================
// SECONDARY COLORS - Soft Lavender
// ============================================================

val Secondary = Color(0xFFA8A4CE)      // Soft lavender - Secondary actions
val SecondaryVariant = Color(0xFF8B86B8) // Deeper lavender
val OnSecondary = Color(0xFFFFFFFF)    // White text on secondary

// ============================================================
// SEMANTIC COLORS (Status & Actions) - Muted Tones
// ============================================================

val Success = Color(0xFF7FB685)        // Muted green - Success states
val Warning = Color(0xFFE8C07D)        // Soft amber - Warning states
val Error = Color(0xFFD89B9B)          // Soft coral - Error states
val Info = Color(0xFF8EACC8)           // Soft blue - Info states

// ============================================================
// BACKGROUND COLORS - Warm Neutrals
// ============================================================

val Background = Color(0xFFF5F3F0)     // Warm off-white - Main background
val BackgroundVariant = Color(0xFFEBE8E3) // Slightly warmer gray
val Surface = Color(0xFFFFFFFB)        // Soft white - Cards & surfaces
val SurfaceVariant = Color(0xFFF0EDE8) // Warm light gray - Alternate surfaces
val OnBackground = Color(0xFF3E3E3E)   // Soft dark gray text on background
val OnSurface = Color(0xFF3E3E3E)      // Soft dark gray text on surface

// ============================================================
// TEXT COLORS - Soft Grays
// ============================================================

val TextPrimary = Color(0xFF3E3E3E)    // Soft dark gray - Primary text
val TextSecondary = Color(0xFF7A7A7A)  // Medium gray - Secondary text
val TextTertiary = Color(0xFFA8A8A8)   // Light gray - Tertiary text
val TextDisabled = Color(0xFFD0D0D0)   // Very light gray - Disabled text

// ============================================================
// CARD & COMPONENT BACKGROUNDS - Calming Tones
// ============================================================

val ContextCardBackground = Color(0xFFE8E5E0)    // Warm gray - Context info cards
val PromptCardBackground = Color(0xFFFFF8E7)     // Soft cream - Reflection prompts
val UrgentCardBorder = Color(0xFFD89B9B)         // Soft coral - Urgent nudges border
val TipCardBorder = Color(0xFF8EACC8)            // Soft blue - Helpful tips border

// ============================================================
// TEMPERATURE GAUGE COLORS - Gentle Gradients
// ============================================================

val TempGreen = Color(0xFF7FB685)      // Calm (0-33%)
val TempYellow = Color(0xFFE8C07D)     // Tense (34-66%)
val TempRed = Color(0xFFD89B9B)        // Stressed (67-100%)

// ============================================================
// PROGRESS RING COLORS - Harmonious Pastels
// ============================================================

val EmpathyRing = Color(0xFF7FB685)    // Soft green - Empathy score
val ListeningRing = Color(0xFF8EACC8)  // Soft blue - Listening ratio
val ClarityRing = Color(0xFFA8A4CE)    // Soft lavender - Clarity score

// ============================================================
// CHAT BUBBLE COLORS - Gentle Contrast
// ============================================================

val UserBubbleBackground = Color(0xFF8EACC8)     // Soft blue - User messages
val UserBubbleText = Color(0xFFFFFFFF)           // White text
val AIBubbleBackground = Color(0xFFE8E5E0)       // Warm gray - AI responses
val AIBubbleText = Color(0xFF3E3E3E)             // Soft dark gray text

// ============================================================
// BORDER & DIVIDER COLORS - Subtle Lines
// ============================================================

val BorderLight = Color(0xFFE0DDD8)    // Light warm gray borders
val BorderMedium = Color(0xFFCCC9C4)   // Medium warm gray borders
val DividerColor = Color(0xFFE0DDD8)   // Divider lines

// ============================================================
// RECORDING STATE COLORS - Soft Indicators
// ============================================================

val RecordingRed = Color(0xFFD89B9B)   // Soft coral recording dot
val RecordingText = Color(0xFF3E3E3E)  // Text during recording
val StopButtonRed = Color(0xFFD89B9B)  // Stop button background

// ============================================================
// OVERLAY & SHADOW COLORS
// ============================================================

val Overlay = Color(0x60000000)        // Black 37.5% - Softer overlays
val ShadowColor = Color(0x15000000)    // Black 8% - Subtle shadows

// ============================================================
// GLOSSY BUTTON COLORS - Premium Gradients
// ============================================================

val GlossyPrimaryStart = Color(0xFF9DB5A5)     // Light sage
val GlossyPrimaryEnd = Color(0xFF7A9688)       // Deeper sage
val GlossySecondaryStart = Color(0xFFB5B1D8)   // Light lavender
val GlossySecondaryEnd = Color(0xFF9490BD)     // Deeper lavender
val GlossyHighlight = Color(0x40FFFFFF)        // White 25% - Glossy shine
val GlossyShadow = Color(0x30000000)           // Black 19% - Depth shadow

// ============================================================
// DARK THEME COLORS - Calm Night Mode
// ============================================================

// Dark theme primary colors
val DarkPrimary = Color(0xFF8FA998)
val DarkPrimaryVariant = Color(0xFF6B8E7F)
val DarkOnPrimary = Color(0xFF1A1A1A)

// Dark theme backgrounds - Deep but not harsh
val DarkBackground = Color(0xFF1A1A1A)     // Soft black
val DarkSurface = Color(0xFF2A2A2A)        // Dark charcoal
val DarkSurfaceVariant = Color(0xFF3A3A3A) // Medium charcoal
val DarkOnBackground = Color(0xFFF0EDE8)   // Warm off-white
val DarkOnSurface = Color(0xFFF0EDE8)      // Warm off-white

// Dark theme text
val DarkTextPrimary = Color(0xFFF0EDE8)    // Warm off-white
val DarkTextSecondary = Color(0xFFC0BDB8)  // Light warm gray
val DarkTextTertiary = Color(0xFF9A9792)   // Medium warm gray

// Dark theme cards
val DarkContextCardBackground = Color(0xFF3A3A3A)
val DarkPromptCardBackground = Color(0xFF4A3F2F)
val DarkUserBubbleBackground = Color(0xFF6B8AA8)
val DarkAIBubbleBackground = Color(0xFF3A3A3A)

// ============================================================
// HELPER FUNCTIONS
// ============================================================

/**
 * Get color with opacity
 */
fun Color.withOpacity(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

/**
 * Get lighter version of color
 */
fun Color.lighten(factor: Float = 0.2f): Color {
    return Color(
        red = (red + (1f - red) * factor).coerceIn(0f, 1f),
        green = (green + (1f - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Get darker version of color
 */
fun Color.darken(factor: Float = 0.2f): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Get temperature color based on percentage
 * 0-33% = Green (Calm)
 * 34-66% = Yellow (Tense)
 * 67-100% = Red (Stressed)
 */
fun getTemperatureColor(percentage: Int): Color {
    return when {
        percentage < 34 -> TempGreen
        percentage < 67 -> TempYellow
        else -> TempRed
    }
}

/**
 * Get sentiment color based on emotion
 */
fun getSentimentColor(sentiment: String): Color {
    return when (sentiment.lowercase()) {
        "calm" -> TempGreen
        "focused" -> Info
        "stressed" -> TempYellow
        "anxious" -> TempRed
        else -> TextSecondary
    }
}