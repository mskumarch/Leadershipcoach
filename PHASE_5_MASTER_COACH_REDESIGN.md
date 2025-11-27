# Phase 5: Master Coach & Mentorship Architect Redesign

## Overview
This phase focuses on transforming the app into a high-performance executive coaching tool. The design and features will be updated to reflect a "Master Coach" persona, emphasizing clarity, structured mentoring, and actionable insights.

## Design Philosophy
*   **Persona**: Senior Leadership Coach, Organizational Effectiveness Partner.
*   **Style**: Soft gradients (Blue/Purple/Sage), Glassmorphism, Rounded corners (24-32dp), Minimal shadows.
*   **Language**: Direct, concise, fact-driven, framework-based (GROW, SBI).

## Key Components & Screens

### 1. Home Screen ("Command Center")
*   **Design**: Soft gradient background, Glassmorphic cards.
*   **Content**: Quick access to start sessions, view recent insights, and track progress.

### 2. Session Start (Live Coach Mode)
*   **Screen**: `ChatScreen.kt`
*   **New Features**:
    *   **Note Panels**: Pill-shaped selectors for Wins, Challenges, Feedback, Goals, Action Items.
    *   **Sentiment Indicator**: Subtle indicator (Calm, Neutral, Concern, Engaged).
    *   **Insights & Recommendations**: AI-driven suggestions (Conversation quality, Red flags, Coaching opportunities).

### 3. Session Summary
*   **Screen**: `SessionDetailScreen.kt`
*   **Timing**: Delivered immediately after session.
*   **Sections**:
    *   Summary of discussion.
    *   Strengths acknowledged.
    *   Areas to improve.
    *   Decisions made.
    *   Action items (Owners & Due dates).
    *   Next 1-1 recommended agenda.

### 4. Backend Logic & AI Analysis (Completed)
- [x] **Commitment Quality Analysis**: Detect vague commitments ("I'll try") vs strong ones ("I will by Friday").
- [x] **Mindset Detection**: Identify Fixed vs Growth mindset language and suggest reframes.
- [x] **Deep Inquiry (The "Why" Ladder)**: Suggest follow-up questions to dig deeper into root causes.
- [x] **Integration**: Updated `CoachingEngine` to run these advanced checks alongside standard analysis.

## 5. Analytics (Manager Insights)
*   **Screen**: `ProgressScreen.kt`
*   **Metrics**:
    *   Speaking time distribution.
    *   Goal completion rate.
    *   Feedback categories.
    *   Sentiment over time.
    *   Progress trajectory.
*   **Design**: Minimalist charts, Soft blue/purple gradient background, Rounded tiles.

## 6. Final Polish (Completed)
- [x] **Session Summary**: Implemented "Traffic Light" header for instant health check.
- [x] **Cognitive Load**: Converted summary cards to "Expandable Accordions" to hide detail until needed.
- [x] **Ghost Mode**: Implemented fade-out logic for Live Coach HUD when metrics are healthy (30-70% Talk Ratio).
- [x] **Refinement**: Polished `SessionDetailScreen` and `ChatScreen` for better visual hierarchy.

## 7. Next Steps
- [x] **One-Tap Follow-Up**: Implement AI generation of accountability emails.
- [ ] **Game Film**: Map audio timestamps to key moments.
- [ ] **Testing**: Verify "Master Coach" persona across all screens.

### Step 1: Live Coach Mode (`ChatScreen.kt`)
*   Add `NotePanel` component with pill selectors.
*   Add `SentimentIndicator` component.
*   Integrate AI insights into the live feed or a dedicated panel.

### Step 2: Session Summary (`SessionDetailScreen.kt`)
*   Refactor `InsightsTab` to include the new sections (Decisions, Action Items, Next Agenda).
*   Update `InsightCard` design to match the "Master Coach" style.

### Step 3: Analytics (`ProgressScreen.kt`)
*   Update `MetricCard` and `ActivityChartCard` to show the new metrics.
*   Apply the soft blue/purple gradient background.

### Step 4: Home Screen
*   Ensure consistency with the new design language.

## Next Steps
*   Start with **Step 1: Live Coach Mode**.
