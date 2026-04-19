package com.example.aimotivationapp.UploadBackend

sealed class AddMotivationState {
    object Idle : AddMotivationState()
    object Loading : AddMotivationState()
    object Success : AddMotivationState()
    data class Error(val message: String) : AddMotivationState()
}