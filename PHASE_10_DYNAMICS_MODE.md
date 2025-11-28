# Phase 10: Dynamics Mode (Strategic Intelligence)

## 🎯 Objective
Build a specialized session mode called **"Dynamics Mode"** designed to help leaders navigate complex, politically charged, or high-stakes conversations. Unlike standard coaching (which focuses on *your* empathy/clarity), this mode focuses on **decoding the other party's intent, alignment, and hidden resistance.**

---

## 🏗️ Phased Implementation Plan

### Phase 1: The "Real-Time Decoder" Foundation
**Goal:** Establish the technical pipeline for analyzing intent on the fly.
*   **New Session Mode**: Add `DYNAMICS` to the session type selector (alongside 1:1, Practice).
*   **The "Rolling Buffer" Engine**:
    *   Implement a system to analyze the last 30-45 seconds of transcript in real-time.
    *   **AI Prompt Engineering**: Create a specialized system prompt focused on *Linguistic Tells* (e.g., "We'll see," "Let's take this offline," "I'm not sure if...").
*   **UI - The "Radar" Interface**:
    *   **Alignment Meter**: A live, pulsing gauge (0-100%) showing "Current Alignment."
        *   *Green*: Strong Agreement.
        *   *Yellow*: Hesitation / Vague.
        *   *Red*: Resistance / Deflection.
    *   **Visual Style**: Use a "Radar" or "Sonar" aesthetic (concentric circles) consistent with the premium Sage theme.

### Phase 2: Strategic Nudges (The "Co-Pilot")
**Goal:** Give the user actionable advice *during* the meeting to change the outcome.
*   **Deflection Detector**:
    *   *Trigger*: User asks a direct question -> Peer gives a long, vague answer without a "Yes/No".
    *   *Nudge*: "⚠️ Deflection detected. Re-ask the question directly."
*   **The "Pocket Veto" Alert**:
    *   *Trigger*: Peer says "Let's circle back" or "Let's take this offline" on a critical decision.
    *   *Nudge*: "🛑 Risk of stalling. Ask: 'What is the specific blocker?'"
*   **Commitment Checker**:
    *   *Trigger*: Peer agrees but gives no timeline/owner.
    *   *Nudge*: "⚓ Anchor it. Ask: 'Who will own this by when?'"
*   **Silence Strategy**:
    *   *Trigger*: User talks for >2 mins without a check-in.
    *   *Nudge*: "🤫 Pause. Ask: 'How does that land with you?'"

### Phase 3: The "Shadow Report" (Post-Session)
**Goal:** A specialized insights view that analyzes the *power dynamics*.
*   **Alignment Map**: A timeline showing where you had them and where you lost them.
*   **Subtext Decoder**: Highlight specific transcript lines where the AI detected hidden meaning.
    *   *Example*: "I'll try my best" -> *AI Note: Low commitment signal.*
*   **Power Dynamics Score**: Who controlled the flow? Who interrupted whom?

### Phase 4: Advanced "Stakeholder Profiles" (Future)
*   **Peer Memory**: The app remembers that "John" tends to be passive-aggressive about budgets.
*   **Pre-Game Prep**: "You are meeting with Sarah. Last time, she stalled on the timeline. Be ready to push for dates."

---

## 🎨 Design & Influence (Premium Aesthetic)
We will leverage the existing **Glass/Sage** system but introduce a **"Strategic" Accent Color** (perhaps a deep **Royal Indigo** or **Slate Blue**) to differentiate this from the "Empathetic" green of standard coaching.

### Unique UI Elements
1.  **The "Pulse" Orb**: Instead of the breathing circle, a "Radar Sweep" animation.
2.  **Glass Cards**: Darker, more "Heads-Up Display" (HUD) style for nudges to feel urgent but professional.

---

## 🚀 Next Steps (Immediate)
1.  **Phase 3: The "Shadow Report"**:
    *   Create `DynamicsSessionDetailScreen` to visualize the analysis post-session.
    *   Implement "Subtext Decoder" list (showing the detected signals in the transcript).
    *   Add "Power Dynamics Score" to the summary.

## ✅ Completed
*   **Phase 1: The "Real-Time Decoder" Foundation**:
    *   Added `DYNAMICS` session mode.
    *   Created `DynamicsRecordingScreen` with Radar UI.
    *   Implemented `AnalyzeDynamicsUseCase` ("The Brain").
*   **Phase 2: Strategic Nudges**:
    *   Implemented real-time detection of Deflection, Vague Commitments, and Resistance.
    *   Connected analysis to the UI (Alignment Meter & Nudges).
