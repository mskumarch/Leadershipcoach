package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * QuestionQualityBadge
 *
 * Displays the quality of questions asked by the user.
 * - High Quality (>50% Open): "Open Questions" (Green)
 * - Low Quality: "Closed Questions" (Gray/Yellow)
 */
@Composable
fun QuestionQualityBadge(
    openQuestionCount: Int,
    totalQuestionCount: Int,
    modifier: Modifier = Modifier
) {
    if (totalQuestionCount == 0) return

    val ratio = openQuestionCount.toFloat() / totalQuestionCount.toFloat()
    val isHighQuality = ratio >= 0.5f

    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isHighQuality) 1.1f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "QualityBadgeScale"
    )

    val backgroundColor = if (isHighQuality) Color(0xFFECFDF5) else Color(0xFFF3F4F6)
    val contentColor = if (isHighQuality) Color(0xFF059669) else Color(0xFF6B7280)
    val text = if (isHighQuality) "✨ Open Questions" else "Closed Questions"

    Row(
        modifier = modifier
            .scale(scale)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}
