package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.*

/**
 * Session Mode Selection Modal
 *
 * Shows 3 session types for user to choose from
 * Appears when:
 * - User taps "Start Session" in Chat screen
 * - User taps Coach tab (when not recording)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionModeModal(
    onModeSelected: (SessionMode) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SoftCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose Session Type",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepCharcoal
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = DeepCharcoal.copy(alpha = 0.6f)
                    )
                }
            }

            Text(
                text = "AI will adapt coaching to your context",
                fontSize = 14.sp,
                color = DeepCharcoal.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = DeepCharcoal.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(8.dp))

            // Session mode cards
            SessionMode.values().forEach { mode ->
                SessionModeCard(
                    mode = mode,
                    onClick = {
                        onModeSelected(mode)
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel button
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    color = DeepCharcoal.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Individual session mode card
 */
@Composable
private fun SessionModeCard(
    mode: SessionMode,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon - Changed from property to method call
                Text(
                    text = mode.getIcon(),  // ✅ Changed from mode.icon
                    fontSize = 40.sp
                )

                // Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = mode.getDisplayName(),  // ✅ Changed from mode.displayName
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepCharcoal
                    )

                    Text(
                        text = mode.getDescription(),  // ✅ Changed from mode.description
                        fontSize = 13.sp,
                        color = DeepCharcoal.copy(alpha = 0.7f),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}