package com.meetingcoach.leadershipconversationcoach.presentation.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.meetingcoach.leadershipconversationcoach.data.repository.SessionWithDetails
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import org.json.JSONArray

@Composable
fun PrepContextDialog(
    sessionWithDetails: SessionWithDetails,
    onDismiss: () -> Unit,
    onStartSession: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Smart Prep",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Stone900
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Context from your last session",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppPalette.Stone500
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Summary
                if (!sessionWithDetails.metrics?.summary.isNullOrBlank()) {
                    Text(
                        text = "SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Sage600,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = sessionWithDetails.metrics?.summary ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppPalette.Stone700
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Action Items
                val actionItems = try {
                    val json = sessionWithDetails.metrics?.actionItemsJson
                    if (!json.isNullOrBlank()) {
                        val array = JSONArray(json)
                        List(array.length()) { array.getString(it) }
                    } else emptyList()
                } catch (e: Exception) { emptyList() }

                if (actionItems.isNotEmpty()) {
                    Text(
                        text = "OPEN ACTION ITEMS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppPalette.Red500,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    actionItems.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", color = AppPalette.Red500)
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppPalette.Stone700
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Button(
                    onClick = onStartSession,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Sage600),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Start Session with Context")
                }
                
                TextButton(onClick = onDismiss) {
                    Text("Close", color = AppPalette.Stone500)
                }
            }
        }
    }
}
