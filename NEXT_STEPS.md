# 🚀 Leadership Coach - Product Roadmap

## 🎯 Current Status
-   **Core**: Live Transcription & Coaching (Stable)
-   **UI**: Clean Slate Theme, Modern Components (Polished)
-   **Stability**: Crash-free, Enterprise Standard Codebase
-   **Features**:
    -   ✅ **Auto-Save**: Sessions are automatically saved to local database.
    -   ✅ **History**: View past sessions with date, duration, and mode.
    -   ✅ **Deep Insights**: Audio-First Analysis with Speaker ID, Pace, and Tone.
    -   ✅ **Speaker ID**: High-quality transcript with speaker labels replaces real-time text.
    -   ✅ **Practice Mode (UI)**: Scenario selection screen is live.

## 🔮 Proposed Future Features

### 1. 🎭 Roleplay & Practice Mode (Active Session)
*The AI speaks back.*
-   **Next Step**: Implement `PracticeSessionScreen` and `PracticeViewModel`.
-   **Logic**: User speaks -> STT -> Gemini (Persona) -> TTS (AI Voice).

### 2. 🏆 Gamification & Progression
*Make learning addictive.*
-   **Streaks & Achievements**: "7-Day Streak", "Master Listener".

## 🛠 Technical Improvements
-   **Unit Tests**: Increase coverage for ViewModels and Domain logic.
-   **CI/CD**: Automate builds and testing.

---
**Next Immediate Step**: Implement the active `PracticeSessionScreen` where the user actually talks to the AI persona.
