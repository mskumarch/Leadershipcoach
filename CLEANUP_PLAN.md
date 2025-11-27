# Critical Cleanup Plan

**Persona**: 🏗️ Software Architect  
**Date**: 2025-11-27  
**Status**: Ready for Implementation

---

## Issues Identified

### 1. Hardcoded Demo Code ❌

#### `SessionViewModel.getSuggestedQuestions()`
- **Issue**: Returns static lists of questions per session mode
- **Impact**: Not AI-driven, defeats purpose of Whisperer Agent
- **Fix**: Remove this method, rely on Whisperer Agent exclusively
- **Files**: `SessionViewModel.kt` (lines 614-653)

#### Fallback Values in AI Parsing
- **Issue**: Many parsing functions default to `score = 50` on error
- **Impact**: Masks failures, provides misleading data
- **Fix**: Use `null` for failures, handle gracefully in UI
- **Files**: `GeminiApiService.kt`

---

### 2. Magic Numbers 🔢

#### Hardcoded Intervals
- `60_000L` - Analysis interval (SessionViewModel)
- `60_000L` - Navigator interval (OneOnOneStrategy)
- `30_000L` - Guardian interval (OneOnOneStrategy)

**Fix**: Extract to `AppConstants` object

#### Hardcoded Thresholds
- Talk ratio calculations
- Score thresholds (70, 50, etc.)
- Question count targets

**Fix**: Extract to `CoachingThresholds` object with research-based values

---

### 3. TODO Comments 📝

#### Cloud STT Service
- **Status**: Placeholder implementation
- **Decision**: Keep TODOs, mark as "Future Feature"
- **Action**: Add comment explaining it's intentionally unimplemented

#### Other TODOs
- Language configuration (Localization - future)
- Scenario clicks (Not in scope)
- Dashboard actions (Not in scope)

**Action**: Document which are future features vs. bugs

---

### 4. Code Duplication 🔄

#### JSON Parsing Pattern
- Repeated in multiple methods:
  - `parseSessionAnalysis()`
  - `parseNavigatorResponse()`
  - `parseWhispererResponse()`
  - `parseGuardianResponse()`

**Fix**: Create `JsonParser` utility class

#### Error Handling Pattern
- Try-catch-log-return-null repeated everywhere

**Fix**: Create `Result<T>` wrapper class

---

### 5. Null Safety Issues ⚠️

#### Potential NPEs
- `sessionDetails.metrics?.summary` - multiple null checks
- `analysis?.transcriptJson` - chained nullables

**Fix**: Use Elvis operator consistently, provide safe defaults

---

### 6. Resource Management 🗑️

#### Audio File Cleanup
- **Issue**: Recorded files may accumulate
- **Fix**: Implement cleanup policy (keep last 10 sessions)

#### Coroutine Cancellation
- **Issue**: Need to verify all jobs are cancelled
- **Fix**: Audit `onCleared()` methods

---

## Implementation Plan

### Step 1: Extract Constants (30 min)
Create `AppConstants.kt`:
```kotlin
object AppConstants {
    object Coaching {
        const val NAVIGATOR_INTERVAL_MS = 60_000L
        const val GUARDIAN_INTERVAL_MS = 30_000L
        const val MIN_SESSION_DURATION_MS = 30_000L
    }
    
    object Thresholds {
        const val IDEAL_TALK_RATIO_MIN = 30
        const val IDEAL_TALK_RATIO_MAX = 40
        const val IDEAL_OPEN_QUESTION_RATIO = 0.6f
        const val MAX_ACCEPTABLE_INTERRUPTIONS = 2
        const val MIN_EMPATHY_SIGNALS = 5
    }
}
```

### Step 2: Remove Hardcoded Questions (15 min)
- Delete `getSuggestedQuestions()` method
- Update `ChatScreen` to only show Whisperer's dynamic question
- Remove fallback static lists

### Step 3: Create Utility Classes (45 min)
- `JsonParser.kt` - Centralized JSON parsing
- `Result.kt` - Sealed class for success/failure
- `AudioFileManager.kt` - File cleanup logic

### Step 4: Consolidate Error Handling (30 min)
- Replace try-catch-null with `Result<T>`
- Add proper logging levels (DEBUG, ERROR)
- User-friendly error messages

### Step 5: Fix Null Safety (20 min)
- Add `requireNotNull()` where appropriate
- Use Elvis operator with safe defaults
- Remove unnecessary null checks

### Step 6: Resource Cleanup (20 min)
- Implement audio file cleanup
- Verify coroutine cancellation
- Add lifecycle logging

---

## Success Criteria

✅ No hardcoded question lists  
✅ All magic numbers extracted to constants  
✅ Centralized JSON parsing  
✅ Consistent error handling  
✅ No null safety warnings  
✅ Resource cleanup verified  
✅ Build passes with no warnings  

---

## Peer Review Checklist

- [ ] Constants follow research-based values
- [ ] No functionality broken
- [ ] Code is more maintainable
- [ ] Error messages are user-friendly
- [ ] Resource leaks prevented

---

**Next**: Implementation (Engineer Hat)
