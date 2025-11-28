package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.animation.core.animateFloatAsState
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
 * Premium Card - Standardized for Visual Consistency
 * 22dp Radius, Soft Shadow, White Background
 */
@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(PremiumStyles.StandardCardRadius)
    
    Surface(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = shape,
                spotColor = Color.Black.copy(alpha = 0.08f),
                ambientColor = Color.Black.copy(alpha = 0.03f)
            ),
        shape = shape,
        color = Color.White,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
            content = content
        )
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
                    colors = listOf(ActiveBlue, ActiveBlueLight)
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
                // Removed blur from content container to fix "completely blur" issue
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowStrong,
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
                    color = GlassBorderLight,
                    shape = RoundedCornerShape(16.dp)
                )
                // Removed blur
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowSubtle,
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
        ToneCheckType.SUCCESS -> Success to "✓"
        ToneCheckType.WARNING -> Warning to "!"
        ToneCheckType.TIP -> Info to "💡"
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
                        colors = listOf(Color(0xF2FFFFFF), AppPalette.Stone50)
                    )
                )
                .border(
                    width = 4.dp,
                    color = sentiment.color,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
                // Removed blur
                .drawBehind {
                    // Layered shadow
                    drawRect(
                        color = ShadowMedium,
                        topLeft = Offset(0f, 4.dp.toPx()),
                        size = size
                    )
                    drawRect(
                        color = ShadowSubtle,
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
    POSITIVE(Success),
    NEUTRAL(Warning),
    NEGATIVE(MutedCoral)
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
        color = AppPalette.Stone100.copy(alpha = 0.9f), // 90% Stone
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

/**
 * Mesh Gradient Background
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppPalette.Stone50)
    ) {
        // Mesh Gradients
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top Left - Sage
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AppPalette.Sage600.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(0f, 0f),
                    radius = size.width * 0.8f
                )
            )
            // Bottom Right - Lavender
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AppPalette.Lavender500.copy(alpha = 0.1f), Color.Transparent),
                    center = Offset(size.width, size.height),
                    radius = size.width * 0.8f
                )
            )
        }
        
        content()
    }
}

/**
 * Start Session Orb - Glowing Pulse Button
 */
@Composable
fun StartSessionOrb(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .size(160.dp)
            .scale(scale)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Outer Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(32.dp)
                .background(AppPalette.Sage500.copy(alpha = glowAlpha), CircleShape)
        )
        
        // Main Orb
        Surface(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            shape = CircleShape,
            color = AppPalette.Sage600,
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(AppPalette.Sage500, AppPalette.Sage600),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "START",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "SESSION",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

/**
 * Floating Pill Navigation
 */
@Composable
fun FloatingPillNav(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(72.dp)
            .width(380.dp),
        shape = RoundedCornerShape(36.dp),
        color = NavGlassBase,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavIcon(icon = Icons.Rounded.Home, label = "Home", isActive = currentTab == 0, onClick = { onTabSelected(0) })
            NavIcon(icon = Icons.Rounded.Lightbulb, label = "Wisdom", isActive = currentTab == 5, onClick = { onTabSelected(5) })
            NavIcon(icon = Icons.Rounded.EditNote, label = "Live", isActive = currentTab == 1, onClick = { onTabSelected(1) })
            NavIcon(icon = Icons.Rounded.BarChart, label = "Stats", isActive = currentTab == 2, onClick = { onTabSelected(2) })
            NavIcon(icon = Icons.Rounded.History, label = "History", isActive = currentTab == 3, onClick = { onTabSelected(3) })
            NavIcon(icon = Icons.Rounded.Settings, label = "Settings", isActive = currentTab == 4, onClick = { onTabSelected(4) })
        }
    }
}

@Composable
private fun NavIcon(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isActive) 1.2f else 1.0f, label = "scale")
    val color = if (isActive) Color(0xFF4F7F6B) else Color(0xFFA0B8AD)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF4F7F6B).copy(alpha = 0.1f), CircleShape)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier
                    .size(24.dp)
                    .scale(scale)
            )
        }
        
        if (isActive) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(4.dp)
                    .background(Color(0xFF4F7F6B), CircleShape)
            )
        }
    }
}




/**
 * Metrics HUD - Top Bar
 */
@Composable
fun MetricsHUD(
    isRecording: Boolean,
    duration: String,
    talkRatio: Int,
    qualityScore: Int, // 0-100
    modifier: Modifier = Modifier
) {
    GlassmorphicFloatingPanel(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(if (isRecording) RecordingActive else NeutralGray, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = duration,
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepCharcoal,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Metrics
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Mini Talk Ratio
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🗣️", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$talkRatio%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Mini Quality
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$qualityScore",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Streaming Transcript Bubble
 */
@Composable
fun StreamingTranscriptBubble(
    text: String,
    isFinal: Boolean,
    speaker: String,
    modifier: Modifier = Modifier
) {
    val isUser = speaker == "USER" // Simplified check
    val align = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (isUser) Color.Transparent else Color.Transparent // Minimalist
    
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = align
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Text(
                text = speaker,
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isFinal) TextPrimary else TextDisabled,
                fontWeight = if (isFinal) FontWeight.Normal else FontWeight.Light,
                modifier = Modifier.widthIn(max = 300.dp)
            )
        }
    }
}
