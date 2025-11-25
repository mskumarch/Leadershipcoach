package com.meetingcoach.leadershipconversationcoach.domain.models

/**
 * PracticeScenario - Defines a roleplay scenario
 */
data class PracticeScenario(
    val id: String,
    val title: String,
    val description: String,
    val aiPersona: String, // The role the AI plays (e.g., "Angry Customer")
    val userRole: String, // The role the user plays (e.g., "Support Manager")
    val difficulty: String, // Easy, Medium, Hard
    val initialMessage: String // What the AI says to start
)

object ScenarioLibrary {
    val scenarios = listOf(
        PracticeScenario(
            id = "feedback_1",
            title = "Giving Constructive Feedback",
            description = "Practice giving difficult feedback to an underperforming employee who is defensive.",
            aiPersona = "You are 'Alex', a talented but inconsistent employee. You get defensive when criticized and blame external factors. You are currently missing deadlines.",
            userRole = "Manager",
            difficulty = "Medium",
            initialMessage = "Hey, you wanted to see me? I hope this isn't about the project deadline, because that wasn't my fault."
        ),
        PracticeScenario(
            id = "salary_negotiation",
            title = "Salary Negotiation",
            description = "Negotiate a salary increase with a strict budget manager.",
            aiPersona = "You are 'Sarah', a department head with a tight budget. You value the employee but have strict orders to freeze raises. You need to be convinced.",
            userRole = "Employee",
            difficulty = "Hard",
            initialMessage = "Thanks for coming in. I know you wanted to discuss compensation, but I have to be upfront—budgets are extremely tight this year."
        ),
        PracticeScenario(
            id = "conflict_resolution",
            title = "Resolving Team Conflict",
            description = "Mediate a dispute between two team members.",
            aiPersona = "You are 'Mike', a senior developer who is annoyed that 'junior developers' are breaking your code. You are arrogant and dismissive.",
            userRole = "Team Lead",
            difficulty = "Hard",
            initialMessage = "I'm done with this. If the new guys touch my code one more time without asking, I'm reverting everything. You need to fix this."
        )
    )
}
