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
            .document(email.trim())
//            .document()
            .get()
            .addOnSuccessListener { doc ->

                Log.d("DEBUG_ROLE", "Email: $email")
                Log.d("DEBUG_ROLE", "Exists: ${doc.exists()}")
                Log.d("DEBUG_ROLE", "Doc ID: ${doc.id}")

                if (doc.exists()) {
                    Log.d("Monitor viewModel", email)
                    onSuccess()
                } else {
                    onFailure()
                }

            }
            .addOnFailureListener {
                onFailure()
                Log.d("TeamHeadLoginScree", it.toString())
            }





    }

    // used in faculty screen
    fun getAllRolesData(
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("roles")
            .get()
            .addOnSuccessListener { result ->

                val rolesList = mutableListOf<Map<String, Any>>()

                for (document in result) {
                    val data = document.data.toMutableMap()

                    // Optional: include document ID (email)
                    data["id"] = document.id

                    rolesList.add(data)

                    Log.d("ALL_ROLES", "DocID: ${document.id} -> Data: $data")
                }

                onSuccess(rolesList)
            }
            .addOnFailureListener { exception ->
                Log.e("ALL_ROLES_ERROR", exception.message.toString())
                onFailure(exception)
            }
    }


}