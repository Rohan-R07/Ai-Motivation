package com.example.aimotivationapp.Screens.AuthScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // previous user
    var signEmail by mutableStateOf("")


    var signPassward by mutableStateOf("")



    fun onSignEmailChange(value: String) {
        signEmail = value
    }

    fun onSignPasswardChange(value: String) {
        signPassward = value
    }


    // register for new user

    var registerEmail by mutableStateOf("")
        private set

    var registerPassward by mutableStateOf("")
        private set

    fun onRegisterEmailChange(value: String) {
        registerEmail = value
    }

    fun onRegisterPasswardChange(value: String) {
        registerPassward = value
    }

    var retypePassword by mutableStateOf("")
        private set


    fun onRetypePasswardChange(value: String) {
        retypePassword = value
    }


}