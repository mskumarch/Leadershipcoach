package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * Extension functions for TranscriptChunk
 */

/**
 * Check if this transcript is from the user
 */
fun TranscriptChunk.isFromUser(): Boolean {
    return speaker == Speaker.USER
}

/**
 * Check if this transcript contains a question
 */
fun TranscriptChunk.isQuestion(): Boolean {
    return text.trim().endsWith("?")
}

/**
 * Check if this transcript is an open-ended question
 */
fun TranscriptChunk.isOpenEndedQuestion(): Boolean {
    if (!isQuestion()) return false

    val lowerText = text.lowercase().trim()

    // Closed question indicators
    val closedIndicators = listOf(
        "is ", "are ", "do ", "does ", "did ", "will ", "would ",
        "can ", "could ", "should ", "has ", "have "
    )

    return !closedIndicators.any { lowerText.startsWith(it) }
}

/**
 * Get word count
 */
fun TranscriptChunk.wordCount(): Int {
    return text.trim().split("\\s+".toRegex()).size
}

/**
 * Check if contains empathetic language
 */
fun TranscriptChunk.hasEmpatheticLanguage(): Boolean {
    val lowerText = text.lowercase()

    val empatheticPhrases = listOf(
        "i understand", "i hear you", "that makes sense",
        "i appreciate", "tell me more", "how do you feel",
        "that must be", "i can see", "thank you for sharing"
    )

    return empatheticPhrases.any { lowerText.contains(it) }
}