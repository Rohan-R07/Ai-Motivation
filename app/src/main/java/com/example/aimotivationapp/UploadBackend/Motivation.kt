package com.example.aimotivationapp.UploadBackend

import com.google.firebase.Timestamp
data class Motivation(
    val id: String = "",
    val message: String = "",
    val tone: String = "",
    val time: Timestamp? = null
)