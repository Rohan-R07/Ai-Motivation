package com.example.aimotivationapp.UploadBackend

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.Timestamp
import androidx.compose.runtime.State

class UploadFetchMotivationQuotes : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var quote by mutableStateOf("")
        private set

    var tone by mutableStateOf("")
        private set

    var addState by mutableStateOf<AddMotivationState>(AddMotivationState.Idle)
        private set

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

    private val _motivations = mutableStateOf<List<Motivation>>(emptyList())
    val motivations: State<List<Motivation>> get() = _motivations


    fun listenMotivations(email: String) {

        db.collection("roles")
            .document(email)
            .collection("motivations")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    Log.e("FETCH_ERROR", error.message.toString())
                    return@addSnapshotListener
                }

                val list = value?.documents?.map { doc ->
                    Motivation(
                        id = doc.id,
                        message = doc.getString("message") ?: "",
                        tone = doc.getString("tone") ?: "",
                        time = doc.getTimestamp("createdAt")
                    )
                } ?: emptyList()

                _motivations.value = list
            }
    }

    fun deleteMotivation(
        email: String,
        docId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        db.collection("roles")
            .document(email.trim())
            .collection("motivations")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                Log.d("DELETE_SUCCESS", "Deleted: $docId")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("DELETE_ERROR", it.message.toString())
                onFailure(it.message ?: "Delete failed")
            }
    }



}
