package com.zabbarfalih.sipstis.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.zabbarfalih.sipstis.SipstisApplication
import com.zabbarfalih.sipstis.data.PengajuanRepository
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.data.UserRepository
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.model.User
import kotlinx.coroutines.launch

private const val TAG = "PPKViewModel"

class PPKViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository,
    private val pengajuanRepository: PengajuanRepository
) : ViewModel() {
    private lateinit var userState: UserState
    private lateinit var user: User

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect {
                userState = it
                try {
                    user = userRepository.getProfile(it.token)
                } catch (e: Exception) {
                    Log.e(TAG, e.stackTraceToString())
                }
            }
        }
    }

    val pengajuans = mutableStateOf<List<Pengajuan>>(emptyList())

    fun fetchPengajuans() {
        viewModelScope.launch {
            try {
                val allPengajuan = pengajuanRepository.getAllPengajuan(userState.token)
                val filteredPengajuan = allPengajuan.filter { it.isPengajuanPPK }
                val filtered2Pengajuan = filteredPengajuan.filter { it.status != StatusEnum.DITOLAK }
                val filtered3Pengajuan = filtered2Pengajuan.filter { it.status != StatusEnum.REVISI }
                pengajuans.value = filtered3Pengajuan
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching pengajuans: ${e.message}")
            }
        }
    }

    init {
        fetchPengajuans()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SipstisApplication)
                PPKViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = application.container.userRepository,
                    pengajuanRepository = application.container.pengajuanRepository
                )
            }
        }
    }
}