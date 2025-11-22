package com.meetingcoach.leadershipconversationcoach.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

/**
 * Glossy Pill-Shaped Button Components
 *
 * Premium, modern buttons with:
 * - Smooth pill shape (fully rounded corners)
 * - Gradient backgrounds
 * - Glossy highlight effect
 * - Subtle shadows for depth
 */

/**
 * Primary glossy pill button with sage green gradient
 */
@Composable
fun GlossyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 2.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = GlossyShadow
            ),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (enabled) {
                            listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
                        } else {
                            listOf(
                                GlossyPrimaryStart.copy(alpha = 0.5f),
                                GlossyPrimaryEnd.copy(alpha = 0.5f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glossy highlight overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlossyHighlight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
            )
            
            // Button content
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Secondary glossy pill button with lavender gradient
 */
@Composable
fun GlossySecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .shadow(
                elevation = if (enabled) 8.dp else 2.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = GlossyShadow
            ),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (enabled) {
                            listOf(GlossySecondaryStart, GlossySecondaryEnd)
                        } else {
                            listOf(
                                GlossySecondaryStart.copy(alpha = 0.5f),
                                GlossySecondaryEnd.copy(alpha = 0.5f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glossy highlight overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GlossyHighlight,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
            )
            
            // Button content
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Outlined glossy pill button (transparent with border)
 */
@Composable
fun GlossyOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(
                colors = listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
            ),
            width = 2.dp
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Primary.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                it()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Small glossy pill button for compact spaces
 */
@Composable
fun GlossySmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(40.dp)
            .shadow(
                elevation = if (enabled) 4.dp else 1.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = GlossyShadow
            ),
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (enabled) {
                            if (isPrimary) {
                                listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
                            } else {
                                listOf(GlossySecondaryStart, GlossySecondaryEnd)
                            }
                        } else {
                            listOf(
                                GlossyPrimaryStart.copy(alpha = 0.5f),
                                GlossyPrimaryEnd.copy(alpha = 0.5f)
                            )
                        }
                    ),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glossy highlight overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .align(Alignment.TopCenter)
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
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
