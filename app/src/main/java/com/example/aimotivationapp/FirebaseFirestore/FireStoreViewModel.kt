package com.example.aimotivationapp.FirebaseFirestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
//
//class FirestoreViewModel : ViewModel() {
//
//    private val db = FirebaseFirestore.getInstance()
//
//    private val _documentIds = MutableStateFlow<List<String>>(emptyList())
//    val documentIds: StateFlow<List<String>> = _documentIds
//
//    fun fetchDocumentIds() {
//        db.collection("your_collection_name")
//            .get()
//            .addOnSuccessListener { result ->
//                val ids = result.map { it.id }
//                _documentIds.value = ids
//            }
//            .addOnFailureListener {
//                // handle error if needed
//            }
//    }
//}

class FirestoreViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun checkUserRole(
        email: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection("roles")
            .document(email.trim()) // important
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onSuccess()   // ✅ user exists (has role)
                } else {
                    onFailure()   // ❌ not found
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}