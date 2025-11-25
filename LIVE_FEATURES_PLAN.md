# 🚀 Live Coaching Features Plan

## Objective
Enhance the "Live" session experience with real-time, visual, and actionable features that align with the "Personal Growth Sanctuary" aesthetic.

---

## 1. 🌊 Live Sentiment Pulse (Visual & Emotional Intelligence)
**Concept**: A subtle, breathing ambient indicator that visualizes the emotional tone of the conversation in real-time. It provides immediate, non-intrusive feedback on the conversation's "temperature".

### Implementation Details
- **Logic**:
    - Enhance `SessionViewModel` to calculate a `sentimentScore` based on the last 5-10 transcript chunks.
    - Use existing `TranscriptChunk` helpers:
        - `isPositive()` / `isEmpathetic()` -> Increases score (Calm/Green).
        - `isDefensive()` / `isStressed()` -> Decreases score (Tense/Red).
        - `isQuestion()` -> Neutral/Engaged (Blue).
- **UI**:
    - Add a **Glassmorphic Orb** or **Wave** in the `ChatScreen` header (behind the "Ready" or "Recording" status).
    - **Animations**:
        - **Calm**: Slow, deep breathing animation (Sage Green / Soft Blue).
        - **Tense**: Faster, erratic pulse (Muted Coral / Orange).
        - **Neutral**: Steady, gentle flow (Warm Taupe).

### User Benefit
Helps the user stay aware of the emotional vibe without reading text, allowing them to self-correct tone instantly.

---

## 2. 🎯 Dynamic Session Goals (Structure & Leadership)
**Concept**: Context-aware checklist items that track the user's progress towards leadership goals specific to the session mode.

### Implementation Details
- **Logic**:
    - Define goals for each `SessionMode`:
        - **1-on-1**: "Build Rapport", "Ask Open Questions", "Agree on Next Steps".
        - **Difficult Conversation**: "State Purpose Clearly", "Listen without Interrupting", "Show Empathy".
    - Update `CoachingEngine` to use Gemini to verify if these goals have been met during periodic analysis.
- **UI**:
    - A **Collapsible Goals Card** in the `ChatScreen` (bottom sheet or floating card).
    - Items automatically check off (✅) when detected by AI.
    - User can also manually check them off.

### User Benefit
Provides structure and ensures key leadership behaviors are demonstrated during the call.

---

## 3. 🗣️ Real-time Speech Pacing (Communication Skills)
**Concept**: A subtle speedometer for speech rate to prevent rushing or dragging.

### Implementation Details
- **Logic**:
    - Calculate Words Per Minute (WPM) in `SessionViewModel` using transcript timestamps.
    - Define optimal range (e.g., 120-150 WPM).
- **UI**:
    - A small, minimalist **Gauge** or **Progress Bar** in the `CoachBottomNavigationBar` or header.
    - **Feedback**:
        - **Too Fast**: "Slow down" icon (Turtle).
        - **Too Slow**: "Pick up pace" icon (Hare).
        - **Optimal**: "Perfect pace" checkmark.

### User Benefit
Improves clarity and executive presence by ensuring the user speaks at a confident, measured pace.

---

## 📝 Execution Plan
1.  **Phase 1: Sentiment Pulse** (High Visual Impact)
    - Implement `sentimentScore` logic.
    - Create `SentimentOrb` component.
    - Integrate into `ChatScreen`.
2.  **Phase 2: Dynamic Goals** (High Utility)
    - Define Goal models.
    - Update AI prompts to check goals.
    - Create `GoalsCard` UI.
3.  **Phase 3: Speech Pacing** (Refinement)
    - Implement WPM calculation.
    - Add UI indicator.

**Recommendation**: Start with **Phase 1 (Sentiment Pulse)** to maximize the "Sanctuary" feel and provide immediate value.
