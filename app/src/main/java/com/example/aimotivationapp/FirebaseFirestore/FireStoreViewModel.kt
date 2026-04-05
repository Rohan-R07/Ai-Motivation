package com.example.aimotivationapp.FirebaseFirestore

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun checkUserRoleforHeadLoginScreen(
        email: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {


        FirebaseFirestore.getInstance()
            .collection("roles")
            .document(email.trim()) // important
//            .document()
            .get()
            .addOnSuccessListener { doc ->

                Log.d("DEBUG_ROLE", "Email: $email")
                Log.d("DEBUG_ROLE", "Exists: ${doc.exists()}")
                Log.d("DEBUG_ROLE", "Doc ID: ${doc.id}")

                if (doc.exists()) {
                    Log.d("Monitor viewModel",email)
                    onSuccess()
                } else {
                    onFailure()
                    Log.d("Monitor viewModel",email)

                }

            }
            .addOnFailureListener {
                onFailure()
                Log.d("TeamHeadLoginScree", it.toString())
            }
    }


}