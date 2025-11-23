package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Leadership Conversation Coach - Calm & Soothing Color System
 *
 * Redesigned with calming sea green and lavender colors
 * Features refreshing sea greens, soothing lavenders, and warm neutrals
 */

// ============================================================
// PRIMARY COLORS - Pleasant Sea Green
// ============================================================

val Primary = Color(0xFF4DB6AC)        // Pleasant sea green - Main brand color
val PrimaryVariant = Color(0xFF26A69A) // Deeper sea green for emphasis
val OnPrimary = Color(0xFFFFFFFF)      // White text on primary

// ============================================================
// SECONDARY COLORS - Calming Lavender
// ============================================================

val Secondary = Color(0xFF9575CD)      // Calming lavender - Secondary actions
val SecondaryVariant = Color(0xFF7E57C2) // Deeper lavender
val OnSecondary = Color(0xFFFFFFFF)    // White text on secondary

// ============================================================
// SEMANTIC COLORS (Status & Actions) - Harmonious Tones
// ============================================================

val Success = Color(0xFF66BB6A)        // Fresh green - Success states
val Warning = Color(0xFFFFB74D)        // Warm amber - Warning states
val Error = Color(0xFFEF5350)          // Coral - Error states
val Info = Color(0xFF42A5F5)           // Sky blue - Info states

// ============================================================
// BACKGROUND COLORS - Soft Neutrals
// ============================================================

val Background = Color(0xFFF0F4F8)     // Cool off-white - Main background
val BackgroundVariant = Color(0xFFE3EAF2) // Light blue-gray
val Surface = Color(0xFFFFFFFE)        // Pure white - Cards & surfaces
val SurfaceVariant = Color(0xFFEDF2F7) // Very light gray - Alternate surfaces
val OnBackground = Color(0xFF2D3748)   // Dark blue-gray text on background
val OnSurface = Color(0xFF2D3748)      // Dark blue-gray text on surface

// ============================================================
// TEXT COLORS - Neutral Grays
// ============================================================

val TextPrimary = Color(0xFF2D3748)    // Dark blue-gray - Primary text
val TextSecondary = Color(0xFF718096)  // Medium gray - Secondary text
val TextTertiary = Color(0xFFA0AEC0)   // Light gray - Tertiary text
val TextDisabled = Color(0xFFCBD5E0)   // Very light gray - Disabled text

// ============================================================
// CARD & COMPONENT BACKGROUNDS - Harmonious Tones
// ============================================================

val ContextCardBackground = Color(0xFFE6F7F5)    // Soft sea green tint - Context info cards
val PromptCardBackground = Color(0xFFF3E5F5)     // Soft lavender tint - Reflection prompts
val UrgentCardBorder = Color(0xFFEF5350)         // Coral - Urgent nudges border
val TipCardBorder = Color(0xFF4DB6AC)            // Sea green - Helpful tips border

// ============================================================
// TEMPERATURE GAUGE COLORS - Sea to Lavender Gradient
// ============================================================

val TempGreen = Color(0xFF4DB6AC)      // Calm (0-33%) - Sea green
val TempYellow = Color(0xFF9575CD)     // Tense (34-66%) - Lavender
val TempRed = Color(0xFFBA68C8)        // Stressed (67-100%) - Deep lavender

// ============================================================
// PROGRESS RING COLORS - Sea Green to Lavender Harmony
// ============================================================

val EmpathyRing = Color(0xFF4DB6AC)    // Sea green - Empathy score
val ListeningRing = Color(0xFF7986CB)  // Indigo - Listening ratio
val ClarityRing = Color(0xFF9575CD)    // Lavender - Clarity score

// ============================================================
// CHAT BUBBLE COLORS - Sea Green & Lavender
// ============================================================

val UserBubbleBackground = Color(0xFF4DB6AC)     // Sea green - User messages
val UserBubbleText = Color(0xFFFFFFFF)           // White text
val AIBubbleBackground = Color(0xFFEDE7F6)       // Soft lavender tint - AI responses
val AIBubbleText = Color(0xFF2D3748)             // Dark text

// ============================================================
// BORDER & DIVIDER COLORS - Clean Lines
// ============================================================

