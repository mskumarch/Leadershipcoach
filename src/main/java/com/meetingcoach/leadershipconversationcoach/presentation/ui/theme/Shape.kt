package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Leadership Conversation Coach - Shape System
 *
 * Defines corner radius and shape values for all components
 * Following Material Design 3 shape guidelines
 */

// ============================================================
// CORNER RADIUS VALUES
// ============================================================

object CornerRadius {
    val none = 0.dp
    val xs = 4.dp       // Extra small corners
    val sm = 8.dp       // Small corners
    val md = 12.dp      // Medium corners (default)
    val lg = 16.dp      // Large corners
    val xl = 20.dp      // Extra large corners
    val xxl = 24.dp     // Extra extra large corners (pills)
    val xxxl = 32.dp    // Extra extra extra large corners
    val full = 999.dp   // Fully rounded (circle/pill)
}

// ============================================================
// SHAPE DEFINITIONS
// ============================================================

/**
 * Material 3 Shape System
 */
val AppShapes = Shapes(
    // Small components (buttons, chips, text fields)
    extraSmall = RoundedCornerShape(CornerRadius.xs),
    small = RoundedCornerShape(CornerRadius.sm),

    // Medium components (cards, dialogs)
    medium = RoundedCornerShape(CornerRadius.md),

    // Large components (bottom sheets, large cards)
    large = RoundedCornerShape(CornerRadius.lg),
    extraLarge = RoundedCornerShape(CornerRadius.xl)
)

/**
 * Custom shapes for specific components
 */
object CustomShapes {
    // Chat bubbles
    val userBubble = RoundedCornerShape(
        topStart = CornerRadius.lg,
        topEnd = CornerRadius.lg,
        bottomStart = CornerRadius.lg,
        bottomEnd = CornerRadius.xs
    )

    val aiBubble = RoundedCornerShape(
        topStart = CornerRadius.lg,
        topEnd = CornerRadius.lg,
        bottomStart = CornerRadius.xs,
        bottomEnd = CornerRadius.lg
    )

    // Cards
    val card = RoundedCornerShape(CornerRadius.md)
    val cardLarge = RoundedCornerShape(CornerRadius.lg)
    val cardRounded = RoundedCornerShape(CornerRadius.xl)

    // Coaching nudge cards
    val contextCard = RoundedCornerShape(CornerRadius.md)
    val urgentCard = RoundedCornerShape(CornerRadius.md)
    val promptCard = RoundedCornerShape(CornerRadius.md)
    val tipCard = RoundedCornerShape(CornerRadius.md)

    // Input field
    val inputField = RoundedCornerShape(CornerRadius.xl)
    val inputFieldPill = RoundedCornerShape(CornerRadius.xxl)

    // Buttons
    val button = RoundedCornerShape(CornerRadius.sm)
    val buttonRounded = RoundedCornerShape(CornerRadius.xl)
    val buttonPill = RoundedCornerShape(CornerRadius.xxl)
    val buttonCircle = CircleShape

    // Floating action button
    val fab = CircleShape
    val fabExtended = RoundedCornerShape(CornerRadius.lg)

    // Status pill
    val statusPill = RoundedCornerShape(CornerRadius.xxl)

    // Bottom sheet
    val bottomSheet = RoundedCornerShape(
        topStart = CornerRadius.xl,
        topEnd = CornerRadius.xl,
        bottomStart = CornerRadius.none,
        bottomEnd = CornerRadius.none
    )

    // Modal dialog
    val dialog = RoundedCornerShape(CornerRadius.xl)

    // Chips
    val chip = RoundedCornerShape(CornerRadius.sm)
    val chipRounded = RoundedCornerShape(CornerRadius.xxl)

    // Progress indicators
    val progressBar = RoundedCornerShape(CornerRadius.full)
    val progressRing = CircleShape

    // Badges
    val badge = RoundedCornerShape(CornerRadius.full)
    val badgeSquare = RoundedCornerShape(CornerRadius.xs)

    // Recording dot
    val recordingDot = CircleShape

    // Temperature gauge
    val temperatureGauge = CircleShape

