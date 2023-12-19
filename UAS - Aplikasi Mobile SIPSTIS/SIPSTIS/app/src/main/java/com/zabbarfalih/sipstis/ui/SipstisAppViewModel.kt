package com.zabbarfalih.sipstis.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zabbarfalih.sipstis.SipstisApplication
import com.zabbarfalih.sipstis.data.PengajuanRepository
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class SipstisAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val pengajuanRepository: PengajuanRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(SipstisAppUiState())
    val uiState: StateFlow<SipstisAppUiState> = _uiState.asStateFlow()

    val userState: StateFlow<UserState> = userPreferencesRepository.user.map { user ->
        user
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserState(
            "",
            "",
            "",
            "",
            "",
            roles = emptyList()
        )
    )

    fun showSpinner() {
        _uiState.update { currentState ->
            currentState.copy(
                showProgressDialog = true
            )
        }
    }

    fun dismissSpinner() {
        _uiState.update { currentState ->
            currentState.copy(
                showProgressDialog = false
            )
        }
    }

    fun showMessageDialog(@StringRes title: Int, @StringRes body: Int) {
        _uiState.update {  currentState ->
            currentState.copy(
                showProgressDialog = false,
                showMessageDialog = true,
                messageTitle = title,
                messageBody = body
            )
        }
    }

    fun dismissMessageDialog() {
        _uiState.update {  currentState ->
            currentState.copy(
                showMessageDialog = false
            )
        }
    }

    suspend fun logout() {
        userPreferencesRepository.saveToken("")
        userPreferencesRepository.saveName("")
        userPreferencesRepository.saveEmail("")
        userPreferencesRepository.saveRoles(emptyList())
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                SipstisAppViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    pengajuanRepository = application.container.pengajuanRepository
                )
            }
        }
    }
}

data class SipstisAppUiState(
    val showProgressDialog: Boolean = false,
    val showMessageDialog: Boolean = false,
    @StringRes val messageTitle: Int = 0,
    @StringRes val messageBody: Int = 0
)