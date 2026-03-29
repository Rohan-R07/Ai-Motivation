package com.example.aimotivationapp.Datastore



import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val preferences: UserPreferences
) {

    // to get the user data in the home screen
    fun getUserName(): Flow<String?> = preferences.getUserName()

    // to update or save the name while loggin in or re-loggin in or signing up
    suspend fun saveAndUpdateName(name: String) {
        preferences.saveAndUpdateName(name)
    }

    // clearing the data while logging out
    suspend fun clearData() {
        preferences.clear()
    }
}