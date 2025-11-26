package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * Enums.kt - ALL Enum Types (Aligned with SessionMetrics.kt)
 *
 * FINAL VERSION - Matches actual code requirements
 *
 * Architecture: Domain Layer - Models
 * Last Updated: November 2025
 */

// ============================================================
// SESSION ENUMS
// ============================================================

enum class SessionMode {
    ONE_ON_ONE,
    TEAM_MEETING,
    DIFFICULT_CONVERSATION,
    ROLEPLAY,
    OFFICE_POLITICS;

    fun getDisplayName(): String {
        return when (this) {
            ONE_ON_ONE -> "One-on-One"
            TEAM_MEETING -> "Team Meeting"
            DIFFICULT_CONVERSATION -> "Difficult Conversation"
            ROLEPLAY -> "Roleplay Practice"
            OFFICE_POLITICS -> "Office Politics"
        }
    }

    fun getDescription(): String {
        return when (this) {
            ONE_ON_ONE -> "Direct report check-in, mentoring, or feedback"
            TEAM_MEETING -> "Group discussion, brainstorming, or team decision-making"
            DIFFICULT_CONVERSATION -> "Conflict resolution, critical feedback, or tough topics"
            ROLEPLAY -> "Practice scenarios with AI personas"
            OFFICE_POLITICS -> "Navigate power dynamics and strategic situations"
        }
    }

    fun getIcon(): String {
        return when (this) {
            ONE_ON_ONE -> "👤"
            TEAM_MEETING -> "👥"
            DIFFICULT_CONVERSATION -> "💬"
            ROLEPLAY -> "🎭"
            OFFICE_POLITICS -> "♟️"
        }
    }
}

enum class SessionStatus {
    IDLE,
    PREPARING,
    RECORDING,
    PAUSED,
    COMPLETED,
    ERROR;

    fun getDisplayName(): String {
        return when (this) {
            IDLE -> "Idle"
            PREPARING -> "Preparing"
            RECORDING -> "Recording"
            PAUSED -> "Paused"
            COMPLETED -> "Completed"
            ERROR -> "Error"
        }
    }

    fun isActive(): Boolean {
        return this == RECORDING || this == PREPARING
    }
}

enum class ExportFormat {
    PDF,
    JSON,
    MARKDOWN,
    CSV,
    TEXT;

    fun getDisplayName(): String {
        return when (this) {
            PDF -> "PDF Document"
            JSON -> "JSON Data"
            MARKDOWN -> "Markdown"
            CSV -> "CSV Spreadsheet"
            TEXT -> "Plain Text"
        }
    }

    fun getFileExtension(): String {
        return when (this) {
            PDF -> ".pdf"
            JSON -> ".json"
            MARKDOWN -> ".md"
            CSV -> ".csv"
            TEXT -> ".txt"
        }
    }
}

// ============================================================
// METRICS ENUMS
// ============================================================

enum class Pace {
    VERY_SLOW,
    SLOW,
    NORMAL,
    FAST,
    VERY_FAST;

    fun getDisplayName(): String {
        return when (this) {
            VERY_SLOW -> "Very Slow"
            SLOW -> "Slow"
            NORMAL -> "Normal"
            FAST -> "Fast"
            VERY_FAST -> "Very Fast"
        }
    }

    fun getEmoji(): String {
        return when (this) {
            VERY_SLOW -> "🐌"
            SLOW -> "🚶"
            NORMAL -> "⚡"
            FAST -> "🏃"
            VERY_FAST -> "🚀"
        }
    }

    fun getWordsPerMinute(): IntRange {
        return when (this) {
            VERY_SLOW -> 0..100
            SLOW -> 101..130
            NORMAL -> 131..160
            FAST -> 161..190
            VERY_FAST -> 191..300
        }
    }
}

/**
 * Sentiment - Overall conversation mood
 * VALUES MUST MATCH SessionMetrics.kt calculateSentiment() function!
 */
enum class Sentiment {
    CALM,
    FOCUSED,
    STRESSED,
    ANXIOUS;

    fun getDisplayName(): String {
        return when (this) {
            CALM -> "Calm"
            FOCUSED -> "Focused"
            STRESSED -> "Stressed"
            ANXIOUS -> "Anxious"
        }
    }

    fun getEmoji(): String {
        return when (this) {
            CALM -> "😌"
            FOCUSED -> "🎯"
            STRESSED -> "😫"
            ANXIOUS -> "😰"
        }
    }

