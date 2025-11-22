package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Leadership Conversation Coach - Typography System
 *
 * Complete Material 3 typography scale with custom font support
 * Uses system font by default, with option to add custom fonts
 */

// ============================================================
// FONT FAMILIES
// ============================================================

/**
 * Default font family (System default)
 * Can be replaced with custom fonts by adding font files to res/font/
 */
val DefaultFontFamily = FontFamily.Default

/**
 * Example: To use custom fonts, add font files to res/font/ folder
 * and uncomment the code below:
 *
 * val CustomFontFamily = FontFamily(
 *     Font(R.font.inter_regular, FontWeight.Normal),
 *     Font(R.font.inter_medium, FontWeight.Medium),
 *     Font(R.font.inter_semibold, FontWeight.SemiBold),
 *     Font(R.font.inter_bold, FontWeight.Bold)
 * )
 */

// ============================================================
// TYPOGRAPHY SCALE
// ============================================================

/**
 * Material 3 Typography Scale
 * Follows Material Design 3 specifications for text styles
 */
val AppTypography = Typography(
    // ========================================
    // DISPLAY STYLES (Large headings)
    // ========================================

    displayLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),

    displayMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),

    displaySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // ========================================
    // HEADLINE STYLES (Section headings)
    // ========================================

    headlineLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // ========================================
    // TITLE STYLES (Subsection headings)
    // ========================================

    titleLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    titleMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    titleSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // ========================================
    // BODY STYLES (Main content text)
    // ========================================

    bodyLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    bodySmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // ========================================
    // LABEL STYLES (Buttons, chips, tags)
    // ========================================

    labelLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    labelMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    labelSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ============================================================
// CUSTOM TEXT STYLES (App-Specific)
// ============================================================

/**
 * Custom text styles for specific use cases in the app
 */

// Card header styles
val CardHeaderStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

// Coaching nudge header
val NudgeHeaderStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.5.sp
)

// Chat bubble text
val ChatBubbleStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp
)

// Status bar text
val StatusBarStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
)

// Recording duration
val DurationStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.sp
)

// Metric label
val MetricLabelStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.5.sp
)

// Metric value
val MetricValueStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

// Button text
val ButtonTextStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
)

// Emoji text (larger for visibility)
val EmojiStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

// Timestamp
val TimestampStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.4.sp
)

// Speaker name in transcript
val SpeakerNameStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
)

// Session title
val SessionTitleStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
)

// Section header
val SectionHeaderStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 22.sp,
    letterSpacing = 0.15.sp
)

// Hint text
val HintTextStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.25.sp
)

// Error message
val ErrorTextStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
)

// ============================================================
// TYPOGRAPHY UTILITIES
// ============================================================

/**
 * Get text style with custom color
 */
fun TextStyle.withColor(color: androidx.compose.ui.graphics.Color): TextStyle {
    return this.copy(color = color)
}

/**
 * Get text style with custom weight
 */
fun TextStyle.withWeight(weight: FontWeight): TextStyle {
    return this.copy(fontWeight = weight)
}

/**
 * Get text style with custom size
 */
fun TextStyle.withSize(size: androidx.compose.ui.unit.TextUnit): TextStyle {
    return this.copy(fontSize = size)
}

/**
 * Get text style with line through
 */
fun TextStyle.strikethrough(): TextStyle {
    return this.copy(
        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
    )
}

/**
 * Get text style with underline
 */
fun TextStyle.underline(): TextStyle {
    return this.copy(
        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
    )
}