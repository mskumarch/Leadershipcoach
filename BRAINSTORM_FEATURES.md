# 🧠 Feature Brainstorming & Implementation Plan

## 1. 📊 Enhanced Post-Analysis Insights
*Goal: Provide deep, actionable feedback on the user's communication style.*

### A. New Metrics to Track
1.  **Speaking Pace**:
    *   *Metric*: Words per minute (WPM).
    *   *Insight*: "You spoke at 140 WPM (Ideal). Great for clarity." vs "You spoke at 180 WPM (Fast). Try slowing down."
2.  **Wording Style & Power Words**:
    *   *Metric*: Count of "Filler Words" (um, uh, like) vs "Power Words" (strategic, drive, impact).
    *   *Insight*: "You used 'um' 12 times. Try pausing instead."
    *   *Insight*: "You used tentative language ('I think', 'maybe') 40% of the time. Be more decisive."
3.  **Engagement Score**:
    *   *Metric*: Question-to-Statement ratio.
    *   *Insight*: "You asked 1 question for every 10 statements. Try asking more to engage others."

### B. "Places to Improve" Section
*   **Implementation**: Explicitly ask Gemini for "3 Specific Actionable Improvements" based on the transcript.
*   **Display**: A dedicated card in the UI with checkboxes for the user to mark as "Worked on".

### C. UI Update: 3-Tab Layout
To solve the clutter issue, we will split the Session Details into 3 distinct tabs:
1.  **📊 Insights**: Scorecards, Pace, Wording Style, "Places to Improve".
2.  **📝 Transcript**: A clean, read-only view of the conversation text (no AI noise).
3.  **🤖 Coaching Log**: The history of AI nudges and your interactions with the bot.

---

## 2. 🗣️ Speaker Identification (The Hard Problem)
*Current Limitation: The free on-device Android Speech Recognizer cannot distinguish between different voices (Diarization).*

### Option A: Manual "Tagging" (Low Tech, High Accuracy)
*   **How it works**: The app labels speakers as "Speaker A", "Speaker B" (based on pauses).
*   **User Action**: After the session, the user taps "Speaker A" and renames it to "John".
*   **Pros**: Easy to build, free.
*   **Cons**: Tedious for the user.

### Option B: "Team Mode" (Multi-Device Sync) 🌟 **Recommended**
*   **How it works**: Everyone in the meeting opens the app on their *own* phone.
*   **Tech**: Phones connect via Bluetooth/Wifi (Nearby Connections API) or Cloud.
*   **Result**: "John's Phone" sends John's audio. "Sarah's Phone" sends Sarah's audio.
*   **Pros**: Perfect speaker ID, perfect audio quality (everyone has a mic).
*   **Cons**: Requires everyone to have the app.

### Option C: Voice Fingerprinting (High Tech, Hard)
*   **How it works**: We record a 10-second sample of the user's voice ("Profile"). During the meeting, we compare audio chunks to this profile.
*   **Tech**: Requires complex audio processing (TensorFlow Lite audio classification).
*   **Pros**: Magical user experience.
*   **Cons**: Very hard to implement robustly on-device without cloud costs.

---

## 3. 🚀 Other Feature Ideas
1.  **Mood/Sentiment Arc**: A line graph showing how the mood changed from "Tense" to "Calm" over time.
2.  **Key Moments Timeline**: Auto-bookmark moments like "Decision Made", "Conflict Detected", "Great Question".
3.  **PDF Export**: "Generate Executive Summary" to share with the team.

---

## ✅ Proposed Next Steps (Implementation Plan)
1.  **Refactor UI**: Split `SessionDetailScreen` into 3 Tabs (Insights, Transcript, Coaching).
2.  **Update AI**: Modify Gemini prompt to generate "Pace", "Wording Style", and "Improvements".
3.  **Database Update**: Add fields for these new metrics.
