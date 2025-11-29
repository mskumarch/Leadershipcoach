package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Leadership Conversation Coach - Premium Typography System
 *
 * Refined for a cleaner, more modern look using system sans-serif fonts.
 * Adjusted weights and letter spacing to mimic premium fonts like Inter/Outfit.
 */

val PremiumFontFamily = FontFamily.SansSerif

val AppTypography = Typography(
    // Display - Large, bold, tight spacing (e.g. "80%")
    displayLarge = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // Headlines - Clean, readable (e.g. "Sleep Quality")
    headlineLarge = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.SemiBold, // Slightly lighter than bold for elegance
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Titles - Section headers
    titleLarge = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium, // Medium weight for hierarchy
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Body - Highly readable, good line height
    bodyLarge = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Labels - Buttons, chips, captions
    labelLarge = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PremiumFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ============================================================
// TYPOGRAPHY UTILITIES
// ============================================================

fun Typography.scale(factor: Float): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(fontSize = this.displayLarge.fontSize * factor),
        displayMedium = this.displayMedium.copy(fontSize = this.displayMedium.fontSize * factor),
        displaySmall = this.displaySmall.copy(fontSize = this.displaySmall.fontSize * factor),
        headlineLarge = this.headlineLarge.copy(fontSize = this.headlineLarge.fontSize * factor),
        headlineMedium = this.headlineMedium.copy(fontSize = this.headlineMedium.fontSize * factor),
        headlineSmall = this.headlineSmall.copy(fontSize = this.headlineSmall.fontSize * factor),
        titleLarge = this.titleLarge.copy(fontSize = this.titleLarge.fontSize * factor),
        titleMedium = this.titleMedium.copy(fontSize = this.titleMedium.fontSize * factor),
        titleSmall = this.titleSmall.copy(fontSize = this.titleSmall.fontSize * factor),
        bodyLarge = this.bodyLarge.copy(fontSize = this.bodyLarge.fontSize * factor),
        bodyMedium = this.bodyMedium.copy(fontSize = this.bodyMedium.fontSize * factor),
        bodySmall = this.bodySmall.copy(fontSize = this.bodySmall.fontSize * factor),
        labelLarge = this.labelLarge.copy(fontSize = this.labelLarge.fontSize * factor),
        labelMedium = this.labelMedium.copy(fontSize = this.labelMedium.fontSize * factor),
        labelSmall = this.labelSmall.copy(fontSize = this.labelSmall.fontSize * factor)
    )
}