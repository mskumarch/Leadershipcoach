package com.meetingcoach.leadershipconversationcoach.presentation.ui.screens.coach.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meetingcoach.leadershipconversationcoach.domain.models.SessionMode

data class CoachingTip(
    val icon: String,
    val title: String,
    val tip: String,
    val category: String
)

@Composable
fun CoachingTipsCarousel(
    sessionMode: SessionMode,
    modifier: Modifier = Modifier
) {
    val tips = remember(sessionMode) { getCoachingTips(sessionMode) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💡 Quick Tips",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Text(
                text = sessionMode.getDisplayName(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9575CD)
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tips) { tip ->
                CoachingTipCard(tip = tip)
            }
        }
    }
}

@Composable
private fun CoachingTipCard(tip: CoachingTip) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Card(
        modifier = Modifier
            .width(280.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4DB6AC).copy(alpha = shimmerAlpha),
                                Color(0xFF9575CD).copy(alpha = shimmerAlpha)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF4DB6AC).copy(alpha = 0.2f),
                                        Color(0xFF9575CD).copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tip.icon,
                            fontSize = 24.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = tip.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF9575CD),
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = tip.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }
                }

                Text(
                    text = tip.tip,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private fun getCoachingTips(mode: SessionMode): List<CoachingTip> {
    return when (mode) {
        SessionMode.ONE_ON_ONE -> listOf(
            CoachingTip(
                icon = "👂",
                title = "Listen 70% of the time",
                tip = "Great 1-on-1s happen when you listen more than you speak. Aim for 60-70% listening.",
                category = "LISTENING"
            ),
            CoachingTip(
                icon = "❓",
                title = "Ask open questions",
                tip = "Start with 'What' and 'How' instead of yes/no questions. Opens deeper conversations.",
                category = "QUESTIONS"
            ),
            CoachingTip(
                icon = "💚",
                title = "Acknowledge feelings first",
                tip = "Before solving problems, acknowledge how they feel. This builds trust.",
                category = "EMPATHY"
            ),
            CoachingTip(
                icon = "⏸️",
                title = "Pause before responding",
                tip = "Wait 2-3 seconds after they finish. Shows you're thinking and gives them space to add more.",
                category = "PACING"
            ),
            CoachingTip(
                icon = "🎯",
                title = "Follow their energy",
                tip = "Let them guide the conversation direction. Your job is to facilitate, not direct.",
                category = "APPROACH"
            )
        )
        SessionMode.TEAM_MEETING -> listOf(
            CoachingTip(
                icon = "🎤",
                title = "Ensure everyone speaks",
                tip = "Track who hasn't contributed. Actively invite quiet members: 'Sarah, what's your take?'",
                category = "INCLUSION"
            ),
            CoachingTip(
                icon = "⚖️",
                title = "Balance air time",
                tip = "No one person should dominate. Politely redirect: 'Thanks John, let's hear from others too.'",
                category = "FACILITATION"
            ),
            CoachingTip(
                icon = "🤔",
                title = "Seek diverse views",
                tip = "When consensus comes quickly, ask: 'Does anyone see this differently?' to avoid groupthink.",
                category = "DIVERSITY"
            ),
            CoachingTip(
                icon = "📍",
                title = "Signpost transitions",
                tip = "Clearly mark topic changes: 'Great. Let's move to the next item.' Keeps everyone aligned.",
                category = "STRUCTURE"
            ),
            CoachingTip(
                icon = "🔄",
                title = "Summarize regularly",
                tip = "Every 10-15 minutes, recap key points and check understanding before moving forward.",
                category = "CLARITY"
            )
        )
        SessionMode.DIFFICULT_CONVERSATION -> listOf(
            CoachingTip(
                icon = "🧘",
                title = "Stay calm and centered",
                tip = "Take deep breaths. Your calm presence helps de-escalate tension. Lead by example.",
                category = "REGULATION"
            ),
            CoachingTip(
                icon = "🛡️",
                title = "Spot defensiveness early",
                tip = "Listen for 'but', 'actually', 'you don't understand'. These signal defensive mode.",
                category = "AWARENESS"
            ),
            CoachingTip(
                icon = "💭",
                title = "Seek to understand first",
                tip = "Before sharing your view, deeply understand theirs: 'Help me see what you're seeing.'",
                category = "EMPATHY"
            ),
            CoachingTip(
                icon = "🤝",
                title = "Find common ground",
                tip = "Even in conflict, find something you agree on. Start there before addressing differences.",
                category = "CONNECTION"
            ),
            CoachingTip(
                icon = "🎭",
                title = "Lower your voice",
                tip = "When tension rises, speak softer and slower. It naturally de-escalates the other person.",
                category = "DE-ESCALATION"
            )
        )
        SessionMode.ROLEPLAY -> listOf(
            CoachingTip(
                icon = "🎭",
                title = "Stay in character",
                tip = "Immerse yourself in the role. Respond naturally as if it were real.",
                category = "IMMERSION"
            ),
            CoachingTip(
                icon = "🎯",
                title = "Focus on the goal",
                tip = "Remember your objective (e.g., giving feedback). Don't get sidetracked.",
                category = "OBJECTIVE"
            ),
            CoachingTip(
                icon = "🧪",
                title = "Experiment safely",
                tip = "Try different approaches. This is a safe space to fail and learn.",
                category = "LEARNING"
            )
        )
        SessionMode.DYNAMICS -> listOf(
            CoachingTip(
                icon = "♟️",
                title = "Map the players",
                tip = "Identify key stakeholders and their hidden agendas. Who influences whom?",
                category = "STRATEGY"
            ),
            CoachingTip(
                icon = "🤝",
                title = "Build alliances",
                tip = "Focus on shared interests. Help others achieve their goals to build political capital.",
                category = "RELATIONSHIPS"
            ),
            CoachingTip(
                icon = "🛡️",
                title = "Protect your reputation",
                tip = "Be consistent and transparent. Avoid gossip but stay informed.",
                category = "REPUTATION"
            )
        )
    }
}
