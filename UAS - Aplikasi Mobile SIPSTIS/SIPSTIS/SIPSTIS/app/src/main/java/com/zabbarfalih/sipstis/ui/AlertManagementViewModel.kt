package com.zabbarfalih.sipstis.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import java.io.IOException

private const val TAG = "AlertManagementViewModel"

class AlertManagementViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private lateinit var token: String

    var alertUiState: AlertUiState by mutableStateOf(AlertUiState.Loading)
        private set

    var selectedId: Long by mutableLongStateOf(0)

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
        getAlerts()
    }

    fun getAlerts() {
        viewModelScope.launch {
            alertUiState = AlertUiState.Loading
            alertUiState = try {
                AlertUiState.Success(alertRepository.getAlerts(token))
            } catch(e: IOException) {
                Log.e(TAG, "IOException: ${e.message}")
                AlertUiState.Error
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                AlertUiState.Error
            }
        }
    }

    suspend fun deleteAlert(): DeleteAlertResult {
        try {
            alertRepository.deleteAlert(token, selectedId)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            return DeleteAlertResult.Error
        }

        return DeleteAlertResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                val alertRepository = application.container.alertRepository
                AlertManagementViewModel(
                    application.userPreferenceRepository,
                    alertRepository
                )
            }
        }
    }

}

sealed interface AlertUiState {
    data class Success(val alerts: List<Alert>): AlertUiState
    object Error: AlertUiState
    object Loading: AlertUiState
}

enum class DeleteAlertResult {
    Success,
    Error
}