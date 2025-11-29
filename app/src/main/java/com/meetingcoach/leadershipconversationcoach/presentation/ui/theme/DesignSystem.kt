package com.meetingcoach.leadershipconversationcoach.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

/**
 * Leadership Coach - Ethereal Glass Design System
 * 
 * Implements the "Glassmorphic" aesthetic with soft gradients,
 * translucent surfaces, and subtle borders.
 */

object GlassDesign {
    
    // ============================================================
    // Glass Opacities
    // ============================================================
    const val CardAlpha = 0.70f       // Main cards
    const val OverlayAlpha = 0.85f    // Modals / Bottom Sheets
    const val NavigationAlpha = 0.90f // Bottom Navigation
    const val BorderAlpha = 0.40f     // Subtle white borders
    
    // ============================================================
    // Gradients
    // ============================================================
    
    // The main background gradient (Sage to Soft Lavender)
    val EtherealBackground = Brush.verticalGradient(
        colors = listOf(
            AppPalette.Sage5,   // Top (Lightest Sage)
            AppPalette.Sage25,  // Mid (Soft Sage)
            AppPalette.Lavender100.copy(alpha = 0.5f) // Bottom (Hint of Lavender)
        )
    )
    
    // Card Gradient (Subtle top-left to bottom-right shine)
    val GlassCardGradient = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.8f),
            Color.White.copy(alpha = 0.5f)
        )
    )
    
    // Active State Gradient (Sage)
    val ActiveGradient = Brush.horizontalGradient(
        colors = listOf(
            AppPalette.Sage500,
            AppPalette.Sage700
        )
    )
    
    // Lavender Accent Gradient
    val LavenderGradient = Brush.horizontalGradient(
        colors = listOf(
            AppPalette.Lavender500,
            Color(0xFF9F8CF1)
        )
    )
}

/**
 * Applies the standard Glassmorphism effect.
 * 
 * @param shape The shape of the component (default: Medium rounded corners)
 * @param alpha The opacity of the glass background (default: 0.7f)
 */
fun Modifier.glass(
    shape: Shape = RoundedCornerShape(16.dp),
    alpha: Float = GlassDesign.CardAlpha
): Modifier = composed {
    this
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = alpha),
                    Color.White.copy(alpha = alpha * 0.7f)
                )
            ),
            shape = shape
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.6f),
                    Color.White.copy(alpha = 0.1f)
                )
            ),
            shape = shape
        )
}

/**
 * Applies a "Premium" shadow and elevation effect without using standard elevation.
 */
fun Modifier.etherealShadow(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = composed {
    // In a real implementation, we might use a custom draw modifier for a colored shadow.
    // For now, we'll rely on standard shadow but keep it soft.
    this
        // .shadow(elevation = 8.dp, shape = shape, spotColor = AppPalette.Sage700.copy(alpha = 0.1f)) 
        // Commented out to avoid over-shadowing, can be enabled if needed.
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    alpha: Float = GlassDesign.CardAlpha,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .glass(shape, alpha),
        content = content
    )
}
