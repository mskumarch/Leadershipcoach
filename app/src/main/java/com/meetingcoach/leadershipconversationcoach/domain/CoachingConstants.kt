package com.meetingcoach.leadershipconversationcoach.domain

/**
 * Coaching Constants - Research-Based Values
 * 
 * All values derived from evidence-based coaching research:
 * - GROW Model (Whitmore)
 * - High Output Management (Grove)
 * - Active Listening (Rogers)
 * - Socratic Method
 * 
 * See: ONE_ON_ONE_COACHING_RESEARCH.md for sources
 */
object CoachingConstants {
    
    /**
     * Agent Execution Intervals
     */
    object AgentIntervals {
        /** Navigator checks GROW stage every 60 seconds */
        const val NAVIGATOR_INTERVAL_MS = 60_000L
        
        /** Guardian monitors behavior every 30 seconds */
        const val GUARDIAN_INTERVAL_MS = 30_000L
        
        /** Whisperer generates questions on-demand (no interval) */
        // No constant needed - triggered by user action
    }
    
    /**
     * Session Duration Thresholds
     * Source: Grove's High Output Management (minimum 1 hour for depth)
     */
    object SessionDuration {
        /** Minimum recommended session duration (30 minutes) */
        const val MIN_DURATION_MS = 30 * 60 * 1000L // 30 minutes
        
        /** Ideal session duration (60 minutes) */
        const val IDEAL_DURATION_MS = 60 * 60 * 1000L // 1 hour
    }
    
    /**
     * Talk Ratio Thresholds
     * Source: Manager Tools, Grove's 1:1 principles
     */
    object TalkRatio {
        /** Ideal minimum: Manager speaks 30% of time */
        const val IDEAL_MIN_PERCENTAGE = 30
        
        /** Ideal maximum: Manager speaks 40% of time */
        const val IDEAL_MAX_PERCENTAGE = 40
        
        /** Warning threshold: Manager speaks >60% (too much) */
        const val WARNING_THRESHOLD = 60
        
        /** Critical threshold: Manager speaks <20% (disengaged) */
        const val CRITICAL_LOW_THRESHOLD = 20
    }
    
    /**
     * Question Quality Thresholds
     * Source: Socratic Method, Powerful Questions research
     */
    object QuestionQuality {
        /** Ideal: 60%+ open-ended questions */
        const val IDEAL_OPEN_RATIO = 0.6f
        
        /** Warning: 40-60% open-ended */
        const val WARNING_OPEN_RATIO = 0.4f
        
        /** Target: Ask at least 5 questions per session */
        const val MIN_QUESTION_COUNT = 5
    }
    
    /**
     * Interruption Thresholds
     * Source: Active Listening research (Rogers)
     */
    object Interruptions {
        /** Ideal: 0-2 interruptions */
        const val MAX_ACCEPTABLE = 2
        
        /** Warning: 3-5 interruptions */
        const val WARNING_THRESHOLD = 5
        
        /** Critical: >5 interruptions (poor listening) */
        const val CRITICAL_THRESHOLD = 5
    }
    
    /**
     * Empathy Signal Thresholds
     * Source: Rogers' Person-Centered Approach
     */
    object Empathy {
        /** Ideal: 5+ empathetic phrases per 30 minutes */
        const val MIN_SIGNALS_PER_30_MIN = 5
        
        /** Empathetic phrase patterns (for detection) */
        val EMPATHY_PATTERNS = listOf(
            "I understand",
            "That makes sense",
            "Tell me more",
            "I hear you",
            "That sounds",
            "I can see",
            "It seems like"
        )
    }
    
    /**
     * Commitment Thresholds
     * Source: Implementation Intentions research
     */
    object Commitments {
        /** Ideal: 2-3 specific commitments per session */
        const val IDEAL_MIN = 2
        const val IDEAL_MAX = 3
        
        /** Warning: Only 1 commitment */
        const val WARNING_THRESHOLD = 1
    }
    
    /**
     * GROW Model Stage Thresholds
     */
    object GrowModel {
        /** Maximum time to spend in one stage before suggesting transition (minutes) */
        const val MAX_STAGE_DURATION_MIN = 15
        
        /** Minimum time before detecting stage (to avoid false positives) */
        const val MIN_STAGE_DURATION_SEC = 30
    }
    
    /**
     * AI Parsing Defaults
     */
    object AI {
        /** Default score when parsing fails (null is preferred, but this is fallback) */
        const val DEFAULT_SCORE = 50
        
        /** Maximum question length for Whisperer (words) */
        const val MAX_QUESTION_LENGTH_WORDS = 15
        
        /** Maximum nudge message length (words) */
        const val MAX_NUDGE_LENGTH_WORDS = 12
    }
    
    /**
     * Resource Management
     */
    object Resources {
        /** Keep last N audio files (older ones deleted) */
        const val MAX_AUDIO_FILES_RETAINED = 10
        
        /** Delete audio files older than N days */
        const val AUDIO_RETENTION_DAYS = 30
    }
}
