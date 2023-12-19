package com.zabbarfalih.sipstis

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.zabbarfalih.sipstis.data.AppContainer
import com.zabbarfalih.sipstis.data.DefaultAppContainer
import com.zabbarfalih.sipstis.data.UserPreferencesRepository

private const val LOGGED_IN_USER_PREFERENCE_NAME = "logged_in_user_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LOGGED_IN_USER_PREFERENCE_NAME
)

class SipstisApplication : Application() {
    lateinit var container: AppContainer
    lateinit var userPreferenceRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        userPreferenceRepository = UserPreferencesRepository(dataStore)
    }
}