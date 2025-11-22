package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * TranscriptChunk.kt - Real-Time Speech Data Model
 *
 * Represents a single chunk of transcribed speech from the Speech-to-Text service.
 * This is the primary data structure used during live session recording.
 *
 * Used by:
 * - LocalSpeechToTextService (populates this model)
 * - TranscriptViewModel (displays in real-time)
 * - CoachingEngine (analyzes for AI nudges)
 * - SessionRepository (persists to database)
 *
 * Clean Architecture: Domain Layer - Core Business Model
 *
 * Created for: STT Backend Integration
 * Last Updated: November 2025
 */

import java.util.UUID

/**
 * Represents a single transcribed speech segment
 *
 * @property id Unique identifier for this transcript chunk
 * @property text The transcribed text content
 * @property speaker Who spoke this chunk (USER, OTHER, SYSTEM, UNKNOWN)
 * @property timestamp Unix timestamp in milliseconds when this was spoken
 * @property confidence Confidence score from STT (0.0 to 1.0, where 1.0 = 100% confident)
 * @property emotion Detected emotion/tone (optional, may be null during live transcription)
 * @property duration Duration of this speech segment in milliseconds
 * @property isPartial True if this is a partial result (still being transcribed), false if final
 * @property rawAudioData Optional reference to raw audio data (for future features)
 *
 * Example Usage:
 * ```
 * val chunk = TranscriptChunk(
 *     text = "I think we should consider the budget carefully",
 *     speaker = Speaker.USER,
 *     confidence = 0.92f,
 *     emotion = Emotion.UNCERTAIN
 * )
 * ```
 */
