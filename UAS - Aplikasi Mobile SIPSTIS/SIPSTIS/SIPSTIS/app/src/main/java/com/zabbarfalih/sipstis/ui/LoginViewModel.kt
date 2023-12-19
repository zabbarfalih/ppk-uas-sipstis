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
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.data.UserRepository
import com.zabbarfalih.sipstis.model.LoginForm
import retrofit2.HttpException

private const val TAG = "LoginViewModel"

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var emailField by mutableStateOf("")
        private set

    var passwordField by mutableStateOf("")
        private set

    fun updateEmail(email: String) {
        emailField = email
    }

    fun updatePassword(password: String) {
        passwordField = password
    }

    suspend fun attemptLogin(): LoginResult {
        try {
            val loginResponse = userRepository.login(LoginForm(emailField, passwordField))
            Log.d(TAG, "accessToken: ${loginResponse.accessToken}")
            userPreferencesRepository.saveToken(loginResponse.accessToken)

            val user = userRepository.getProfile(loginResponse.accessToken)

            Log.d(TAG, "name: ${user.name}")
            Log.d(TAG, "nip: ${user.nip}")
            Log.d(TAG, "email: ${user.email}")
            Log.d(TAG, "notelepon: ${user.noTelepon}")

            userPreferencesRepository.saveName(user.name)
            userPreferencesRepository.saveNip(user.nip)
            userPreferencesRepository.saveEmail(user.email)
            userPreferencesRepository.saveNoTelepon(user.noTelepon)
            userPreferencesRepository.saveRoles(loginResponse.roles)

            return LoginResult.Success
        } catch(e: HttpException) {
            when (e.code()) {
                400 -> {
                    Log.d(TAG, "Email: $emailField")
                    Log.d(TAG, "Password: $passwordField")
                    return LoginResult.BadInput
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Tidak dapat login: (${e.javaClass}) ${e.message}")
            Log.e(TAG, e.stackTraceToString())
            return LoginResult.Error
        }

        return LoginResult.WrongEmailOrPassword
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                val userRepository = application.container.userRepository
                LoginViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}

enum class LoginResult {
    Success,
    BadInput,
    WrongEmailOrPassword,
    Error
}