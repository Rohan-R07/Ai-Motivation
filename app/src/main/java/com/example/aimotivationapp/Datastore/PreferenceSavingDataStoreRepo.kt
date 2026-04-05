package com.example.aimotivationapp.Datastore


import kotlinx.coroutines.flow.Flow


class UserRepository(
    private val preferences: UserPreferences
) {

    // 🔹 Get name
    fun getUserName(): Flow<String?> = preferences.getUserName()

    // 🔹 Get email
    fun getUserEmail(): Flow<String?> = preferences.getUserEmail()

    // 🔹 Save name
    suspend fun saveAndUpdateName(name: String) {
        preferences.saveAndUpdateName(name)
    }

    // 🔹 Save email
    suspend fun saveAndUpdateEmail(email: String) {
        preferences.saveAndUpdateEmail(email)
    }

    // 🔹 Clear data
    suspend fun clearData() {
        preferences.clear()
    }
}