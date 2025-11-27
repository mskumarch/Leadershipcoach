# Test Plan: 1:1 Coaching System Refactor

**Date**: 2025-11-27
**Objective**: Verify the stability, functionality, and performance of the refactored 1:1 Coaching System, specifically focusing on the new Multi-Agent Architecture.

---

## 1. Unit Tests (Architecture Verification)

### 1.1 Dependency Injection
- [ ] Verify `CoachingEngine` is correctly injected into `SessionManager`.
- [ ] Verify `SessionManager` is correctly injected into `SessionViewModel`.
- [ ] Verify `AnalyzeSessionUseCase` is correctly injected into `SessionViewModel`.

### 1.2 Component Isolation
- [ ] **SessionManager**: Test `startSession` and `stopSession` lifecycle without UI.
- [ ] **CoachingOrchestrator**: Test `addMessage` and internal state updates.
- [ ] **AnalyzeSessionUseCase**: Test analysis logic with mocked `CoachingEngine`.

---

## 2. Manual Integration Tests (End-to-End)

### 2.1 Session Lifecycle
- [ ] **Start Session**:
    - Grant microphone permission.
    - Start 1:1 Session.
    - Verify "Recording" state in UI.
    - Verify Foreground Service notification appears.
- [ ] **Pause/Resume**:
    - Pause session. Verify timer stops.
    - Resume session. Verify timer resumes.
- [ ] **Stop Session**:
    - Stop session.
    - Verify "Analyzing..." state (if applicable) or immediate save.
    - Verify session is saved to History.

### 2.2 Real-Time Features
- [ ] **Speech-to-Text**:
    - Speak into the microphone.
    - Verify real-time transcript appears in Chat.
- [ ] **Metrics**:
    - Verify Talk Ratio updates in real-time.
    - Verify Progress Rings update.
- [ ] **Orchestrator (AI Agents)**:
    - **Whisperer**: Wait for a pause or ask "What should I ask?". Verify suggested question appears.
    - **Guardian**: Interrupt frequently or speak too fast. Verify nudge appears.
    - **Navigator**: Verify "GROW Stage" updates (Goal -> Reality -> Options -> Will).

### 2.3 Post-Session Analysis
- [ ] **Analysis**:
    - After stopping, verify "Session saved with AI Insights" message.
    - Check History screen for the new session.
    - Verify Summary, Sentiment, and Improvements are populated.

### 2.4 Error Handling
- [ ] **Permission Denied**: Deny microphone permission. Verify error message.
- [ ] **Network Error**: Turn off WiFi/Data. Start session. Verify graceful degradation (local recording only).

---

## 3. Performance & Stability

- [ ] **Memory Leak Check**: Start/Stop session 5 times. Monitor memory usage.
- [ ] **Background Execution**: Put app in background while recording. Verify recording continues.
- [ ] **Battery Usage**: Monitor battery drain during a 10-minute session.

---

## 4. Regression Testing

- [ ] **History Screen**: Verify old sessions still load correctly.
- [ ] **Settings**: Verify changing "Analysis Interval" updates the engine config.
- [ ] **Onboarding**: Verify new user flow is not broken.

---

## 5. Execution Log

| Test Case | Status | Notes |
|-----------|--------|-------|
| 1.1 DI | ✅ Pass | Verified via Build |
| 2.1 Lifecycle | Pending | |
| 2.2 Real-Time | Pending | |
| 2.3 Analysis | Pending | |
