# Commercial UI Polish Checklist

To make the app feel like a premium, paid product, focus on these "delight" details.

## 1. Micro-Interactions (Implemented)
*   ✅ **Pulsing Recording Indicator**: Added a breathing animation to the red dot during recording.
*   ✅ **Formatted Summaries**: Auto-formatting bullet points in insights.

## 2. Loading States (Next)
*   **Skeleton/Shimmer Loading**: Replace `CircularProgressIndicator` with a "Shimmer" effect (gray pulsing boxes) that mimics the layout of the content. This makes the app feel faster.
    *   *Where*: History Screen, Insights Tab.

## 3. Empty States
*   **Call to Action**: Don't just say "No Sessions". Add a big "Start Session" button directly in the empty state.
*   **Illustrations**: Use high-quality vector illustrations (SVG) instead of just icons.

## 4. Onboarding Flow
*   **Welcome Screen**: "Become a better leader."
*   **Privacy Promise**: "Your data stays private."
*   **How it Works**: "Record -> Analyze -> Improve."

## 5. Haptic Feedback
*   Vibrate slightly when:
    *   Recording starts/stops.
    *   An "Urgent Nudge" appears.
    *   A session is deleted.

## 6. Transitions
*   **Shared Element Transitions**: When clicking a session card, the card should "expand" into the detail screen.
