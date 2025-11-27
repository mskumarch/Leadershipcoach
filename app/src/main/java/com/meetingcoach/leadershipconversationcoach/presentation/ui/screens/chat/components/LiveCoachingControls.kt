package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

/**
 * Note Panel with Pill-shaped Selectors
 * Allows the mentor to quickly tag or view categories during the session.
 */
@Composable
fun NotePanel(
    modifier: Modifier = Modifier,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Wins", "Challenges", "Feedback", "Goals", "Action Items")
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            NotePill(
                text = category,
                isSelected = category == selectedCategory,
                onClick = {
                    selectedCategory = if (selectedCategory == category) null else category
                    onCategorySelected(category)
                }
            )
        }
    }
}

@Composable
fun NotePill(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = if (isSelected) AppPalette.Sage600 else AppPalette.Stone100,
        contentColor = if (isSelected) Color.White else AppPalette.Stone500
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Sentiment Indicator
 * Subtle indicator of the current sentiment: Calm, Neutral, Concern, Engaged
 */
@Composable
fun SentimentIndicator(
    sentiment: String, // "Calm", "Neutral", "Concern", "Engaged"
    modifier: Modifier = Modifier
) {
    val (color, label) = when (sentiment) {
        "Calm" -> AppPalette.Sage500 to "Calm"
        "Concern" -> AppPalette.Red500 to "Concern"
        "Engaged" -> AppPalette.Blue500 to "Engaged"
        else -> AppPalette.Stone500 to "Neutral"
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
