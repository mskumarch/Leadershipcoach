# Phase 7: UI Polish & Consistency (Expert Critique)

## Objective
Elevate the app from "functional prototype" to "premium, commercial-grade product" by addressing specific UI/UX inconsistencies and applying a unified design language.

## 1. High-Impact Fixes (Priority)
- [x] **Bottom Navigation**:
    - Replace Emoji icons with **Material Symbols Rounded** (Vector Icons).
    - Apply specific colors: Inactive `#A0B8AD` (40%), Active `#4F7F6B`.
    - Add active state animation (Glow ring + Scale 1.08x).
- [ ] **Visual Consistency**:
    - **Background**: Standardize top fade gradient (`#E7F3ED` -> `#FFFFFF`).
    - **Cards**: Enforce **22dp corner radius** and specific shadow (`0dp 6dp 12dp rgba(0,0,0,0.08)`).
    - **Spacing**: Standardize page padding (20dp) and section spacing (16-28dp).
- [ ] **Typography**:
    - Update `Type.kt` to enforce hierarchy:
        - H1: 28sp Bold
        - H2: 22sp SemiBold
        - Section Header: 17sp Medium
        - Body: 15sp Regular
        - Caption: 13sp Medium (55% opacity)

## 2. Screen-Specific Refinements
- [ ] **Home Screen**:
    - Add blurred gradient behind "Start Session" orb.
    - Add abstract leadership illustration.
    - Fix "Daily Tip" card spacing.
    - Replace generic profile avatar.
- [ ] **History Screen**:
    - Update Session Cards (Shadow + 22dp Radius).
    - Fix Graph Header padding.
- [ ] **Analytics / Growth**:
    - Modernize Charts: Rounded ends, gradients, soft easing.
    - Style "Score Donut" to look less generic.
    - Update Time Range Pill Toggle (1D/7D/30D).
- [ ] **Session Insights**:
    - Add micro-icons to Feedback Loop.
    - Add placeholder illustration for "Game Film" if empty.
- [ ] **Settings**:
    - Lighten "Analysis Frequency" tile.
    - Unify icon styles (Robot vs Palette vs Gear).
    - Modernize Slider track.

## 3. Implementation Strategy
1.  **Foundation**: Update `Type.kt` and `PremiumComponents.kt` (Navigation, Cards).
2.  **Home & Nav**: Apply changes to `HomeComponents.kt` and `FloatingPillNav`.
3.  **Screens**: systematic pass through History, Analytics, Wisdom, Settings.
