package com.meetingcoach.leadershipconversationcoach.domain.models

data class RssItem(
    val title: String,
    val description: String,
    val link: String,
    val pubDate: String,
    val source: String,
    val imageUrl: String? = null
)
