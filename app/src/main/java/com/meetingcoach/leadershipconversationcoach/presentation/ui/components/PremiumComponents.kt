package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

/**
 * Premium Glassmorphic Components
 * 
 * Depth-rich UI components with layered shadows and glass effects
 */

/**
 * Glassmorphic Card - AI Response Style
 * 
 * Warm taupe with backdrop blur and layered shadows
 */
@Composable
fun GlassmorphicAICard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = GlassTaupe,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GlassBorderLight,
                            Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .drawBehind {
                    // Layered shadow effect
                    drawRect(
                        color = ShadowMedium,
                        topLeft = Offset(0f, 4.dp.toPx()),
                        size = size
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    }
}

/**
 * User Message Bubble - Gradient with Inner Glow
 */
@Composable
fun UserMessageBubble(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .widthIn(max = 280.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 6.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(UserMessageGradientStart, UserMessageGradientEnd)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(InnerGlowLight, Color.Transparent),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 6.dp, bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .drawBehind {
                // Layered blue shadow
                drawRoundRect(
                    color = ShadowBlue,
                    topLeft = Offset(0f, 4.dp.toPx()),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                )
            }
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 22.5.sp,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
        )
    }
}

/**
 * Glassmorphic Floating Panel - For Headers
 */
@Composable
fun GlassmorphicFloatingPanel(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color(0xD9FFFFFF), // 85% white
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0x66FFFFFF), // 40% white
                    shape = RoundedCornerShape(20.dp)
                )
                .blur(20.dp)
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowLight,
                        topLeft = Offset(0f, 4.dp.toPx()),
                        size = size
                    )
                }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

/**
 * Tone Check Card - Elevated with Subtle Blur
 */
@Composable
fun ToneCheckCard(
    modifier: Modifier = Modifier,
    items: List<ToneCheckItem>
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xE6FFFFFF), // 90% white
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = GlassBorderSage,
                    shape = RoundedCornerShape(16.dp)
                )
                .blur(10.dp)
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowDeep,
                        topLeft = Offset(0f, 2.dp.toPx()),
                        size = size
                    )
                    drawRect(
                        color = ShadowMedium,
                        topLeft = Offset(0f, 8.dp.toPx()),
                        size = size
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items.forEach { item ->
                    ToneCheckItemRow(item)
                }
            }
        }
    }
}

data class ToneCheckItem(
    val type: ToneCheckType,
    val text: String
)

enum class ToneCheckType {
    SUCCESS, WARNING, TIP
}

@Composable
private fun ToneCheckItemRow(item: ToneCheckItem) {
    val scale by rememberInfiniteTransition(label = "hover").animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom icon
        ToneCheckIcon(type = item.type)
        
        Text(
            text = item.text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = DeepCharcoal
        )
    }
}

@Composable
private fun ToneCheckIcon(type: ToneCheckType) {
    val (color, icon) = when (type) {
        ToneCheckType.SUCCESS -> ToneSuccess to "✓"
        ToneCheckType.WARNING -> ToneWarning to "!"
        ToneCheckType.TIP -> ToneTip to "💡"
    }
    
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Transcript Card - Floating with Sentiment Border
 */
@Composable
fun TranscriptCard(
    speaker: String,
    timestamp: String,
    text: String,
    sentiment: SentimentType,
    modifier: Modifier = Modifier
) {
    val hoverScale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = hoverScale,
        animationSpec = tween(200),
        label = "hover"
    )
    
    Surface(
        modifier = modifier.scale(animatedScale),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CardGradientTop, CardGradientBottom)
                    )
                )
                .border(
                    width = 4.dp,
                    color = sentiment.color,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                .blur(8.dp)
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowMedium,
                        topLeft = Offset(0f, 4.dp.toPx()),
                        size = size
                    )
                    drawRect(
                        color = ShadowDeep,
                        topLeft = Offset(0f, 1.dp.toPx()),
                        size = size
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = speaker,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepCharcoal
                        )
                        Text(
                            text = timestamp,
                            fontSize = 12.sp,
                            color = NeutralGray
                        )
                    }
                    
                    // Sentiment dot with pulse
                    SentimentDot(sentiment = sentiment)
                }
                
                // Content
                Text(
                    text = text,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = DeepCharcoal
                )
            }
        }
    }
}

enum class SentimentType(val color: Color) {
    POSITIVE(SentimentPositive),
    NEUTRAL(SentimentNeutral),
    NEGATIVE(SentimentNegative)
}

@Composable
private fun SentimentDot(sentiment: SentimentType) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(sentiment.color)
            .blur(4.dp)
    )
}

/**
 * Settings Card - Neumorphic Style
 */
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color(0xE6C6A884), // 90% taupe
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color(0x40FFFFFF), // 25% white
                    shape = RoundedCornerShape(24.dp)
                )
                .drawBehind {
                    // Outer shadow
                    drawRect(
                        color = ShadowMedium,
                        topLeft = Offset(0f, 8.dp.toPx()),
                        size = size
                    )
                    // Inner highlight
                    drawRect(
                        color = InnerGlowLight,
                        topLeft = Offset(0f, 0f),
                        size = size.copy(height = 1.dp.toPx())
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                content = content
            )
        }
    }
}

/**
 * Empty State with Floating Animation
 */
@Composable
fun FloatingEmptyState(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Floating icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(y = offsetY.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            SageGreen,
                            Color.Transparent
                        )
                    )
                )
                .drawBehind {
                    drawCircle(
                        color = ShadowSage,
                        radius = size.minDimension / 2
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 64.sp
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DeepCharcoal,
            letterSpacing = (-0.5).sp
        )
        
        // Subtitle
        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = NeutralGray,
            letterSpacing = 0.2.sp
        )
    }
}
