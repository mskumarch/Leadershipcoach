package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

/**
 * Modern Chat Input Field - For asking AI questions
 *
 * Features:
 * - Visible text input with proper contrast
 * - Glossy pill-shaped send button
 * - Multi-line support (max 3 lines)
 * - Smooth animations
 */
@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Text input field
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = ShadowColor
                ),
            placeholder = {
                Text(
                    text = "Ask AI a question...",
                    color = TextTertiary
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                disabledTextColor = TextDisabled,
                focusedContainerColor = SurfaceVariant,
                unfocusedContainerColor = SurfaceVariant,
                disabledContainerColor = SurfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Primary,
                focusedPlaceholderColor = TextTertiary,
                unfocusedPlaceholderColor = TextTertiary
            ),
            shape = RoundedCornerShape(24.dp),
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (value.isNotBlank()) {
                        onSend()
                    }
                }
            )
        )

        // Glossy send button
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = if (value.isNotBlank()) 6.dp else 2.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = GlossyShadow
                )
                .background(
                    brush = if (value.isNotBlank()) {
                        Brush.verticalGradient(
                            colors = listOf(GlossyPrimaryStart, GlossyPrimaryEnd)
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                BorderLight,
                                BorderMedium
                            )
                        )
                    },
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Glossy highlight
            if (value.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    GlossyHighlight,
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                )
            }
            
            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank(),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (value.isNotBlank()) {
                        Color.White
                    } else {
                        TextTertiary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}