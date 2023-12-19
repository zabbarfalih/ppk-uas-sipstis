package com.zabbarfalih.sipstis.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zabbarfalih.sipstis.SipstisApplication
import com.zabbarfalih.sipstis.data.AlertRepository
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.model.Alert
import kotlinx.coroutines.launch

private const val TAG = "AlertViewModel"

class AlertViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {
    private lateinit var token: String
    var nameField by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    suspend fun createAlert(): CreateAlertResult {
        try {
            alertRepository.createAlert(token, Alert(
                id = null,
                name = nameField
            ))
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            return CreateAlertResult.Error
        }

        return CreateAlertResult.Success
    }

    fun updateNameField(newName: String) {
        nameField = newName
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                val alertRepository = application.container.alertRepository
                AlertViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    alertRepository = alertRepository
                )
            }
        }
    }
}

enum class CreateAlertResult {
    Success,
    Error
}