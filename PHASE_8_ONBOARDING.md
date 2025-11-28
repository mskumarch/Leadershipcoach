# Phase 8: Onboarding & First-Run Experience

## Objective
Create a premium, "Apple-like" onboarding experience that welcomes new users, explains the app's value, and gracefully handles permissions and initial setup. This is a critical step for commercialization.

## 1. Features

### A. Welcome Screen
*   **Visual**: Full-screen animated gradient background (`StandardBackground`).
*   **Content**: App Logo (large), Tagline ("Master Your Leadership Conversations"), and a "Get Started" button.
*   **Animation**: Subtle fade-in of elements.

### B. Value Proposition Carousel
*   **Format**: Horizontal pager with 3 slides.
*   **Slide 1: Real-time Coaching**: "Get live, private nudges during your meetings."
*   **Slide 2: Deep Analytics**: "Visualize your speaking time, sentiment, and impact."
*   **Slide 3: Growth Journey**: "Track your progress and master new skills."
*   **UI**: `PremiumCard` styling for the content area, page indicators at the bottom.

### C. Permission Primer
*   **Problem**: System permission dialogs are jarring and often rejected.
*   **Solution**: A dedicated screen *explaining why* we need permissions before requesting them.
*   **Permissions**:
    *   🎙️ **Microphone**: "To analyze your speech in real-time."
    *   🔔 **Notifications**: "To send you discreet coaching nudges."
*   **Interaction**: "Grant Permissions" button -> System Dialog -> "Continue".

### D. User Personalization (Mini-Profile)
*   **Goal**: Customize the AI coach from Day 1.
*   **Input**:
    *   **Name**: "What should we call you?"
    *   **Leadership Style**: Select from [Servant, Visionary, Democratic, Coaching].
*   **Outcome**: Save to `UserPreferences` and use in the system prompt.

## 2. Technical Implementation

### A. New Screens
*   `OnboardingScreen.kt`: The main container for the flow.
*   `WelcomeStep.kt`
*   `ValuePropStep.kt`
*   `PermissionsStep.kt`
*   `ProfileStep.kt`

### B. Navigation
*   Update `NavigationScreen.kt` to show `OnboardingScreen` if `isFirstRun` is true.
*   Add `isFirstRun` boolean to `UserPreferences` (DataStore).

### C. Assets
*   Need vector icons for the slides (use `Icons.Rounded`).
*   Need a placeholder "Logo" (use a composed icon for now).

## 3. Step-by-Step Plan
1.  **Setup**: Create `OnboardingViewModel` and `UserPreferences` logic for `isFirstRun`.
2.  **UI Construction**: Build the `OnboardingScreen` with the `HorizontalPager`.
3.  **Permissions**: Implement the "Primer" logic using Accompanist Permissions or standard ActivityResultContracts.
4.  **Personalization**: Create the Profile input form and save logic.
5.  **Integration**: Wire it up in `MainActivity` / `NavigationScreen`.
