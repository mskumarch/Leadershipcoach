# Refinement & Feature Plan: Commercial Polish

This document outlines the plan to refine the Leadership Coach app into a polished, commercial-grade product.

## Phase 1: The "Active" Experience (Recording & Session Management)
**Goal:** Make the recording screen feel "alive" and give users control.

### 1.1 Live Audio Visualization
- **Current:** Static or simulated pulsing.
- **Plan:** Connect the `WaveformVisualization` component to the `AudioRecorder`'s amplitude.
- **Implementation:**
  - Expose `maxAmplitude` flow from `AudioRecorder`.
  - Observe this flow in `SessionViewModel`.
  - Pass the amplitude value to the `PulsingConcentricCircles` or `WaveformVisualization` composable.

### 1.2 Session Controls (Pause/Resume)
- **Current:** Start -> Stop.
- **Plan:** Add a "Pause" state.
- **Implementation:**
  - Update `SessionState` with `isPaused`.
  - Pause the `Timer` and `SpeechRecognizer`.
  - UI: Toggle "Pause" icon to "Resume" (Play) icon.

### 1.3 Session Titling
- **Current:** No titles (likely auto-generated ID or date).
- **Plan:**
  - **Post-Session Dialog:** Immediately after stopping, show a dialog: "Name this session" (e.g., "Weekly Sync with Sarah").
  - **AI Suggestion:** Use Gemini to suggest a title based on the transcript (e.g., "Performance Review - Q3").
  - **Database:** Add `title` column to `SessionEntity`.

---

## Phase 2: The "Payoff" (Analysis & Insights)
**Goal:** Transform raw text into a beautiful, structured report card.

### 2.1 Structured AI Output
- **Current:** Unstructured text block.
- **Plan:** Update Gemini prompt to return JSON with specific fields:
  - `score_card`: { `empathy`: 8, `clarity`: 7, `listening`: 9 }
  - `summary`: "Brief 2-sentence summary."
  - `key_takeaways`: ["Point 1", "Point 2", "Point 3"]
  - `action_items`: ["Do X", "Schedule Y"]

### 2.2 Visual Report Card UI
- **Plan:** Create a new `SessionDetailScreen` with:
  - **Hero Score:** Large circular progress indicator for the overall "Leadership Score".
  - **Metric Cards:** Smaller cards for Empathy, Clarity, etc.
  - **Takeaways List:** Bulleted list with checkmarks.
  - **Transcript Tab:** Separate tab to view the full conversation text.

### 2.3 Shareable Artifacts
- **Plan:** "Share Report" button.
- **Implementation:** Generate a formatted plain text or simple PDF intent to share via Email/Slack.

---

## Phase 3: History & Long-Term Value
**Goal:** Make the app useful for tracking progress over months.

### 3.1 Search & Filter
- **Plan:** Add a Search Bar to `HistoryScreen`.
- **Implementation:**
  - Filter `SessionEntity` list by `title` or `transcript` content.
  - Add "Filter Chips": "One-on-One", "Team Meeting", "Conflict".

### 3.2 Trend Visualization
- **Plan:** Add a "Progress" tab or section on Dashboard.
- **Implementation:**
  - Line chart showing `empathyScore` and `clarityScore` over the last 10 sessions.
  - "Streak" counter for weekly sessions.

---

## Phase 4: Resilience & Offline Mode
**Goal:** Ensure the app works even when the internet is flaky.

### 4.1 Local Audio Caching
- **Current:** Audio might be discarded or not saved reliably.
- **Plan:** Ensure `AudioRecorder` saves to a permanent file (`Context.filesDir`) immediately.

### 4.2 Offline Queueing
- **Plan:** If Gemini API fails (no internet):
  - Save the session with status `PENDING_ANALYSIS`.
  - Show a "Waiting for connection..." indicator in History.
  - Use `WorkManager` to retry the analysis when the network returns.

### 4.3 Graceful Error Handling
- **Plan:**
  - **"Too Quiet":** If amplitude is low for 10s, show toast: "Speak closer to the mic".
  - **"Too Short":** If session < 15s, warn before analyzing: "Session too short for meaningful analysis."

---

## Phase 5: Speaker Recognition (Diarization)
**Goal:** Distinguish "You" vs. "Them" for accurate coaching.

### 5.1 Technical Feasibility
- **Challenge:** Local `SpeechRecognizer` does not support speaker labels (Diarization).
- **Solution:** We must send the **Audio File** to Gemini (Multimodal), not just text.
- **Pros:** Gemini 1.5 Flash/Pro is excellent at "Speaker A said... Speaker B said...".
- **Cons:** Higher data usage, slower than real-time text streaming.

### 5.2 Implementation Plan
1.  **Record:** Save `.m4a` or `.wav` file locally.
2.  **Upload:** Send the file byte stream to Gemini API.
3.  **Prompt:** "Analyze this audio. Identify Speaker A (Coach) and Speaker B (Employee). Provide transcript and analysis."
4.  **Fallback:** Keep local STT for real-time "Nudges" (visual feedback), but use the Audio Upload for the *final* high-quality analysis.

---

## Next Steps
1.  **Execute Phase 1:** Connect audio visualization and add Session Title.
2.  **Execute Phase 2:** Refine Gemini Prompts for JSON output.
