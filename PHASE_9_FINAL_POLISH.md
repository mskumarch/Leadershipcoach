# Phase 9: Final UI Polish & Consistency

## Objective
Address the specific design critique to elevate the app to a 10/10 premium standard. Focus on removing visual noise, ensuring icon consistency, and refining micro-interactions.

## 1. 🚨 Critical Fix: Bottom Navigation Background
**The Problem**: The green "peel" background behind the floating nav bar destroys the "floating" illusion and adds visual noise.
**The Fix**:
- [x] Change bottom area background to **Pure White (#FFFFFF)** or soft off-white (#FAFAF7). (Completed)
- [x] Ensure `FloatingPillNav` has a glassmorphic blur + shadow (`0dp 8dp 20dp rgba(0, 0, 0, 0.06)`). (Completed)
- [x] **Icons**: Switch ALL nav icons to **Material Symbols Rounded (Outlined)**. (Completed)
    - Home, Lightbulb, Checklist (Live), Monitoring (Stats), History, Settings.
- [x] **Active State**: (Completed)
    - Tint: `#3E7D68` (Deep Sage).
    - Add subtle circular glow behind icon.
    - Increase size: 24dp -> 27dp.

## 2. Screen-Specific Refinements

### A. 📊 Growth Screen (Analytics)
- [x] **Donut Chart**: Add soft glow or layered ring (dual-tone) for depth. (Completed)
- [x] **"View Details" Button**: Change color to Brand Accent (Sage) or soft black with reduced opacity. (Completed)
- [x] **Spacing**: Add `12dp` padding below "Deep Dive Analytics". (Completed)
- [x] **Nav Icon**: Ensure "Stats" icon matches the new outlined style. (Completed - Handled in Nav Update)

### B. 📝 Transcript Tab
- [x] **Summary Card**: (Completed)
    - Increase padding (top/bottom).
    - Lighten green background by 5% for readability.
- [x] **Tab Indicator**: Reduce height to `2-3dp`. (Completed)
- [x] **Tab Bar**: Add tiny drop shadow for separation. (Completed)

### C. 🕰️ History Screen
- [x] **Session Cards**: (Completed)
    - Add soft shadow: `0dp 3dp 8dp rgba(0,0,0,0.05)`.
    - Increase corner radius to `22dp`.
    - Fix arrow icon alignment (raise by ~4dp).
- [x] **Avatar**: Replace generic icon with a modern circular silhouette or colored initials. (Completed)

### D. ⚙️ Settings Screen
- [x] **Icons**: Enforce consistent stroke thickness (Material Symbols Rounded). (Completed)
- [x] **Slider**: Add soft gradient or depth to the rail. (Completed)
- [x] **Haptic Toggle**: Use M3 toggle style with animated thumb. (Completed)
- [x] **Alignment**: Add `4-8dp` top padding to "Text Size" title. (Completed)
- [x] **Coaching Style**: Convert to Dropdown. (Completed)

### E. 💡 Session Insights
- [ ] **Headers**: Increase font weight (+100) for "Performance Scorecard" and "Feedback Loop".
- [ ] **Action Plan Button**: Add slight shadow or gloss.
- [ ] **Spacing**: Add `10dp` spacing between sections.

### F. 🏠 Home Landing Screen
- [ ] **Start Button**: Add depth shadow (`0dp 8dp 20dp rgba(0,0,0,0.12)`).
- [ ] **Icons**: Ensure starter icons match the new outlined style.
- [ ] **Profile Icon**: Use a softer round-rect with gradient.

## 3. Implementation Plan
1.  **Nav & Icons**: Fix the bottom bar first (highest impact).
2.  **Home & Settings**: Quick wins on static screens.
3.  **History & Growth**: Refine the complex list/chart views.
4.  **Insights & Transcript**: Polish the detail views.
