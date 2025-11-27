package com.meetingcoach.leadershipconversationcoach.data.repository

import android.util.Xml
import com.meetingcoach.leadershipconversationcoach.domain.models.RssItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RssRepository @Inject constructor() {

    private val feeds = mapOf(
        "Harvard Business Review" to "http://feeds.hbr.org/harvardbusiness",
        "First Round Review" to "https://review.firstround.com/rss",
        "Leadership Freak" to "https://leadershipfreak.blog/feed",
        "McKinsey Insights" to "https://www.mckinsey.com/featured-insights/rss.xml"
    )

    suspend fun fetchAllFeeds(): List<RssItem> = withContext(Dispatchers.IO) {
        val allItems = mutableListOf<RssItem>()
        
        feeds.forEach { (source, urlString) ->
            try {
                val items = parseFeed(urlString, source)
                allItems.addAll(items)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // Sort by date (simple string sort for now, ideally parse dates)
        // Since date formats vary wildly in RSS, we'll just shuffle or keep order for now
        // A robust app would parse RFC822 dates.
        allItems.shuffled() // Shuffle for variety in the "Daily Wisdom" feed
    }

    private fun parseFeed(urlString: String, source: String): List<RssItem> {
        val items = mutableListOf<RssItem>()
        val url = URL(urlString)
        val parser = Xml.newPullParser()
        
        try {
            url.openStream().use { inputStream ->
                parser.setInput(inputStream, null)
                var eventType = parser.eventType
                
                var currentTitle: String? = null
                var currentDescription: String? = null
                var currentLink: String? = null
                var currentPubDate: String? = null
                var isInsideItem = false
                
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val name = parser.name
                    
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            if (name.equals("item", ignoreCase = true) || name.equals("entry", ignoreCase = true)) {
                                isInsideItem = true
                            } else if (isInsideItem) {
                                when {
                                    name.equals("title", ignoreCase = true) -> currentTitle = readText(parser)
                                    name.equals("description", ignoreCase = true) || name.equals("summary", ignoreCase = true) -> currentDescription = readText(parser)
                                    name.equals("link", ignoreCase = true) -> {
                                        // Atom feeds use attributes for href
                                        currentLink = parser.getAttributeValue(null, "href") ?: readText(parser)
                                    }
                                    name.equals("pubDate", ignoreCase = true) || name.equals("published", ignoreCase = true) -> currentPubDate = readText(parser)
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            if (name.equals("item", ignoreCase = true) || name.equals("entry", ignoreCase = true)) {
                                isInsideItem = false
                                if (currentTitle != null && currentLink != null) {
                                    // Clean up description (remove HTML tags)
                                    val cleanDesc = currentDescription?.replace(Regex("<.*?>"), "")?.trim() ?: ""
                                    
                                    items.add(
                                        RssItem(
                                            title = currentTitle,
                                            description = cleanDesc,
                                            link = currentLink,
                                            pubDate = currentPubDate ?: "",
                                            source = source
                                        )
                                    )
                                }
                                currentTitle = null
                                currentDescription = null
                                currentLink = null
                                currentPubDate = null
                            }
                        }
                    }
                    eventType = parser.next()
                }
            }
        } catch (e: Exception) {
            // Log error but return what we have
            e.printStackTrace()
        }
        
        return items
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
}
