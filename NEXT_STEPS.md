# 🚀 Leadership Coach - Product Roadmap

## 🎯 Current Status
-   **Core**: Live Transcription & Coaching (Stable)
-   **UI**: 
    -   ✅ **Premium Redesign**: "WOW" factor achieved.
    -   ✅ **Navigation**: Custom "Pop-up" Bottom Bar with Lightbulb FAB (Pill height restored).
    -   ✅ **Theme**: Calm Green Gradient Background.
    -   ✅ **History Tab**: Swipe-to-delete, Date grouping, Gradient cards.
    -   ✅ **Session Insights**: Swipe navigation, Transcript summary, **Trends vs Average**.
-   **Features**:
    -   ✅ **Office Politics Mode**: Specialized coaching.
    -   ✅ **Progress Dashboard**: Real data integration.
    -   ✅ **Background Processing**: Foreground Service implemented.
    -   ✅ **Advanced Nudges**: Filler Word Counter & Personality Detection implemented.
    -   ✅ **Gamification Backend**: Achievements system (Database, Repository, Logic) implemented.

## 🔮 Proposed Future Features

### 1. 🏆 Gamification UI (Next Priority)
*Visualize the achievements.*
-   **UI**: Create an `AchievementsScreen` to display unlocked badges.
-   **Integration**: Link from the "Lightbulb" FAB or Profile.

### 2. 🎭 Roleplay Active Session
*The AI speaks back.*
-   **Logic**: User speaks -> STT -> Gemini (Persona) -> TTS (AI Voice).
-   **UI**: Immersive chat interface with "End Roleplay" button.

### 3. 🎙️ Voice Tone Analysis
*Analyze pitch and emotion.*
-   Requires resolving AudioRecorder conflict with SpeechRecognizer.

## 🛠 Technical Improvements
-   **Unit Tests**: Increase coverage.
-   **CI/CD**: Automate builds.

---
**Next Immediate Step**: Implement the `AchievementsScreen` to visualize the gamification system.
