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
    -   ✅ **Gamification**: Backend & UI (Achievements Screen) implemented.

## 🔮 Proposed Future Features

### 1. 🎭 Roleplay Active Session (Next Priority)
*The AI speaks back.*
-   **Logic**: User speaks -> STT -> Gemini (Persona) -> TTS (AI Voice).
-   **UI**: Immersive chat interface with "End Roleplay" button.

### 2. 🎙️ Voice Tone Analysis
*Analyze pitch and emotion.*
-   Requires resolving AudioRecorder conflict with SpeechRecognizer.

### 3. 🧠 Smart Pause Detection
*Detect long silences or monologues.*
-   Add logic to `CoachingEngine`.

## 💼 Commercialization Track (New)
See `COMMERCIALIZATION_ROADMAP.md` for full details.
1.  **Backend**: Set up API Proxy (Firebase/Node.js).
2.  **Auth**: User Login & Cloud Sync.
3.  **Legal**: Privacy Policy & Terms.
4.  **Monetization**: Subscription Paywall.

---
**Next Immediate Step**: Choose between **Roleplay Feature** (Product) or **Backend Setup** (Commercialization).