    // Timeline marker
    val timelineMarker = CircleShape

    // Session mode card
    val sessionModeCard = RoundedCornerShape(CornerRadius.lg)

    // Navigation bar
    val navigationBar = RoundedCornerShape(
        topStart = CornerRadius.none,
        topEnd = CornerRadius.none,
        bottomStart = CornerRadius.none,
        bottomEnd = CornerRadius.none
    )

    // Image containers
    val imageRounded = RoundedCornerShape(CornerRadius.md)
    val imageCircle = CircleShape
    val avatar = CircleShape

    // Dividers (no corner radius, just for consistency)
    val divider = RoundedCornerShape(CornerRadius.none)
}

/**
 * Shape utilities
 */
object ShapeUtils {
    /**
     * Get a rounded rectangle shape with custom corner radius
     */
    fun roundedRectangle(radius: androidx.compose.ui.unit.Dp): Shape {
        return RoundedCornerShape(radius)
    }

    /**
     * Get a rounded rectangle with different corners
     */
    fun roundedRectangle(
        topStart: androidx.compose.ui.unit.Dp = CornerRadius.none,
        topEnd: androidx.compose.ui.unit.Dp = CornerRadius.none,
        bottomStart: androidx.compose.ui.unit.Dp = CornerRadius.none,
        bottomEnd: androidx.compose.ui.unit.Dp = CornerRadius.none
    ): Shape {
        return RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )
    }

    /**
     * Get a pill shape (fully rounded on sides)
     */
    fun pill(): Shape {
        return RoundedCornerShape(CornerRadius.full)
    }

    /**
     * Get a circle shape
     */
    fun circle(): Shape {
        return CircleShape
    }

    /**
     * Get a shape with only top corners rounded
     */
    fun topRounded(radius: androidx.compose.ui.unit.Dp = CornerRadius.lg): Shape {
        return RoundedCornerShape(
            topStart = radius,
            topEnd = radius,
            bottomStart = CornerRadius.none,
            bottomEnd = CornerRadius.none
        )
    }

    /**
     * Get a shape with only bottom corners rounded
     */
    fun bottomRounded(radius: androidx.compose.ui.unit.Dp = CornerRadius.lg): Shape {
        return RoundedCornerShape(
            topStart = CornerRadius.none,
            topEnd = CornerRadius.none,
            bottomStart = radius,
            bottomEnd = radius
        )
    }

    /**
     * Get a shape with only left corners rounded
     */
    fun leftRounded(radius: androidx.compose.ui.unit.Dp = CornerRadius.lg): Shape {
        return RoundedCornerShape(
            topStart = radius,
            topEnd = CornerRadius.none,
            bottomStart = radius,
            bottomEnd = CornerRadius.none
        )
    }

    /**
     * Get a shape with only right corners rounded
     */
    fun rightRounded(radius: androidx.compose.ui.unit.Dp = CornerRadius.lg): Shape {
        return RoundedCornerShape(
            topStart = CornerRadius.none,
            topEnd = radius,
            bottomStart = CornerRadius.none,
            bottomEnd = radius
        )
    }
}

/**
 * Shape presets for common patterns
 */
object ShapePresets {
    // Subtle rounding
    val subtle = RoundedCornerShape(CornerRadius.xs)

    // Standard rounding
    val standard = RoundedCornerShape(CornerRadius.md)

    // Pronounced rounding
    val pronounced = RoundedCornerShape(CornerRadius.lg)

    // Maximum rounding (pill-like)
    val maximum = RoundedCornerShape(CornerRadius.xxl)

    // No rounding (square)
    val square = RoundedCornerShape(CornerRadius.none)

    // Circle/pill
    val circular = CircleShape
}

/**
 * Conversational bubble shapes (for chat-like interfaces)
 */
object BubbleShapes {
    // Sent message (user) - pointed on bottom right
    val sent = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 4.dp
    )

    // Received message (AI/other) - pointed on bottom left
    val received = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 4.dp,
        bottomEnd = 16.dp
    )

    // System message - fully rounded
    val system = RoundedCornerShape(16.dp)
}