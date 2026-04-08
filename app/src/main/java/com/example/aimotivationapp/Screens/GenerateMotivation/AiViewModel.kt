package com.example.aimotivationapp.Screens.GenerateMotivation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash-lite")

    var uiState by mutableStateOf<String>("")
        private set


    fun generateMotivation(prompt: String) {

        viewModelScope.launch {
            try {
                // 🔥 Firebase AI call here
                val response = model.generateContent(prompt)
                uiState = response.text.toString()

            } catch (e: Exception) {
                uiState = "Error: ${e.message}"
            }
        }
    }
}