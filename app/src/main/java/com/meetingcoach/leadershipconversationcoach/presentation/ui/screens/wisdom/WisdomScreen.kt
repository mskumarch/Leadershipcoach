package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.wisdom

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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

    // Handle Back Press to close detail view
    BackHandler(enabled = uiState.selectedArticle != null) {
        viewModel.clearSelection()
    }

    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.StandardBackground(
        modifier = modifier
    ) {
        if (uiState.selectedArticle != null) {
            WisdomDetailView(
                article = uiState.selectedArticle!!,
                summary = uiState.articleSummary,
                isSummarizing = uiState.isSummarizing,
                onBack = { viewModel.clearSelection() },
                onSummarize = { viewModel.summarizeArticle(uiState.selectedArticle!!) },
                onReadFull = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uiState.selectedArticle!!.link))
                    context.startActivity(intent)
                },
                onShare = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "💡 Leadership Insight:\n\n${uiState.articleSummary ?: uiState.selectedArticle!!.title}\n\nRead more: ${uiState.selectedArticle!!.link}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Insight"))
                }
            )
        } else {
            WisdomList(
                uiState = uiState,
                onRefresh = { viewModel.loadArticles() },
                onSelectArticle = { viewModel.selectArticle(it) },
                onReadArticle = { article ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                    context.startActivity(intent)
                },
                onShareArticle = { article ->
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "💡 Daily Leadership Tip: ${article.title}\n\nRead more: ${article.link}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Insight"))
                }
            )
        }
    }
}

@Composable
fun WisdomList(
    uiState: com.meetingcoach.leadershipconversationcoach.presentation.viewmodels.WisdomUiState,
    onRefresh: () -> Unit,
    onSelectArticle: (RssItem) -> Unit,
    onReadArticle: (RssItem) -> Unit,
    onShareArticle: (RssItem) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Daily Wisdom",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppPalette.Stone900
                )
                Text(
                    text = "Curated insights for your growth",
                    fontSize = 16.sp,
                    color = AppPalette.Stone500
                )
            }
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Feed",
                    tint = AppPalette.Sage600,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPalette.Sage600)
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
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
                            onReadClick = { onReadArticle(uiState.articles.first()) },
                            onSummarizeClick = { onSelectArticle(uiState.articles.first()) },
                            onShareClick = { onShareArticle(uiState.articles.first()) }
                        )
                    }
                }

                // Feed List
                items(uiState.articles.drop(1)) { article ->
                    WisdomFeedItem(
                        article = article,
                        onClick = { onSelectArticle(article) }
                    )
                }
            }
        }
    }
}

@Composable
fun WisdomDetailView(
    article: RssItem,
    summary: String?,
    isSummarizing: Boolean,
    onBack: () -> Unit,
    onSummarize: () -> Unit,
    onReadFull: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppPalette.Stone900)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Insight Detail",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Meta
            Text(
                text = article.source.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = AppPalette.Sage600,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Title
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = AppPalette.Stone900
            )
            Spacer(modifier = Modifier.height(24.dp))

            // AI Summary Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AppPalette.Stone50),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppPalette.Stone300)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✨", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI EXECUTIVE SUMMARY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppPalette.Lavender500
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSummarizing) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = AppPalette.Lavender500
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Analyzing article...", color = AppPalette.Stone500)
                        }
                    } else if (summary != null) {
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppPalette.Stone900,
                            lineHeight = 24.sp
                        )
                    } else {
                        Text(
                            text = "Get the key takeaways without reading the whole article.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppPalette.Stone500
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onSummarize,
                            colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Lavender500),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Generate Summary")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Original Snippet
            Text(
                text = "Original Snippet",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppPalette.Stone900
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                color = AppPalette.Stone700,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onReadFull,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPalette.Sage600)
                ) {
                    Text("Read Full Article")
                }
                OutlinedButton(
                    onClick = onShare,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppPalette.Sage600)
                ) {
                    Text("Share")
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding
        }
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumStyles.StandardCardRadius),
                spotColor = AppPalette.Sage600.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumStyles.StandardCardRadius),
        colors = CardDefaults.cardColors(containerColor = AppPalette.Sage600),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
    com.meetingcoach.leadershipconversationcoach.presentation.ui.components.PremiumCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
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
