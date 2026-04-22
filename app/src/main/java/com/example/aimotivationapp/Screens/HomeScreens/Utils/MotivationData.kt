package com.example.aimotivationapp.Screens.HomeScreens.Utils



data class RecentMotivation(
    val id: Int,
    val tone: String,
    val toneColor: String,
    val message: String,

    val emoji: String,
)


enum class ToneColor { Friendly, Encouraging, Positive, Uplifting,Motivational }
