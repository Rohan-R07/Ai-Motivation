package com.example.aimotivationapp.Datastore



import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email") // ✅ NEW
    }

    // 🔹 Get name
    fun getUserName(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_NAME]
        }
    }

    // 🔹 Get email
    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_EMAIL]
        }
    }

    // 🔹 Save name
    suspend fun saveAndUpdateName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
        }
    }

    // 🔹 Save email
    suspend fun saveAndUpdateEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL] = email
        }
    }

    // 🔹 Clear data (logout)
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}