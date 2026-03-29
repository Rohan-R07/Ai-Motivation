package com.example.aimotivationapp.Datastore


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PreferenceSavingViewModel(
    private val repo: UserRepository
) : ViewModel() {

    // 🔥 Reactive name (auto updates UI)
    val userName: StateFlow<String?> = repo.getUserName()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    // 🔹 First time save
    fun saveName(name: String) {
        viewModelScope.launch {
            repo.saveAndUpdateName(name)
        }
    }

    // 🔹 First time save
    fun clearName() {
        viewModelScope.launch {
            repo.clearData()
        }
    }
}


class PreferenceViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferenceSavingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreferenceSavingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}