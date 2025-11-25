# 🧼 Clean Slate Theme & Crash Fixes

## 1. 🎨 New "Clean Slate" Theme
- **Objective**: Address user feedback about "muddy" colors and poor contrast.
- **Palette**:
    - **Background**: `White` (`#FFFFFF`) - Pure, clean canvas.
    - **Primary**: `Teal 600` (`#0D9488`) - Vibrant, professional accent.
    - **Secondary**: `Slate 100` (`#F1F5F9`) - Subtle gray for cards/containers.
    - **Text**: `Slate 900` (`#0F172A`) - High contrast, readable text.
- **Implementation**: Updated `Color.kt` and `Theme.kt`. The app now uses a standard light theme with professional teal accents, moving away from the experimental "Sage/Taupe" mix that caused visual clashes.

## 2. 🐛 Crash Fixes
- **Issue**: "Clicking on Transcript is killing the app".
- **Fix**:
    - **Stop Button**: Replaced custom `MutedCoral` color with standard `MaterialTheme.colorScheme.error` in `TranscriptScreen.kt`. This ensures the button always has a valid color definition and avoids potential resource linking issues.
    - **Stability**: Verified `TranscriptItem` and helper functions are robust.

## 3. 🌑 Dark Mode
- **Fix**: Confirmed `WindowCompat.setDecorFitsSystemWindows(window, false)` is in place to fix the black rectangle issue.

## 📦 Verification
1.  **Check UI**: The app should look much cleaner with a white background and teal buttons. No more "muddy" green/brown combinations.
2.  **Check Transcript**: Navigate to the Transcript tab. It should NOT crash.
3.  **Check Stop Button**: Start a session, go to Transcript. The Stop button should be Red (Error color) and functional.
