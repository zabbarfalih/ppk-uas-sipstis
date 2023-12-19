package com.zabbarfalih.sipstis.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zabbarfalih.sipstis.SipstisApplication
import com.zabbarfalih.sipstis.data.UserRepository
import com.zabbarfalih.sipstis.model.RegisterForm


private const val TAG = "RegisterViewModel"

enum class RegisterResult {
    Success,
    EmptyField,
    PasswordMismatch,
    Error,
    InvalidEmail
}

//@Serializable
//data class Role(
//    val id: Int,
//    val name: String
//)

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    var nameField by mutableStateOf("")
        private set
    var nipField by mutableStateOf("")
        private set
    var emailField by mutableStateOf("")
        private set
    var noTeleponField by mutableStateOf("")
        private set
    var passwordField by mutableStateOf("")
        private set
    var confirmPasswordField by mutableStateOf("")
        private set
//    var rolesField by mutableStateOf(listOf(Role(id = 1, name = "ROLE_UNIT")))
//        private set

    fun updateNameField(name: String) {
        nameField = name
    }

    fun updateNipField(nip: String) {
        nipField = nip
    }

    fun updateEmailField(email: String) {
        emailField = email
    }

    fun updateNoTeleponField(noTelepon: String) {
        noTeleponField = noTelepon
    }

    fun updatePasswordField(password: String) {
        passwordField = password
    }

    fun updateConfirmPasswordField(password: String) {
        confirmPasswordField = password
    }

    suspend fun register(): RegisterResult {
        if (nameField == "" || nipField == "" || emailField == "" || noTeleponField == "" || passwordField == "") {
            return RegisterResult.EmptyField
        }
        if (!isValidEmail(emailField)) {
            return RegisterResult.InvalidEmail
        }
        if (passwordField != confirmPasswordField) {
            return RegisterResult.PasswordMismatch
        }

        try {
            userRepository.register(RegisterForm(nameField, nipField, emailField, noTeleponField, passwordField))
        } catch (e: Exception) {
            Log.e(TAG, "Error: (${e.javaClass}) ${e.message}")
            return RegisterResult.Error
        }

        return RegisterResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                val userRepository = application.container.userRepository
                RegisterViewModel(userRepository = userRepository)
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}