package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Leadership Conversation Coach - Spacing System
 *
 * Consistent spacing values following Material Design 3 guidelines
 * Based on 4dp grid system for predictable layouts
 */

object Spacing {
    // ========================================
    // BASE SPACING VALUES (4dp increments)
    // ========================================

    val none: Dp = 0.dp
    val xxxs: Dp = 2.dp     // Extra extra extra small
    val xxs: Dp = 4.dp      // Extra extra small
    val xs: Dp = 8.dp       // Extra small
    val sm: Dp = 12.dp      // Small
    val md: Dp = 16.dp      // Medium (default)
    val lg: Dp = 24.dp      // Large
    val xl: Dp = 32.dp      // Extra large
    val xxl: Dp = 40.dp     // Extra extra large
    val xxxl: Dp = 48.dp    // Extra extra extra large

    // ========================================
    // COMPONENT-SPECIFIC SPACING
    // ========================================

    // Screen padding
    val screenHorizontal: Dp = 24.dp
    val screenVertical: Dp = 16.dp
    val screenTop: Dp = 16.dp
    val screenBottom: Dp = 16.dp

    // Card padding
    val cardPadding: Dp = 16.dp
    val cardPaddingSmall: Dp = 12.dp
    val cardPaddingLarge: Dp = 20.dp

    // Card margin
    val cardMarginHorizontal: Dp = 24.dp
    val cardMarginVertical: Dp = 12.dp

    // Button padding
    val buttonPaddingHorizontal: Dp = 24.dp
    val buttonPaddingVertical: Dp = 12.dp
    val buttonPaddingSmall: Dp = 8.dp

    // Icon sizes
    val iconSmall: Dp = 16.dp
    val iconMedium: Dp = 24.dp
    val iconLarge: Dp = 32.dp
    val iconXLarge: Dp = 48.dp

    // Spacing between items
    val itemSpacingSmall: Dp = 8.dp
    val itemSpacingMedium: Dp = 12.dp
    val itemSpacingLarge: Dp = 16.dp

    // Section spacing
    val sectionSpacing: Dp = 24.dp
    val sectionSpacingLarge: Dp = 32.dp

    // List item spacing
    val listItemSpacing: Dp = 12.dp
    val listItemPadding: Dp = 16.dp

    // Divider spacing
    val dividerSpacing: Dp = 16.dp

    // Bottom bar height
    val bottomBarHeight: Dp = 56.dp
    val bottomBarPadding: Dp = 8.dp

    // Status bar padding
    val statusBarHeight: Dp = 52.dp
    val statusBarPaddingHorizontal: Dp = 16.dp
    val statusBarPaddingVertical: Dp = 12.dp

    // Floating action button
    val fabSize: Dp = 56.dp
    val fabPadding: Dp = 16.dp
    val fabBottomPadding: Dp = 80.dp

    // Chat bubbles
    val bubblePadding: Dp = 12.dp
    val bubbleMarginHorizontal: Dp = 24.dp
    val bubbleMarginVertical: Dp = 4.dp
    val bubbleMaxWidth: Dp = 280.dp

    // Input field
    val inputFieldPaddingHorizontal: Dp = 12.dp
    val inputFieldPaddingVertical: Dp = 8.dp
    val inputFieldHeight: Dp = 48.dp

    // Coaching cards
    val coachingCardMaxWidth: Dp = 320.dp
    val coachingCardPadding: Dp = 16.dp

    // Recording dot
    val recordingDotSize: Dp = 8.dp

    // Progress ring
    val progressRingSize: Dp = 120.dp
    val progressRingStrokeWidth: Dp = 8.dp

    // Temperature gauge
    val temperatureGaugeSize: Dp = 32.dp
    val temperatureGaugeStrokeWidth: Dp = 4.dp

    // Timeline scrubber
    val timelineHeight: Dp = 40.dp
    val timelineMarkerSize: Dp = 8.dp

    // Session mode card
    val sessionModeCardPadding: Dp = 20.dp
    val sessionModeIconSize: Dp = 48.dp
}

/**
 * Semantic spacing for specific UI patterns
 */
object SemanticSpacing {
    // Space between label and value
    val labelValueSpacing: Dp = 4.dp

    // Space between icon and text
    val iconTextSpacing: Dp = 8.dp

    // Space between sections in a form
    val formSectionSpacing: Dp = 24.dp

    // Space between form fields
    val formFieldSpacing: Dp = 16.dp

    // Space around modal dialogs
    val modalPadding: Dp = 24.dp
    val modalItemSpacing: Dp = 16.dp

    // Space in bottom sheets
    val bottomSheetPadding: Dp = 24.dp
    val bottomSheetItemSpacing: Dp = 12.dp

    // Space in cards
    val cardContentSpacing: Dp = 12.dp
    val cardHeaderSpacing: Dp = 8.dp

    // Space between message bubbles
    val messageBubbleSpacing: Dp = 12.dp

    // Space for coaching nudges
    val nudgeSpacing: Dp = 12.dp
    val nudgeContentSpacing: Dp = 8.dp
}

/**
 * Elevation values for shadows and layers
 */
object Elevation {
    val none: Dp = 0.dp
    val level1: Dp = 1.dp      // Subtle elevation
    val level2: Dp = 2.dp      // Cards
    val level3: Dp = 4.dp      // Raised cards
    val level4: Dp = 8.dp      // FAB, bottom bar
    val level5: Dp = 12.dp     // Modal dialogs
    val level6: Dp = 16.dp     // Dropdown menus
    val level7: Dp = 24.dp     // Top-level overlays
}

/**
 * Touch target sizes for accessibility
 */
object TouchTarget {
    val minimum: Dp = 48.dp    // Minimum touch target (Material guidelines)
    val button: Dp = 48.dp     // Standard button height
    val iconButton: Dp = 48.dp // Icon button size
    val checkbox: Dp = 48.dp   // Checkbox touch area
    val radioButton: Dp = 48.dp // Radio button touch area
}

/**
 * Layout constraints
 */
object LayoutConstraints {
    val maxContentWidth: Dp = 600.dp      // Max width for content on tablets
    val maxCardWidth: Dp = 400.dp         // Max width for cards
    val maxBubbleWidth: Dp = 280.dp       // Max width for chat bubbles
    val minTouchTarget: Dp = 48.dp        // Minimum touch target
    val toolbarHeight: Dp = 56.dp         // Standard toolbar height
    val navigationBarHeight: Dp = 80.dp   // Bottom navigation height
}

/**
 * Animation durations (in milliseconds)
 */
object AnimationDuration {
    const val instant = 0
    const val fast = 150
    const val normal = 300
    const val slow = 500
    const val verySlow = 1000
}

/**
 * Helper extension functions
 */

/**
 * Get responsive spacing based on screen size
 * Useful for tablets and large screens
 */
fun Dp.responsive(isTablet: Boolean): Dp {
    return if (isTablet) this * 1.5f else this
}

/**
 * Get spacing multiplied by factor
 */
operator fun Dp.times(factor: Float): Dp {
    return Dp(this.value * factor)
}

/**
 * Get spacing multiplied by integer
 */
operator fun Dp.times(factor: Int): Dp {
    return Dp(this.value * factor)
}