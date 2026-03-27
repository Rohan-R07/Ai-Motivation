package com.example.aimotivationapp.FirebaseAuthentication

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthentication : ViewModel(){

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}

// TODO USED WHEN HEAD SCREEN IS TO BE MADE !