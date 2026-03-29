package com.example.aimotivationapp.FirebaseFirestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getUserRole(userId: String): String? {
        return try {
            val doc = db.collection("users")
                .document(userId)
                .get()
                .await()

            doc.getString("role")

        } catch (e: Exception) {
            null
        }
    }
}