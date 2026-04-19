package com.example.aimotivationapp.UploadBackend

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UploadFetchMotivationQuotes : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var quote by mutableStateOf("")
        private set

    var tone by mutableStateOf("")
        private set

    var addState by mutableStateOf<AddMotivationState>(AddMotivationState.Idle)
        private set

    // input handler
    fun onInputChange(newQuote: String? = null, newTone: String? = null) {
        newQuote?.let { quote = it }
        newTone?.let { tone = it }
    }

    fun addMotivation(
        email: String,
        quote: String,
        tone: String,
        onSucesses: () -> Unit
    ) {

        if (quote.isBlank() || tone.isBlank()) {
            addState = AddMotivationState.Error("Fields cannot be empty")
            return
        }

        addState = AddMotivationState.Loading

        val data = hashMapOf(
            "message" to quote.trim(),
            "tone" to tone.trim(),
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("roles")
            .document(email)
            .collection("motivations")
            .add(data)
            .addOnSuccessListener {
                addState = AddMotivationState.Success
                onSucesses()
            }
            .addOnFailureListener {
                addState = AddMotivationState.Error(it.message ?: "Error")
            }
    }

    fun resetState() {
        addState = AddMotivationState.Idle
    }
}

//
//class UploadFetchMotivationQuotes : ViewModel() {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    // 🔥 Store email internally
//    private var userEmail: String = ""
//
//    var quote by mutableStateOf("")
//        private set
//
//    var tone by mutableStateOf("")
//        private set
//
//    var addState by mutableStateOf<AddMotivationState>(AddMotivationState.Idle)
//        private set
//
//    // ✅ Initialize email (call this from Splash)
//    fun setUserEmail(email: String) {
//        userEmail = email
//    }
//
//    // input handler
//    fun onInputChange(newQuote: String? = null, newTone: String? = null) {
//        newQuote?.let { quote = it }
//        newTone?.let { tone = it }
//    }
//
//    fun addMotivation(
//        quote: String,
//        tone: String,
//        onSucesses: () -> Unit
//    ) {
//
//        // 🔥 Validate email first
//        if (userEmail.isBlank() || !userEmail.contains("@")) {
//            addState = AddMotivationState.Error("Invalid user email")
//            return
//        }
//
//        if (quote.isBlank() || tone.isBlank()) {
//            addState = AddMotivationState.Error("Fields cannot be empty")
//            return
//        }
//
//        addState = AddMotivationState.Loading
//
//        val data = hashMapOf(
//            "message" to quote.trim(),
//            "tone" to tone.trim(),
//            "createdAt" to FieldValue.serverTimestamp()
//        )
//
//        db.collection("roles")
//            .document(userEmail) // 🔥 using stored email
//            .collection("motivations")
//            .add(data)
//            .addOnSuccessListener {
//                addState = AddMotivationState.Success
//                onSucesses()
//            }
//            .addOnFailureListener {
//                addState = AddMotivationState.Error(it.message ?: "Error")
//            }
//    }
//
//    fun resetState() {
//        addState = AddMotivationState.Idle
//    }
//}