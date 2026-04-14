package com.example.aimotivationapp.Screens.HomeScreens.Utils



data class RecentMotivation(
    val id: Int,
    val tone: String,
    val toneColor: String,
    val message: String,

    val emoji: String,
)


val sampleMotivations = listOf(
    RecentMotivation(
        id = 1,
        tone = "Friendly",
        toneColor = ToneColor.Friendly.toString(),
        emoji = "😊",
        message = "Your dedication and hard work have been instrumental in our recent successes. Keep up the fantastic work!",

    ),
    RecentMotivation(
        id = 2,
        tone = "Encouraging",
        toneColor = ToneColor.Encouraging.toString(),
        emoji = "💪",
        message = "Your innovative ideas and problem-solving skills have significantly contributed to our team's progress. Keep pushing boundaries!",

    ),
    RecentMotivation(
        id = 3,
        tone = "Inspiring",
        toneColor = ToneColor.Inspiring.toString(),
        emoji = "🌟",
        message = "The passion you bring to every project lifts the entire team. Your commitment is truly contagious!",

    ),
    RecentMotivation(
        id = 4,
        tone = "Bold",
        toneColor = ToneColor.Bold.toString(),
        emoji = "🔥",
        message = "This is your moment. Own it. You've trained for this, worked for this, and earned this. Go show what you're made of!",

    ),
)
enum class ToneColor { Friendly, Encouraging, Inspiring, Bold }
