# Multi-Agent Coaching System Implementation Plan

## Objective
Transform the application into a "World-Class 1:1 Coach" by implementing a Multi-Agent System. This system will use specialized AI agents for real-time guidance (The "Real-Time Trio") and deep post-session analysis (The "Deep Analyst").
Crucially, the architecture will separate logic by **Session Type** (e.g., 1:1 vs. Team Meeting) to ensure future scalability and independent enhancement.

## Architecture: The "Orchestrator & Strategy" Pattern

### 1. The Orchestrator (`CoachingOrchestrator`)
The central brain that runs in the `SessionViewModel`. It decides *when* to call agents based on triggers (Time, Silence, User Action) and manages the parallel execution.

### 2. Session Strategies (`CoachingStrategy`)
To keep code separate, we will define a `CoachingStrategy` interface. Each session type will have its own implementation:
- `OneOnOneStrategy` (Focus: GROW Model, Rapport)
- `TeamMeetingStrategy` (Focus: Inclusion, Agenda - Future)
- `DifficultConversationStrategy` (Focus: De-escalation - Future)

### 3. The Agents
Each Strategy will provide its own set of agents. For 1:1, we will have:
- **The Navigator (GROW Agent)**: Tracks the conversation stage (Goal, Reality, Options, Way Forward).
- **The Whisperer (Question Agent)**: Generates context-aware questions.
- **The Guardian (Behavior Agent)**: Monitors tone and pace.

## Implementation Phases

### Phase 1: Foundation & Architecture
- Define `CoachingAgent` interface.
- Define `CoachingStrategy` interface.
- Create `CoachingOrchestrator` class.
- Create `OneOnOneStrategy` skeleton.

### Phase 2: The 1:1 Agents (Real-Time)
- **NavigatorAgent**: Implement prompt for GROW stage detection.
- **WhispererAgent**: Implement prompt for context-aware questions.
- **GuardianAgent**: Implement prompt for behavioral nudges.
- Integrate these into `OneOnOneStrategy`.

### Phase 3: Integration & UI
- Connect `CoachingOrchestrator` to `SessionViewModel`.
- Update `ChatScreen` to display:
    - **GROW Progress Bar** (Visual indicator of stage).
    - **Dynamic Question Button** (Updates based on Whisperer).
    - **Live Nudges** (From Guardian).

### Phase 4: The Deep Analyst (Post-Session)
- Create `DeepAnalysisAgent` for 1:1 sessions.
- Implement "Commitment Tracker" and "Question Analysis" (Open vs Closed).
- Update `ReportCard` to show these deep insights.

## Directory Structure
```
com.meetingcoach.leadershipconversationcoach
├── data
│   ├── ai
│   │   ├── agents
│   │   │   ├── CoachingAgent.kt
│   │   │   ├── NavigatorAgent.kt
│   │   │   ├── WhispererAgent.kt
│   │   │   └── GuardianAgent.kt
│   │   ├── strategies
│   │   │   ├── CoachingStrategy.kt
│   │   │   └── OneOnOneStrategy.kt
│   │   └── CoachingOrchestrator.kt
```

## Next Steps
Start with **Phase 1: Foundation & Architecture**.
