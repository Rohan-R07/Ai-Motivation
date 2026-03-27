package com.example.aimotivationapp.Screens.AuthScreens.Utils

import android.util.Patterns

fun signInInputs(email: String, password: String): String? {

    return if (email.isEmpty() || password.isEmpty()) {
        "Fields cannot be empty"

    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        "Invalid email format"

    } else if (password.length < 6) {
        "Password must be at least 6 characters"

    } else if (!password.any { it.isDigit() }) {
        "Password must contain at least one number"

    } else if (!password.any { it.isUpperCase() }) {
        "Password must contain at least one uppercase letter"

    } else {
        null
    }
}
