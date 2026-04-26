package com.example.aimotivationapp.Screens.GenerateMotivation

import android.util.Log
import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
import org.json.JSONObject
import kotlin.collections.emptyList

fun parseMotivations(rawJson: String): List<RecentMotivation> {
    return try {

        val cleaned = rawJson
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val jsonObject = org.json.JSONObject(cleaned)
        val jsonArray  = jsonObject.getJSONArray("motivations")

        List(jsonArray.length()) { index ->
            RecentMotivation(
                id        = index + 1,
                tone      = toneForIndex(index),
                toneColor = colorForIndex(index),
                message   = jsonArray.getString(index),
                emoji     = emojiForIndex(index)
            )
        }
    } catch (e: Exception) {
        Log.e("parseMotivations", "Parsing failed: ${e.message}")
        emptyList()
    }
}

private fun toneForIndex(index: Int) =
    listOf("Motivational", "Encouraging", "Positive", "Uplifting", "Friendly")
        .getOrElse(index) { "Motivational" }

private fun colorForIndex(index: Int) =
    listOf("purple", "blue", "violet", "green", "orange")
        .getOrElse(index) { "purple" }

private fun emojiForIndex(index: Int) =
    listOf("🚀", "💪", "🔥", "🌿", "⚡")
        .getOrElse(index) { "✨" }