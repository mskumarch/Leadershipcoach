package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier // ← Add this parameter
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "📚 History Screen\n(Coming Soon)",
            fontSize = 24.sp
        )
    }
}