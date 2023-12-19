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
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.data.UserRepository
import com.zabbarfalih.sipstis.model.ChangePasswordForm
import com.zabbarfalih.sipstis.model.User
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val TAG = "ProfileViewModel"

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var token: String
    private lateinit var email: String
    private lateinit var nip: String
    private lateinit var roles: List<String>

    var showConfirmDialog by mutableStateOf(false)

    var nameField by mutableStateOf("")
        private set

    var noTeleponField by mutableStateOf("")
        private set

    var oldPasswordField by mutableStateOf("")
        private set

    var newPasswordField by mutableStateOf("")
        private set

    var confirmPasswordField by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                nameField = user.name
                nip = user.nip
                email = user.email
                noTeleponField = user.noTelepon
                roles = user.roles
            }
        }
    }

    fun updateNameField(name: String) {
        nameField = name
    }

    fun updateNoTeleponField(noTelepon: String) {
        noTeleponField = noTelepon
    }

    fun updateOldPasswordField(password: String) {
        oldPasswordField = password
    }

    fun updateNewPasswordField(password: String) {
        newPasswordField = password
    }

    fun updateConfirmPasswordField(password: String) {
        confirmPasswordField = password
    }

    suspend fun updateProfile(): UpdateProfileResult {
        if (nameField.isBlank() || noTeleponField.isBlank()) {
            return UpdateProfileResult.EmptyField
        }

        try {
            userRepository.updateProfile(
                token = token,
                user = User(
                    id = null,
                    name = nameField,
                    nip = nip,
                    email = email,
                    noTelepon = noTeleponField,
                )
            )

            userPreferencesRepository.saveName(nameField)
            userPreferencesRepository.saveNoTelepon(noTeleponField)

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            return UpdateProfileResult.Error
        }

        return UpdateProfileResult.Success
    }

    suspend fun updatePassword(): UpdatePasswordResult {
        if (oldPasswordField.isBlank() || newPasswordField.isBlank() || confirmPasswordField.isBlank()) {
            return UpdatePasswordResult.EmptyField
        }

        if (newPasswordField != confirmPasswordField) {
            return UpdatePasswordResult.Mismatch
        }

        if (newPasswordField != confirmPasswordField) {
            return UpdatePasswordResult.Mismatch
        }

        try {
            userRepository.updatePassword(
                token,
                ChangePasswordForm(
                    oldPassword = oldPasswordField,
                    newPassword = newPasswordField
                )
            )
        } catch (e: HttpException) {
            Log.e(TAG, "Error: (${e.javaClass}) ${e.message}")
            Log.d(TAG, "Password Lama: $oldPasswordField")
            Log.d(TAG, "Password Baru: $newPasswordField")
            Log.d(TAG, "Konfirmasi Password: $confirmPasswordField")
            return when (e.code()) {
                401 -> UpdatePasswordResult.WrongOldPassword
                else -> UpdatePasswordResult.Error
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: (${e.javaClass}) ${e.message}")
            Log.d(TAG, "Password Lama: $oldPasswordField")
            Log.d(TAG, "Password Baru: $newPasswordField")
            Log.d(TAG, "Konfirmasi Password: $confirmPasswordField")
            return UpdatePasswordResult.Error
        }

        return UpdatePasswordResult.Success
    }

    suspend fun deleteAccount(): DeleteAccountResult {
        try {
            userRepository.deleteUser(token)
        } catch (e: Exception) {
            Log.e(TAG, "Error: (${e.javaClass}) ${e.message}")
            return DeleteAccountResult.Error
        }

        userPreferencesRepository.saveToken("")
        userPreferencesRepository.saveName("")
        userPreferencesRepository.saveNip("")
        userPreferencesRepository.saveEmail("")
        userPreferencesRepository.saveRoles(emptyList())

        return DeleteAccountResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                ProfileViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = application.container.userRepository
                )
            }
        }
    }
}

enum class UpdateProfileResult {
    Success,
    Error,
    EmptyField
}

enum class UpdatePasswordResult {
    Success,
    WrongOldPassword,
    Mismatch,
    Error,
    EmptyField
}

enum class DeleteAccountResult {
    Success,
    Error
}