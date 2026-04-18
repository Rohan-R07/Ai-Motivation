package com.example.aimotivationapp.Screens.GenerateMotivation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimotivationapp.Screens.HomeScreens.Utils.RecentMotivation
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch

//class AiViewModel : ViewModel() {
//
//    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
//        .generativeModel("gemini-2.5-flash-lite")
//
//    var uiState by mutableStateOf<String>("")
//        private set
//
//
//    fun generateMotivation(prompt: String) {
//
//        viewModelScope.launch {
//            try {
//                // 🔥 Firebase AI call here
//                val response = model.generateContent(prompt)
//                uiState = response.text.toString()
//
//            } catch (e: Exception) {
//                uiState = "Error: ${e.message}"
//            }
//        }
//    }
//}
//


//class AiViewModel : ViewModel() {
//
//    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
//        .generativeModel("gemini-2.5-flash-lite")
//
//    var uiState by mutableStateOf<String>("")
//        private set
//
//    var parsedMotivations by mutableStateOf<List<RecentMotivation>>(emptyList())
//        private set
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    fun generateMotivation(prompt: String) {
//
//        // ── Reset before every new generation ────────────────────────────
//        uiState = ""
//        parsedMotivations = emptyList()
//        errorMessage = null
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                // 🔥 Firebase AI call
//                val response = model.generateContent(prompt)
//                val rawText = response.text.toString()
//
//                uiState = rawText
//
//                // ── Parse and store motivations ───────────────────────────
//                val parsed = parseMotivations(rawText)
//
//                if (parsed.isNotEmpty()) {
//                    parsedMotivations = parsed
//                } else {
//                    errorMessage = "Could not parse motivations. Please try again."
//                }
//
//            } catch (e: Exception) {
//                errorMessage = "Error: ${e.message}"
//                uiState = "Error: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    // ── Clear error after it's been shown ────────────────────────────────
//    fun clearError() {
//        errorMessage = null
//    }
//
//    // ── Reset everything (useful when going back to CREATE state) ─────────
//    fun reset() {
//        uiState = ""
//        parsedMotivations = emptyList()
//        errorMessage = null
//        isLoading = false
//    }
//}



class AiViewModel : ViewModel() {

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash-lite")

    // ── Raw response ──────────────────────────────────────────────────────────
    var uiState by mutableStateOf("")
        private set

    // ── Parsed motivations ────────────────────────────────────────────────────
    var parsedMotivations by mutableStateOf<List<RecentMotivation>>(emptyList())
        private set

    // ── Loading flag ──────────────────────────────────────────────────────────
    var isLoading by mutableStateOf(false)
        private set

    // ── Regenerating flag (separate so UI can show different state) ───────────
    var isRegenerating by mutableStateOf(false)
        private set

    // ── Error message ─────────────────────────────────────────────────────────
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // ── Store last prompt so regenerate can reuse it ──────────────────────────
    private var lastPrompt: String = ""

    // ─────────────────────────────────────────────────────────────────────────

    fun generateMotivation(prompt: String) {
        // Save prompt for regenerate
        lastPrompt = prompt

        // Reset before every new call
        uiState           = ""
        parsedMotivations = emptyList()
        errorMessage      = null
        isLoading         = true
        isRegenerating    = false

        viewModelScope.launch {
            runGeneration(prompt)
        }
    }

    // ── Regenerate using the last prompt ──────────────────────────────────────
    fun regenerateMotivation() {
        if (lastPrompt.isEmpty()) {
            errorMessage = "Nothing to regenerate. Please generate first."
            return
        }

        // Keep existing list visible while regenerating
        // so the screen doesn't go blank
        errorMessage   = null
        isRegenerating = true
        isLoading      = false

        viewModelScope.launch {
            runGeneration(lastPrompt)
        }
    }

    // ── Shared generation logic ───────────────────────────────────────────────
    private suspend fun runGeneration(prompt: String) {
        try {
            val response = model.generateContent(prompt)
            val rawText  = response.text.toString()

            uiState = rawText
            Log.d("AiViewModel", "Raw response: $rawText")

            val parsed = parseMotivations(rawText)

            if (parsed.isNotEmpty()) {
                parsedMotivations = parsed
            } else {
                errorMessage = "Could not parse motivations. Please try again."
            }

        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
            Log.e("AiViewModel", "Generation failed: ${e.message}")
        } finally {
            isLoading      = false
            isRegenerating = false
        }
    }

    // ── Clear error after Snackbar shows it ───────────────────────────────────
    fun clearError() {
        errorMessage = null
    }

    // ── Full reset when user goes back to CREATE ───────────────────────────────
    fun reset() {
        uiState           = ""
        parsedMotivations = emptyList()
        errorMessage      = null
        isLoading         = false
        isRegenerating    = false
        lastPrompt        = ""
    }
}