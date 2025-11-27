package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.wisdom

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meetingcoach.leadershipconversationcoach.domain.models.RssItem
import com.meetingcoach.leadershipconversationcoach.presentation.ui.theme.AppPalette
import com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.WisdomViewModel

@Composable
fun WisdomScreen(
    modifier: Modifier = Modifier,
    viewModel: WisdomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(AppPalette.Stone50, AppPalette.Stone100)
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Wisdom",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AppPalette.Stone900,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Curated insights for your growth",
            fontSize = 16.sp,
            color = AppPalette.Stone500,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Hero Card (Tip of the Day - First Item)
                if (uiState.articles.isNotEmpty()) {
                    item {
                        HeroWisdomCard(
                            article = uiState.articles.first(),
                            onReadClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uiState.articles.first().link))
                                context.startActivity(intent)
                            },
                            onSummarizeClick = { viewModel.selectArticle(uiState.articles.first()) },
                            onShareClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "💡 Daily Leadership Tip: ${uiState.articles.first().title}\n\nRead more: ${uiState.articles.first().link}")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Insight"))
                            }
                        )
                    }
                }

                // Feed List
                items(uiState.articles.drop(1)) { article ->
                    WisdomFeedItem(
                        article = article,
                        onClick = { viewModel.selectArticle(article) }
                    )
                }
            }
        }
    }

    // Article Detail/Summary Sheet
    if (uiState.selectedArticle != null) {
        val article = uiState.selectedArticle!!
        
        AlertDialog(
            onDismissRequest = { viewModel.clearSelection() },
            title = { Text(text = "Insight Summary") },
            text = {
                Column {
                    Text(text = article.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (uiState.isSummarizing) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        Text("Gemini is reading...", modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (uiState.articleSummary != null) {
                        Text(text = uiState.articleSummary!!, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text(text = article.description, maxLines = 5, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.summarizeArticle(article) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Lavender500)
                        ) {
                            Text("✨ Summarize with AI")
                        }
                    }
                }
            },
            confirmButton = {
                Row {
                    // Share Button
                    TextButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "💡 Leadership Insight:\n\n${uiState.articleSummary ?: article.title}\n\nRead more: ${article.link}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Insight"))
                        }
                    ) {
                        Text("Share")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Read Full Article")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.clearSelection() }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun HeroWisdomCard(
    article: RssItem,
    onReadClick: () -> Unit,
    onSummarizeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppPalette.Sage600),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Badge(containerColor = Color.White.copy(alpha = 0.2f)) {
                Text("TIP OF THE DAY", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.source,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onReadClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AppPalette.Sage600),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Read")
                }
                OutlinedButton(
                    onClick = onSummarizeClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Summarize")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Share Button on Card
            TextButton(
                onClick = onShareClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share with Team", color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
fun WisdomFeedItem(
    article: RssItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.source,
                style = MaterialTheme.typography.labelSmall,
                color = AppPalette.Sage600,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.description, // Usually contains snippet
                style = MaterialTheme.typography.bodySmall,
                color = AppPalette.Stone500,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
