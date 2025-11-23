# 🚨 Critical UI/UX Fixes

## 🛠️ Addressed Issues

### 1. **Bottom Navigation Bar Visibility**
- **Problem**: Icons were blurry and invisible due to incorrect blur application and low contrast.
- **Fix**:
    - **Removed Blur from Content**: Refactored `CoachBottomNavigationBar` to apply glass styling to the background layer only, ensuring icons remain sharp.
    - **Improved Contrast**: Changed unselected icon color from `NeutralGray` to `DeepCharcoal` to ensure visibility against the glass background.

### 2. **Stop Button Location**
- **Problem**: Stop button was still appearing in the Coach screen, confusing the user who expected it in the Transcript screen.
- **Fix**:
    - **Coach Screen**: Removed the `ModernRecordingInterface` (which contained the stop button) from `CoachScreen.kt`. Replaced it with a "Session in Progress" status card that directs the user to the Transcript tab.
    - **Transcript Screen**: Verified that the floating Stop button is present and correctly positioned in `TranscriptScreen.kt`.

### 3. **Inconsistent Backgrounds**
- **Problem**: Chat screen had a different background (light blue/white) compared to the rest of the app (Sage Green).
- **Fix**: Explicitly set the background of `ChatScreen` to `SageGreen` to guarantee consistency with the design system.

## 🎨 Result
- **Consistent UI**: All screens now use the Sage Green background.
- **Usable Navigation**: Bottom menu icons are sharp and clearly visible.
- **Clear Flow**: Recording controls are centralized in the Transcript screen as requested.

## 📦 Verification
1.  **Check Bottom Nav**: Icons should be sharp and dark gray (unselected) or white (selected).
2.  **Check Chat Screen**: Background should be Sage Green.
3.  **Start Recording**:
    - Go to Coach tab -> Start Session.
    - Verify Coach tab shows "Session in Progress" (no stop button).
    - Go to Transcript tab -> Verify Stop button is visible at the bottom.
