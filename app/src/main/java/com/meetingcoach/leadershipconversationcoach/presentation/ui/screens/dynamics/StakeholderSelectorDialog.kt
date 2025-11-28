package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.dynamics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette

@Composable
fun StakeholderSelectorDialog(
    stakeholders: List<Stakeholder>,
    onStakeholderSelected: (Stakeholder) -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Who are you meeting with?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select a stakeholder to get tailored strategic advice.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(stakeholders) { stakeholder ->
                    StakeholderItem(stakeholder) {
                        onStakeholderSelected(stakeholder)
                    }
                }
                
                item {
                    Button(
                        onClick = { /* TODO: Add new stakeholder flow */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Sage100),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AppPalette.Sage900)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Stakeholder", color = AppPalette.Sage900)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onSkip) {
                Text("Skip (Generic Mode)")
            }
        }
    )
}

@Composable
fun StakeholderItem(stakeholder: Stakeholder, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = AppPalette.Sage50),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AppPalette.Sage200, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stakeholder.name.first().toString(),
                    fontWeight = FontWeight.Bold,
                    color = AppPalette.Sage900
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stakeholder.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stakeholder.role,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