    fun getColor(): String {
        return when (this) {
            CALM -> "#10B981"      // Green
            FOCUSED -> "#3B82F6"   // Blue
            STRESSED -> "#F59E0B"  // Orange
            ANXIOUS -> "#EF4444"   // Red
        }
    }

    fun getTemperatureValue(): Int {
        return when (this) {
            CALM -> 15
            FOCUSED -> 35
            STRESSED -> 65
            ANXIOUS -> 85
        }
    }
}

// ============================================================
// MESSAGE ENUMS
// ============================================================

enum class MessageType {
    INSTRUCTION,
    TRANSCRIPT,
    CONTEXT,
    URGENT_NUDGE,
    IMPORTANT_PROMPT,
    HELPFUL_TIP,
    USER_QUESTION,
    AI_RESPONSE;

    fun getDisplayName(): String {
        return when (this) {
            INSTRUCTION -> "Instruction"
            TRANSCRIPT -> "Transcript"
            CONTEXT -> "Context"
            URGENT_NUDGE -> "Urgent Nudge"
            IMPORTANT_PROMPT -> "Important Prompt"
            HELPFUL_TIP -> "Helpful Tip"
            USER_QUESTION -> "User Question"
            AI_RESPONSE -> "AI Response"
        }
    }
}

enum class Priority {
    URGENT,
    IMPORTANT,
    HELPFUL,
    INFO;

    fun getDisplayName(): String {
        return when (this) {
            URGENT -> "Urgent"
            IMPORTANT -> "Important"
            HELPFUL -> "Helpful"
            INFO -> "Info"
        }
    }
}

// ============================================================
// SPEAKER ENUMS
// ============================================================

enum class Speaker {
    USER,
    OTHER,
    SYSTEM,
    UNKNOWN;

    fun getDisplayName(): String {
        return when (this) {
            USER -> "You"
            OTHER -> "Other"
            SYSTEM -> "System"
            UNKNOWN -> "Unknown"
        }
    }
}

/**
 * Emotion - Detected emotion in speech
 * NOW HAS getTemperatureValue() method!
 */
enum class Emotion {
    CALM,
    FOCUSED,
    ASSERTIVE,
    DEFENSIVE,
    FRUSTRATED,
    CURIOUS,
    NEUTRAL,
    UNCERTAIN,
    ANXIOUS,
    SUPPORTIVE,
    STRESSED;

    fun getDisplayName(): String {
        return when (this) {
            CALM -> "Calm"
            FOCUSED -> "Focused"
            ASSERTIVE -> "Assertive"
            DEFENSIVE -> "Defensive"
            FRUSTRATED -> "Frustrated"
            CURIOUS -> "Curious"
            NEUTRAL -> "Neutral"
            UNCERTAIN -> "Uncertain"
            ANXIOUS -> "Anxious"
            SUPPORTIVE -> "Supportive"
            STRESSED -> "Stressed"
        }
    }

    fun getEmoji(): String {
        return when (this) {
            CALM -> "😌"
            FOCUSED -> "🎯"
            ASSERTIVE -> "💪"
            DEFENSIVE -> "🛡️"
            FRUSTRATED -> "😤"
            CURIOUS -> "🤔"
            NEUTRAL -> "😐"
            UNCERTAIN -> "🤷"
            ANXIOUS -> "😰"
            SUPPORTIVE -> "🤝"
            STRESSED -> "😫"
        }
    }

    /**
     * Returns temperature contribution (0-100) for conversation temperature gauge
     * Used by SessionMetrics and TranscriptChunk
     */
    fun getTemperatureValue(): Int {
        return when (this) {
            CALM -> 10
            FOCUSED -> 20
            NEUTRAL -> 30
            CURIOUS -> 25
            SUPPORTIVE -> 15
            ASSERTIVE -> 40
            UNCERTAIN -> 45
            FRUSTRATED -> 70
            DEFENSIVE -> 75
            ANXIOUS -> 80
            STRESSED -> 85
        }
    }
}

// ============================================================
// TRANSCRIPTION ENUMS
// ============================================================

enum class TranscriptionQuality {
    STANDARD,  // Free - Android STT
    PREMIUM;   // Paid - Google Cloud STT

    fun getDisplayName(): String {
        return when (this) {
            STANDARD -> "Standard (Free)"
            PREMIUM -> "Premium (Paid)"
        }
    }

    fun getDescription(): String {
        return when (this) {
            STANDARD -> "85-90% accuracy • Works offline • Free"
            PREMIUM -> "95-98% accuracy • Speaker ID • Requires internet"
        }
    }
}