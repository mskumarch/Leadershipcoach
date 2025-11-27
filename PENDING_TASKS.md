# Pending Tasks & Future Roadmap

**Date**: 2025-11-27
**Status**: Refactoring Complete (Phases 1-6)

---

## 🚧 Pending UI/UX Polish Items
The following items were listed in the original plan but were not prioritized during the core refactoring:

1.  **Talk Ratio Gauge**: ✅ **COMPLETED**
    -   Implemented `TalkRatioGauge.kt` with circular progress and animations.
    -   Integrated into `ChatScreen`.

2.  **Question Quality Badge**: ✅ **COMPLETED**
    -   Implemented `QuestionQualityBadge.kt` with scale animation.
    -   Integrated into `ChatScreen`.

3.  **Micro-animations**: ✅ **COMPLETED**
    -   Added `animateFloatAsState` to gauges and badges.

---

## 🛠️ Technical Debt / Future Improvements

1.  **Deep Analyst Fields**: ✅ **COMPLETED**
    -   Added fields back to `SessionMetrics` and `AnalyzeSessionUseCase`.

2.  **Unit Test Coverage**:
    -   **Description**: A `TEST_PLAN.md` exists, but automated unit tests for the new `SessionManager` and `CoachingOrchestrator` should be written.

3.  **Localization**: ✅ **COMPLETED**
    -   Extracted strings to `strings.xml` and updated UI components.

---

## 📋 Summary
The core system is robust, stable, and functional. The pending items are primarily visual enhancements and "nice-to-have" features that can be addressed in future sprints.
