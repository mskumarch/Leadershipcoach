# Trends Analysis Feature

## Overview
The Trends Analysis feature provides users with context on their performance by comparing their current session metrics against their historical averages.

## Implementation Details

### 1. Data Layer
- **`SessionDao`**: Added `getAverageMetrics()` query to calculate `AVG(empathyScore)`, `AVG(clarityScore)`, and `AVG(listeningScore)` from the `session_metrics` table.
- **`SessionRepository`**: Exposed `getAverageMetrics()` which returns `Result<AverageMetricsTuple?>`.
- **`AverageMetricsTuple`**: Data class holding the aggregated values.

### 2. ViewModel
- **`HistoryViewModel`**: 
    - Updated `loadSessionDetails` to fetch both specific session details and average metrics.
    - Updated `HistoryUiState` to hold `averageMetrics`.

### 3. UI Layer
- **`SessionDetailScreen`**:
    - `ScorecardSection` now accepts `averageMetrics`.
    - `MetricRow` displays a "vs Avg" indicator.
    - **Visuals**:
        - Green (`+X`) if the score is above or equal to average.
        - Red (`-X`) if the score is below average.
        - Aligned with the progress bar for easy comparison.

## Future Enhancements
- **Trend Graphs**: Visual line charts showing progress over time (last 5 sessions).
- **Category Trends**: Specific trends for "Team Meetings" vs "One-on-One".
- **Percentile Ranking**: "You are in the top 10% of listeners" (requires cloud data).
