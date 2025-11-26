# 🧠 Brainstorming & Analysis

## 1. History Tab Improvements
*   **Delete Session**: ✅ **Essential**. Users need to remove test data or sensitive recordings.
    *   *Implementation*: Add a swipe-to-delete or long-press menu in `HistoryScreen`.
*   **Gradient Cards**: ✅ **High Value**. Aligns with the new premium UI.
    *   *Implementation*: Update `SessionCard` to use `Brush.linearGradient`.
*   **Date Grouping**: ✅ **High Value**. Improves readability as history grows.
    *   *Implementation*: Use a `stickyHeader` in `LazyColumn` grouped by date (e.g., "Today", "Yesterday", "Nov 20").

## 2. Session Insight Improvements
*   **Swipe Navigation**: ✅ **Standard UX**.
    *   *Implementation*: Use `HorizontalPager` with `TabRow`.
*   **Coaching Tab Bug**: 🐞 **Critical Fix**.
    *   *Issue*: It currently shows all messages.
    *   *Fix*: Filter `messages` to exclude `TRANSCRIPT` type in `CoachingTab`.
*   **Transcript Summary**: ✅ **High Value**.
    *   *Implementation*: We already generate a summary in `SessionMetrics`. We can display this at the top of the Transcript tab or as a "Key Takeaways" card.

## 3. Growth / Progress Tab
*   **Real Data**: ✅ **Essential**.
    *   *Current State*: Mock data.
    *   *Plan*: Create `ProgressViewModel` to query `SessionRepository`. Calculate:
        *   **Overall Score**: Average of Empathy/Clarity/Listening from last 30 days.
        *   **Activity**: Count sessions per day.
        *   **Trends**: Compare this week vs last week.

## 4. ⚠️ Technical Challenge: Background Processing
*   **The Problem**: Android aggressively kills background apps, especially those using the microphone. A 60-minute session will almost certainly be killed if the screen turns off or the user switches apps.
*   **The Solution**: **Foreground Service**.
    *   We must implement an Android `Service` with `foregroundServiceType="microphone"`.
    *   This shows a persistent notification (e.g., "Recording Session...").
    *   This ensures the OS keeps the app alive and microphone active.

## 5. Nudges & Real-Time Features
*   **Smart Pause Detection**: ✅ **High Value**.
    *   *Concept*: If user speaks continuously for > 2 mins without a > 2s pause, nudge: "Take a breath. Let others process."
    *   *Implementation*: Track timestamps in `SpeechToTextService`.
*   **Filler Word Counter**: ✅ **Easy Win**.
    *   *Concept*: Count "um", "uh", "like". Nudge if frequency > 5 per minute.
    *   *Implementation*: Regex match in `CoachingEngine`.
*   **Personality Detection**: 🌟 **Star Feature**.
    *   *Concept*: Analyze the *other person's* speech patterns.
        *   *Data-heavy* -> **Analytical** ("Give them numbers").
        *   *Fast/Direct* -> **Driver** ("Be brief").
        *   *Emotional* -> **Expressive** ("Connect personally").
    *   *Implementation*: Add this logic to the Gemini prompt.
*   **Voice Tone Analysis**: ⚠️ **Hard**.
    *   *Constraint*: Requires raw audio processing. We currently disabled `AudioRecorder` due to conflicts.
    *   *Recommendation*: Postpone until Audio/STT conflict is fully resolved.

## 6. Post-Analysis Features
*   **Personality Insights**: Add a section "Communication Style Used" (e.g., "You were 80% Driver, 20% Amiable").
*   **Tone vs Words**: "You said 'I'm fine' but your pitch indicated stress." (Requires Audio).

## 💡 Strategic Recommendation
**Prioritize in this order:**
1.  **Fix Bugs**: Coaching tab display.
2.  **UX Polish**: History gradients, Date grouping, Swipe nav.
3.  **Real Data**: Connect Progress tab to DB.
4.  **Foreground Service**: **CRITICAL** for reliability. Without this, the app is a toy.
5.  **New Features**: Personality Detection (Text-based) & Filler Words.

**Does this plan align with your vision?**