val BorderLight = Color(0xFFE2E8F0)    // Light cool gray borders
val BorderMedium = Color(0xFFCBD5E0)   // Medium cool gray borders
val DividerColor = Color(0xFFE2E8F0)   // Divider lines

// ============================================================
// RECORDING STATE COLORS - Clear Indicators
// ============================================================

val RecordingRed = Color(0xFFEF5350)   // Coral recording dot
val RecordingText = Color(0xFF2D3748)  // Text during recording
val StopButtonRed = Color(0xFFEF5350)  // Stop button background

// ============================================================
// OVERLAY & SHADOW COLORS
// ============================================================

val Overlay = Color(0x60000000)        // Black 37.5% - Softer overlays
val ShadowColor = Color(0x15000000)    // Black 8% - Subtle shadows

// ============================================================
// GLOSSY BUTTON COLORS - Sea Green & Lavender Gradients
// ============================================================

val GlossyPrimaryStart = Color(0xFF80CBC4)     // Light sea green
val GlossyPrimaryEnd = Color(0xFF26A69A)       // Deep sea green
val GlossySecondaryStart = Color(0xFFB39DDB)   // Light lavender
val GlossySecondaryEnd = Color(0xFF7E57C2)     // Deep lavender
val GlossyHighlight = Color(0x50FFFFFF)        // White 31% - Glossy shine
val GlossyShadow = Color(0x40000000)           // Black 25% - Depth shadow
val GlossyNavBarStart = Color(0xFFE0F2F1)      // Very light sea green - Nav bar gradient start
val GlossyNavBarEnd = Color(0xFFF3E5F5)        // Very light lavender - Nav bar gradient end

// ============================================================
// MODERN UI ACCENT COLORS - Contemporary Design
// ============================================================

// Soft Pastels for Modern UI
val AccentCoral = Color(0xFFFF9B71)            // Soft coral - Warm accent
val AccentPeach = Color(0xFFFFB4A2)            // Peach - Gentle highlight
val AccentLavender = Color(0xFFB39DDB)         // Soft lavender - Cool accent
val AccentMint = Color(0xFF80CBC4)             // Mint green - Fresh accent
val AccentRose = Color(0xFFFF7B9C)             // Rose - Romantic accent
val AccentSky = Color(0xFF90CAF9)              // Sky blue - Calm accent

// Gradient Combinations
val GradientCoralRose = listOf(AccentCoral, AccentRose)
val GradientLavenderSky = listOf(AccentLavender, AccentSky)
val GradientMintTeal = listOf(AccentMint, Primary)
val GradientPeachCoral = listOf(AccentPeach, AccentCoral)

// Voice Recording Colors
val RecordingPulse = Color(0xFF9575CD)         // Lavender pulse
val RecordingWave = Color(0xFFB39DDB)          // Light lavender wave
val RecordingActive = Color(0xFFFF9B71)        // Coral when active

// ============================================================
// DARK THEME COLORS - Serene Night Mode
// ============================================================

// Dark theme primary colors
val DarkPrimary = Color(0xFF4DB6AC)
val DarkPrimaryVariant = Color(0xFF26A69A)
val DarkOnPrimary = Color(0xFF1A202C)

// Dark theme backgrounds - Deep but soothing
val DarkBackground = Color(0xFF1A202C)     // Dark blue-black
val DarkSurface = Color(0xFF2D3748)        // Dark slate
val DarkSurfaceVariant = Color(0xFF4A5568) // Medium slate
val DarkOnBackground = Color(0xFFF7FAFC)   // Cool off-white
val DarkOnSurface = Color(0xFFF7FAFC)      // Cool off-white

// Dark theme text
val DarkTextPrimary = Color(0xFFF7FAFC)    // Cool off-white
val DarkTextSecondary = Color(0xFFCBD5E0)  // Light cool gray
val DarkTextTertiary = Color(0xFFA0AEC0)   // Medium cool gray

// Dark theme cards
val DarkContextCardBackground = Color(0xFF2C4A47)
val DarkPromptCardBackground = Color(0xFF3E3357)
val DarkUserBubbleBackground = Color(0xFF26A69A)
val DarkAIBubbleBackground = Color(0xFF4A5568)

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