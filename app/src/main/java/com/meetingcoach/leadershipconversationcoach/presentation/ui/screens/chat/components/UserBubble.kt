package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlossyHighlight
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlossyPrimaryEnd
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlossyPrimaryStart
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.GlossyShadow

/**
 * User Question Bubble Component - Modern Sea Green Design
 *
 * Purpose: Display user's questions to the AI coach
 *
 * Visual Style:
 * - Background: Sea green gradient with glossy effect
 * - Text color: White
 * - Corner radius: 20dp (top) and 4dp (bottom-right pointer)
 * - Max width: 75% of screen
 * - Padding: 16dp
 * - Elevation: 4dp with shadow
 * - Alignment: Right with margin
 * - Glossy highlight overlay for premium feel
 *
 * Example: [User's question appears in sea green bubble on right side]
 */
@Composable
fun UserBubble(
    content: String,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.9f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .alpha(alpha.value)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 4.dp
                    ),
                    spotColor = GlossyShadow
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GlossyPrimaryStart,
                            GlossyPrimaryEnd
                        )
                    ),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 4.dp
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlossyHighlight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
            )

            Text(
                text = content,
                fontSize = 15.sp,
                color = Color.White,
                lineHeight = 22.sp,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            )
        }
    }
}