data class TranscriptChunk(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val speaker: Speaker = Speaker.UNKNOWN,
    val timestamp: Long = System.currentTimeMillis(),
    val confidence: Float = 1.0f,
    val emotion: Emotion? = null,
    val duration: Long = 0L,
    val isPartial: Boolean = false,
    val rawAudioData: ByteArray? = null
) {

    /**
     * Returns true if this chunk has high confidence (>= 80%)
     */
    fun isHighConfidence(): Boolean = confidence >= 0.8f

    /**
     * Returns true if this chunk has low confidence (< 60%)
     * Low confidence chunks may need manual review or re-transcription
     */
    fun isLowConfidence(): Boolean = confidence < 0.6f

    /**
     * Returns confidence as a percentage (0-100)
     */
    fun getConfidencePercentage(): Int = (confidence * 100).toInt()

    /**
     * Returns true if this chunk is likely meaningful speech
     * Filters out very short utterances and low confidence results
     */
    fun isMeaningful(): Boolean {
        return text.trim().length > 3 && // More than 3 characters
                !isPartial && // Final result
                confidence >= 0.5f // At least 50% confidence
    }

    /**
     * Returns the word count in this chunk
     */
    fun getWordCount(): Int = text.trim().split("\\s+".toRegex()).size

    /**
     * Returns true if this chunk is attributed to the user
     */
    fun isFromUser(): Boolean = speaker == Speaker.USER

    /**
     * Returns true if this chunk is attributed to another person
     */
    fun isFromOther(): Boolean = speaker == Speaker.OTHER

    /**
     * Returns true if this chunk has detected emotion
     */
    fun hasEmotion(): Boolean = emotion != null

    /**
     * Returns a formatted string representation for logging/debugging
     */
    fun toLogString(): String {
        return "[${speaker.getDisplayName()}] (${getConfidencePercentage()}%) ${if (isPartial) "[PARTIAL]" else ""} $text"
    }

    /**
     * Returns a display-friendly version of the timestamp
     * Format: MM:SS
     */
    fun getFormattedTimestamp(sessionStartTime: Long): String {
        val elapsedMs = timestamp - sessionStartTime
        val seconds = (elapsedMs / 1000) % 60
        val minutes = (elapsedMs / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    // ============================================================
    // EMOTION ANALYSIS HELPERS
    // ============================================================

    /**
     * Returns true if the detected emotion indicates stress/tension
     */
    fun isStressed(): Boolean {
        return emotion in listOf(
            Emotion.STRESSED,
            Emotion.ANXIOUS,
            Emotion.FRUSTRATED,
            Emotion.DEFENSIVE
        )
    }

    /**
     * Returns true if the detected emotion indicates positive tone
     */
    fun isPositive(): Boolean {
        return emotion in listOf(
            Emotion.CALM,
            Emotion.SUPPORTIVE,
            Emotion.CURIOUS
        )
    }

    /**
     * Returns the temperature contribution of this chunk (0-100)
     * Used for conversation temperature gauge
     */
    fun getTemperatureContribution(): Int {
        return emotion?.getTemperatureValue() ?: 30 // Default neutral
    }

    // ============================================================
    // PATTERN DETECTION HELPERS
    // ============================================================

    /**
     * Returns true if this chunk contains a question
     */
    fun isQuestion(): Boolean {
        return text.trim().endsWith("?") ||
                text.lowercase().startsWith("what ") ||
                text.lowercase().startsWith("how ") ||
                text.lowercase().startsWith("why ") ||
                text.lowercase().startsWith("when ") ||
                text.lowercase().startsWith("where ") ||
                text.lowercase().startsWith("who ") ||
                text.lowercase().startsWith("can ") ||
                text.lowercase().startsWith("could ") ||
                text.lowercase().startsWith("would ")
    }

    /**
     * Returns true if this chunk seems to be an open-ended question
     * (Better for coaching than yes/no questions)
     */
    fun isOpenEndedQuestion(): Boolean {
        if (!isQuestion()) return false

        val lowerText = text.lowercase()
        return lowerText.startsWith("what ") ||
                lowerText.startsWith("how ") ||
                lowerText.startsWith("why ") ||
                lowerText.contains("tell me about") ||
                lowerText.contains("tell me more")
    }

    /**
     * Returns true if this chunk contains empathetic language
     */
    fun isEmpathetic(): Boolean {
        val empatheticPhrases = listOf(
            "i understand",
            "i hear you",
            "that makes sense",
            "i appreciate",
            "thank you",
            "i see",
            "that's valid",
            "i can imagine",
            "tell me more",
            "how does that feel",
            "what concerns you"
        )

        val lowerText = text.lowercase()
        return empatheticPhrases.any { lowerText.contains(it) }
    }

    /**
     * Returns true if this chunk contains defensive language
     */
    fun isDefensive(): Boolean {
        val defensivePhrases = listOf(
            "but ",
            "actually ",
            "you don't understand",
            "that's not true",
            "i didn't",
            "it's not my fault",
            "you're wrong",
            "no offense but"
        )

        val lowerText = text.lowercase()
        return defensivePhrases.any { lowerText.contains(it) }
    }

    // ============================================================
    // EQUALS & HASHCODE (ByteArray handling)
    // ============================================================

    /**
     * Custom equals implementation to properly compare ByteArray
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TranscriptChunk

        if (id != other.id) return false
        if (text != other.text) return false
        if (speaker != other.speaker) return false
        if (timestamp != other.timestamp) return false
        if (confidence != other.confidence) return false
        if (emotion != other.emotion) return false
        if (duration != other.duration) return false
        if (isPartial != other.isPartial) return false
        if (rawAudioData != null) {
            if (other.rawAudioData == null) return false
            if (!rawAudioData.contentEquals(other.rawAudioData)) return false
        } else if (other.rawAudioData != null) return false

        return true
    }

    /**
     * Custom hashCode implementation to properly handle ByteArray
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + speaker.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + confidence.hashCode()
        result = 31 * result + (emotion?.hashCode() ?: 0)
        result = 31 * result + duration.hashCode()
        result = 31 * result + isPartial.hashCode()
        result = 31 * result + (rawAudioData?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * Extension function to convert a list of TranscriptChunks to a single text string
 * Useful for passing to AI analysis
 */
fun List<TranscriptChunk>.toText(): String {
    return this.joinToString("\n") { chunk ->
        "${chunk.speaker.getDisplayName()}: ${chunk.text}"
    }
}

/**
 * Extension function to filter chunks by speaker
 */
fun List<TranscriptChunk>.filterBySpeaker(speaker: Speaker): List<TranscriptChunk> {
    return this.filter { it.speaker == speaker }
}

/**
 * Extension function to get total word count across chunks
 */
fun List<TranscriptChunk>.getTotalWordCount(): Int {
    return this.sumOf { it.getWordCount() }
}

/**
 * Extension function to get chunks within a time range
 */
fun List<TranscriptChunk>.getChunksInTimeRange(
    startTime: Long,
    endTime: Long
): List<TranscriptChunk> {
    return this.filter { it.timestamp in startTime..endTime }
}

/**
 * Extension function to get the most recent chunk
 */
fun List<TranscriptChunk>.getMostRecent(): TranscriptChunk? {
    return this.maxByOrNull { it.timestamp }
}

/**
 * Extension function to calculate average confidence across chunks
 */
fun List<TranscriptChunk>.getAverageConfidence(): Float {
    if (this.isEmpty()) return 0f
    return this.map { it.confidence }.average().toFloat()
}