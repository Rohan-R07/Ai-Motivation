package com.example.aimotivationapp.Screens.HomeScreens.Utils



data class RecentMotivation(
    val id: Int,
    val tone: String,
    val toneColor: String,
    val message: String,
    val timeAgo: String,
    val sentBy: String,
    val emoji: String,
)


val sampleMotivations = listOf(
    RecentMotivation(
        id = 1,
        tone = "Friendly",
        toneColor = ToneColor.Friendly.toString(),
        emoji = "😊",
        message = "Your dedication and hard work have been instrumental in our recent successes. Keep up the fantastic work!",
        timeAgo = "2d ago",
        sentBy = "Alex",
    ),
    RecentMotivation(
        id = 2,
        tone = "Encouraging",
        toneColor = ToneColor.Encouraging.toString(),
        emoji = "💪",
        message = "Your innovative ideas and problem-solving skills have significantly contributed to our team's progress. Keep pushing boundaries!",
        timeAgo = "3d ago",
        sentBy = "Alex",
    ),
    RecentMotivation(
        id = 3,
        tone = "Inspiring",
        toneColor = ToneColor.Inspiring.toString(),
        emoji = "🌟",
        message = "The passion you bring to every project lifts the entire team. Your commitment is truly contagious!",
        timeAgo = "5d ago",
        sentBy = "Alex",
    ),
    RecentMotivation(
        id = 4,
        tone = "Bold",
        toneColor = ToneColor.Bold.toString(),
        emoji = "🔥",
        message = "This is your moment. Own it. You've trained for this, worked for this, and earned this. Go show what you're made of!",
        timeAgo = "1w ago",
        sentBy = "Alex",
    ),
)
enum class ToneColor { Friendly, Encouraging, Inspiring, Bold }
