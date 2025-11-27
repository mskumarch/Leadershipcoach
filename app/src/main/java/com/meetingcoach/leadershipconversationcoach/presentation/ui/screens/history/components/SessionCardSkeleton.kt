package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.meetingcoach.leadershipconversationcoach.presentation.ui.components.ShimmerBox

@Composable
fun SessionCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon placeholder
                    ShimmerBox(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Title placeholder
                        ShimmerBox(
                            modifier = Modifier.size(width = 120.dp, height = 20.dp)
                        )
                        // Date placeholder
                        ShimmerBox(
                            modifier = Modifier.size(width = 160.dp, height = 14.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Chip placeholder 1
                ShimmerBox(
                    modifier = Modifier.size(width = 80.dp, height = 32.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                // Chip placeholder 2
                ShimmerBox(
                    modifier = Modifier.size(width = 80.dp, height = 32.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
