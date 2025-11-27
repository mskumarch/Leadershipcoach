# Phase 6: Immersive Practice & Personalization

## Objective
Transform the "Roleplay Practice" mode from a simple list into an immersive, gamified training ground. Additionally, introduce personalization to tailor the AI coach to the user's leadership style.

## 1. Immersive Practice Mode
- [ ] **UI Redesign**: Apply the "Glassmorphism" and "Mesh Gradient" design system to `PracticeModeScreen`.
- [ ] **Pre-Game Briefing**: Create a "Mission Briefing" screen before a scenario starts:
    - **Context**: "You are meeting with Alex..."
    - **Objective**: "Get Alex to take ownership."
    - **Key Tactics**: "Use open questions, avoid blame."
- [ ] **In-Game Persona**: Ensure the AI strictly adheres to the `aiPersona` (e.g., "Defensive", "Crying", "Angry") using system prompts.
- [ ] **Post-Game Debrief**: A dedicated scorecard for the practice session:
    - **Win/Loss**: Did you achieve the objective?
    - **Skill Check**: Did you use the suggested tactics?

## 2. Personalization (Onboarding)
- [ ] **Leadership Quiz**: A simple 3-question quiz to determine the user's style (e.g., "Servant Leader", "Direct Driver").
- [ ] **Customized Coaching**: Adjust the `CoachingEngine` prompts based on the user's style (e.g., "Direct Driver" gets more empathy nudges; "Servant Leader" gets more assertiveness nudges).

## 3. "Coach's Corner" (Learning Hub)
- [ ] **Framework Library**: A read-only section with quick cards for "STAR Method", "Radical Candor", "GROW Model".
- [ ] **Daily Tips Archive**: A history of the daily tips shown on the home screen.

## 4. Audio Visualization (Polish)
- [ ] **Waveform**: Add a visual audio waveform (even if simulated) to the "Game Film" timeline for a premium feel.
