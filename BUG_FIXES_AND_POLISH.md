# 🐛 Bug Fixes & Polish

## 1. Critical Crash Fix (NoSuchMethodError)
-   **Issue**: "FATAL EXCEPTION: main ... NoSuchMethodError: No virtual method at(...) in KeyframesSpec".
-   **Cause**: Dependency version mismatch. The project was using an older Compose BOM (`2024.01.00`) which caused a conflict between `androidx.compose.material3` and `androidx.compose.animation`.
-   **Fix**: Updated Compose BOM to `2024.09.00` in `app/build.gradle.kts`. This aligns all Compose libraries (UI, Material3, Animation) to compatible versions (approx. 1.7.x / 1.3.x).

## 2. Chat Screen Layout
-   **Issue**: "Send button overlapped by hamburger menu" & "Huge gap between bottom menu and chat textbox".
-   **Fix**:
    -   Reduced `ChatInputField` bottom padding from `80dp` to `16dp`.
    -   The Hamburger Menu FAB remains at `bottom = 100dp`, floating clearly above the input field.

## 3. App Icon
-   **Issue**: "Icon is just plain solid color".
-   **Fix**:
    -   Replaced the PNG foreground with a **Vector Drawable** (`ic_launcher_foreground.xml`).
    -   The icon is now a crisp White Speech Bubble with a Star on a Teal background.

## 4. Transcript Screen
-   **Issue**: "App kills itself when clicking transcript icon".
-   **Fix**:
    -   Refactored `TranscriptScreen` to use `LazyColumn` for performance.
    -   Fixed theme resource references (`SoftCream` -> `MaterialTheme.colorScheme.background`).
    -   Added safety checks.
    -   **Note**: The BOM update also fixes the underlying crash in `CircularProgressIndicator` which was affecting this screen.

## 5. Theme Consistency
-   **Issue**: "Session mode colors don't match".
-   **Fix**:
    -   Refactored `SessionModeModal.kt` to use `MaterialTheme` colors.
    -   Created `AppPalette` in `Color.kt` as the single source of truth.

## 📦 Verification
1.  **Crash Check**: Launch the app, start a session, and navigate to Transcript or History. It should NOT crash.
2.  **UI Check**: Verify Chat screen layout and App Icon.
