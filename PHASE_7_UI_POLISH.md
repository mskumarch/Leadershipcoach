# Phase 7: UI Polish & Consistency (Expert Critique)

## Objective
Elevate the app from "functional prototype" to "premium, commercial-grade product" by addressing specific UI/UX inconsistencies and applying a unified design language.

## 1. High-Impact Fixes (Priority)
- [x] **Bottom Navigation**:
    - Replace Emoji icons with **Material Symbols Rounded** (Vector Icons).
    - Apply specific colors: Inactive `#A0B8AD` (40%), Active `#4F7F6B`.
    - Add active state animation (Glow ring + Scale 1.08x).
- [x] **Visual Consistency**:
    - **Background**: Standardize top fade gradient (`#E7F3ED` -> `#FFFFFF`).
    - **Cards**: Enforce **22dp corner radius** and specific shadow (`0dp 6dp 12dp rgba(0,0,0,0.08)`).
    - **Spacing**: Standardize page padding (20dp) and section spacing (16-28dp).
- [x] **Typography**:
    - Update `Type.kt` to enforce hierarchy:
        - H1: 28sp Bold
        - H2: 22sp SemiBold
        - Section Header: 17sp Medium
        - Body: 15sp Regular
        - Caption: 13sp Medium (55% opacity)

## 2. Screen-Specific Refinements
- [x] **Home Screen**:
    - Apply consistent styling (`StandardBackground`, `PremiumCard`). (Completed)
    - Remove "Goal Completion" and "Sentiment Trend" cards. (Completed)
    - Ensure "Start Session" orb matches new design. (Completed)
- [x] **History Screen**:
    - Update Session Cards (Shadow + 22dp Radius). (Completed)
    - Fix Graph Header padding. (Completed)
- [x] **Analytics/Growth**:
    - Apply consistent styling (`StandardBackground`, `PremiumCard`). (Completed)
    - Modernize Charts: Rounded ends, gradients, soft easing. (Completed)
    - Style "Score Donut" to look less generic. (Completed)
    - Update Time Range Pill Toggle (1D/7D/30D). (Completed)
- [x] **Session Insights**:
    - Apply consistent styling (`StandardBackground`, `PremiumCard`). (Completed)
    - Add micro-icons to Feedback Loop. (Completed)
    - Add placeholder illustration for "Game Film" if empty. (Skipped - using Text for now)
- [x] **Settings**:
    - Lighten "Analysis Frequency" tile. (Completed)
    - Unify icon styles (Robot vs Palette vs Gear). (Completed)
    - Modernize Slider track. (Completed)

## 3. Implementation Strategy
1.  **Foundation**: Update `Type.kt` and `PremiumComponents.kt` (Navigation, Cards).
2.  **Home & Nav**: Apply changes to `HomeComponents.kt` and `FloatingPillNav`.
3.  **Screens**: systematic pass through History, Analytics, Wisdom, Settings.
