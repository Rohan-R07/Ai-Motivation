package com.example.aimotivationapp.Screens.GenerateMotivation

import org.json.JSONObject

fun cleanJson(response: String): String {
    return response
        .replace("```json", "")
        .replace("```", "")
        .trim()
}

fun parseMotivations(response: String): List<String> {

    val clean = cleanJson(response)

    val jsonObject = JSONObject(clean)
    val jsonArray = jsonObject.getJSONArray("motivations")

    return List(jsonArray.length()) { index ->
        jsonArray.getString(index)
    }
}