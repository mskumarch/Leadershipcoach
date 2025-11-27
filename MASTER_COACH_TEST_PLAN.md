# Master Coach Redesign - Final Verification Plan

## Objective
Verify that the "Master Coach" persona is correctly implemented across the application, adhering to the principles of "Data-Centric, Not Data-Heavy" and "Cognitive Lightness."

## 1. Live Coach Mode (Ghost Mode)
- [ ] **HUD Fading**: Verify `ChatScreen.kt` has logic to fade `MetricsHUD` and `LiveCoachingControls` to 0.3 alpha when Talk Ratio is between 30-70%.
- [ ] **Nudge Triggering**: Verify `CoachingEngine.kt` triggers `analyzeCommitmentQuality`, `detectMindset`, and `deepenInquiry`.
- [ ] **Visuals**: Verify `NotePanel` and `SentimentIndicator` are present.

## 2. Session Summary (Traffic Light)
- [ ] **Header Logic**: Verify `SessionHealthHeader` in `SessionDetailScreen.kt` correctly classifies sessions (Green/Yellow/Red) based on empathy and clarity scores.
- [ ] **Accordions**: Verify `ExpandableMasterCoachCard` is used for "Session Summary", "Scorecard", "Feedback Loop", and "Next Agenda".
- [ ] **Default State**: Verify only "Game Film" (if added) or "Action Plan" are expanded/highlighted effectively.

## 3. Post-Analysis Tools
- [ ] **One-Tap Follow-Up**: Verify `HistoryViewModel.kt` calls `GeminiApiService.generateFollowUpMessage` and `SessionDetailScreen.kt` displays the result in a Dialog.
- [ ] **Game Film**: Verify `GameFilmTimeline` in `SessionDetailScreen.kt` filters for "NUDGE", "USER_QUESTION", "AI_RESPONSE" and renders markers.

## 4. Analytics (Manager Insights)
- [ ] **Deep Dive**: Verify `ProgressScreen.kt` includes `SpeakingTimeDistributionCard`, `GoalCompletionCard`, and `SentimentTrendCard`.

## 5. End-to-End Flow
- [ ] **Data Flow**: Ensure `SessionMetricsEntity` populates the new UI components correctly.
