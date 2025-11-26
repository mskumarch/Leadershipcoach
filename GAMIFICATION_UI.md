# Gamification UI Plan

## Objective
Create a visual interface to display user achievements and progress, motivating them to continue using the app.

## Components

### 1. Achievement Entity (Implemented)
- `id`: Unique identifier (e.g., "first_session")
- `title`: Display name
- `description`: How to unlock
- `iconResId`: Resource ID for the icon
- `isUnlocked`: Boolean status
- `progress`: Current progress value
- `target`: Target value to unlock

### 2. Achievements Screen
- **Layout**: Grid or List of achievement cards.
- **Card Design**:
    - **Locked**: Greyed out, padlock icon, progress bar showing current status.
    - **Unlocked**: Colorful, "Badge" look, date unlocked.
- **Header**: "Your Trophy Case" or "Leadership Gym".
- **Summary**: "X/Y Achievements Unlocked".

### 3. Integration
- **Entry Point**: 
    - Option A: New tab in Bottom Navigation (might be too crowded).
    - Option B: "Lightbulb" FAB action (currently goes to Progress).
    - Option C: Icon in the Top Bar of `ProgressScreen` or `HistoryScreen`.
    - **Recommendation**: Add a "Trophy" icon to the Top Bar of the `ProgressScreen`.

## Implementation Steps
1.  **ViewModel**: Create `AchievementsViewModel` or extend `ProgressViewModel` to fetch achievements using `GamificationRepository`.
2.  **UI Components**:
    - `AchievementCard`: Composable for individual items.
    - `AchievementsGrid`: Composable for the list.
3.  **Screen**: `AchievementsScreen` composable.
4.  **Navigation**: Add route to `NavigationGraph`.

## Design Ideas
- Use the "Calm Green" theme for unlocked achievements.
- Use a "Gold" or "Bronze" accent for special achievements.
- Add a simple animation (confetti or shine) when viewing a newly unlocked achievement.
