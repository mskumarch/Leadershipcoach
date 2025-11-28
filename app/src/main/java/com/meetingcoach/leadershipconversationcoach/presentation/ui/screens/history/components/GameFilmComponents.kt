package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.data.local.SessionMessageEntity
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * Game Film Empty State
 * Displays when no key moments (nudges, questions) were detected in the session.
 */
@Composable
fun GameFilmEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Illustration
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(AppPalette.Sage100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.VideocamOff,
                contentDescription = null,
                tint = AppPalette.Sage500,
                modifier = Modifier.size(48.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No Key Moments",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppPalette.Stone900
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "This session flowed smoothly without any AI interventions or user questions.",
            style = MaterialTheme.typography.bodyMedium,
            color = AppPalette.Stone500,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

/**
 * Enhanced Game Film Timeline
 * Visualizes the session as a timeline with interactive markers for key moments.
 */
@Composable
fun EnhancedGameFilmTimeline(
    durationSeconds: Int,
    messages: List<SessionMessageEntity>,
    startedAt: Long,
    onMomentSelected: (SessionMessageEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyMoments = messages.filter {
        it.messageType.contains("NUDGE") || 
        it.messageType == "USER_QUESTION" ||
        it.messageType == "AI_RESPONSE"
    }

    if (keyMoments.isEmpty()) {
        GameFilmEmptyState(modifier)
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Session Timeline",
                style = MaterialTheme.typography.labelMedium,
                color = AppPalette.Stone500,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatDuration(durationSeconds),
                style = MaterialTheme.typography.labelMedium,
                color = AppPalette.Stone500
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timeline Visualization
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // 1. The Track (Base Line)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(AppPalette.Stone200, RoundedCornerShape(2.dp))
            )

            // 2. Markers
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val trackY = size.height / 2
                
                keyMoments.forEach { moment ->
                    val relativeTime = (moment.createdAt - startedAt) / 1000f
                    val progress = if (durationSeconds > 0) (relativeTime / durationSeconds).coerceIn(0f, 1f) else 0f
                    val x = width * progress
                    
                    val color = when {
                        moment.messageType.contains("URGENT") -> AppPalette.Red500
                        moment.messageType.contains("IMPORTANT") -> AppPalette.Amber500
                        moment.messageType == "USER_QUESTION" -> AppPalette.Blue500
                        else -> AppPalette.Sage500 // AI Response / Helpful Tip
                    }
                    
                    // Draw Marker Line
                    drawLine(
                        color = color.copy(alpha = 0.5f),
                        start = Offset(x, trackY - 15.dp.toPx()),
                        end = Offset(x, trackY + 15.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                    
                    // Draw Marker Dot
                    drawCircle(
                        color = color,
                        radius = 6.dp.toPx(),
                        center = Offset(x, trackY)
                    )
                }
            }
            
            // Invisible Touch Targets for better UX
            // (In a real app, we'd calculate touch offsets, but for now we rely on the list below or simple tapping)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(color = AppPalette.Red500, label = "Nudge")
            LegendItem(color = AppPalette.Blue500, label = "Question")
            LegendItem(color = AppPalette.Sage500, label = "Insight")
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = AppPalette.Stone500)
    }
}

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
