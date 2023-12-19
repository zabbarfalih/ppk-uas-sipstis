package com.zabbarfalih.sipstis.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val TOKEN = stringPreferencesKey("user_token")
        val NAME = stringPreferencesKey("user_name")
        val NIP = stringPreferencesKey("user_nip")
        val EMAIL = stringPreferencesKey("user_email")
        val NOTELEPON = stringPreferencesKey("user_no_telepon")

        val ROLES = stringPreferencesKey("user_roles")
        const val TAG = "UserPreferencesRepo"
    }

    val user: Flow<UserState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences:", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserState(
                preferences[TOKEN] ?: "",
                preferences[NAME] ?: "",
                preferences[NIP] ?: "",
                preferences[EMAIL] ?: "",
                preferences[NOTELEPON] ?: "",
                preferences[ROLES]?.split(",") ?: emptyList()
            )
        }
    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun saveNip(nip: String) {
        dataStore.edit { preferences ->
            preferences[NIP] = nip
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    suspend fun saveNoTelepon(noTelepon: String) {
        dataStore.edit { preferences ->
            preferences[NOTELEPON] = noTelepon
        }
    }

    suspend fun save(name : String, noTelepon : String) {
        dataStore.edit { preferences ->
            preferences[NAME] = name
            preferences[NOTELEPON] = noTelepon
        }
    }

    suspend fun saveRoles(roles: List<String>) {
        dataStore.edit { preferences ->
            preferences[ROLES] = roles.joinToString(",")
        }
    }
}

data class UserState(
    val token: String,
    val name: String,
    val nip: String,
    val email: String,
    val noTelepon: String,
    val roles: List<String>
) {
}
