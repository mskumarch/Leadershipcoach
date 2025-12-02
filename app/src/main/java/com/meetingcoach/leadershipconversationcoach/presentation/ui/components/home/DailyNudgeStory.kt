package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import kotlinx.coroutines.delay

@Composable
fun DailyNudgeStory(
    onDismiss: () -> Unit
) {
    // Mock Data for the Story
    val slides = listOf(
        StorySlide(
            emoji = "💡",
            title = "The Power of Pause",
            content = "Did you know? Pausing for just 3 seconds before responding can increase perceived authority by 40%.",
            color = AppPalette.Sage700
        ),
        StorySlide(
            emoji = "🧠",
            title = "Why it Works",
            content = "It signals that you are thinking, not just reacting. It gives you control over the conversation pace.",
            color = AppPalette.Stone700
        ),
        StorySlide(
            emoji = "⚡",
            title = "Try This Today",
            content = "In your next meeting, count to 3 after someone finishes speaking before you jump in.",
            color = AppPalette.Red500
        )
    )

    var currentSlideIndex by remember { mutableStateOf(0) }
    val currentSlide = slides[currentSlideIndex]

    // Auto-advance logic (optional, but "Story" style usually has it)
    // For now, let's make it tap-to-advance for better UX reading speed
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable {
                    if (currentSlideIndex < slides.size - 1) {
                        currentSlideIndex++
                    } else {
                        onDismiss()
                    }
                }
        ) {
            // Background Color with Gradient feel
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(currentSlide.color)
            )

            // Progress Bars at top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                slides.forEachIndexed { index, _ ->
                    val progress by animateFloatAsState(
                        targetValue = if (index < currentSlideIndex) 1f else if (index == currentSlideIndex) 1f else 0f, // Simplified progress
                        animationSpec = tween(durationMillis = if (index == currentSlideIndex) 5000 else 0, easing = LinearEasing),
                        label = "progress"
                    )
                    
                    // Static bars for now to keep it simple
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (index <= currentSlideIndex) Color.White else Color.White.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentSlide.emoji,
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = currentSlide.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentSlide.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 28.sp
                )
            }

            // Tap hint
            Text(
                text = "Tap to continue",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            )
        }
    }
}

data class StorySlide(
    val emoji: String,
    val title: String,
    val content: String,
    val color: Color
)
