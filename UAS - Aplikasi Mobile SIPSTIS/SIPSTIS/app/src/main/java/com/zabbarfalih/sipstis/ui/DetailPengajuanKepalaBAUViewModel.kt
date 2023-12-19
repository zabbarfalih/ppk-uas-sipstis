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
import com.zabbarfalih.sipstis.data.PengajuanRepository
import com.zabbarfalih.sipstis.data.UserPreferencesRepository
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.StatusEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val TAG = "DetailPengajuanKepalaBAUViewModel"

class DetailPengajuanKepalaBAUViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val pengajuanRepository: PengajuanRepository
) : ViewModel() {

    var showConfirmDialog1 by mutableStateOf(false)
    var showConfirmDialog2 by mutableStateOf(false)
    var showConfirmDialog3 by mutableStateOf(false)

    private val _pengajuan = MutableStateFlow<Pengajuan?>(null)
    val pengajuan: StateFlow<Pengajuan?> = _pengajuan

    fun getPengajuanById(pengajuanId: Long) {
        viewModelScope.launch {
            try {
                val result = pengajuanRepository.getPengajuanById(token, pengajuanId)
                if (result.status == StatusEnum.MENUNGGU_PERSETUJUAN) {
                    _pengajuan.value = result
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting pengajuan by id: ${e.message}")
            }
        }
    }

    private lateinit var token: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var nip: String
    private lateinit var noTelepon: String
    private lateinit var roles: List<String>

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                name = user.name
                nip = user.nip
                email = user.email
                noTelepon = user.noTelepon
                roles = user.roles
            }
        }
    }

    suspend fun setujuiPengajuan(pengajuanId: Long): KonfirmasiKepalaBAUResult1 {
        return try {
            pengajuanRepository.setujuiPengajuan(token, pengajuanId)
            KonfirmasiKepalaBAUResult1.Success
        } catch (e: HttpException) {
            Log.e(TAG, "Error setujui pengajuan: ${e.message}")
            KonfirmasiKepalaBAUResult1.Error
        }
    }

    suspend fun tolakPengajuan(pengajuanId: Long): KonfirmasiKepalaBAUResult2 {
        return try {
            pengajuanRepository.tolakPengajuan(token, pengajuanId)
            KonfirmasiKepalaBAUResult2.Success
        } catch (e: HttpException) {
            Log.e(TAG, "Error tolak pengajuan: ${e.message}")
            KonfirmasiKepalaBAUResult2.Error
        }
    }

    suspend fun revisiPengajuan(pengajuanId: Long): KonfirmasiKepalaBAUResult3 {
        return try {
            pengajuanRepository.revisiPengajuan(token, pengajuanId)
            KonfirmasiKepalaBAUResult3.Success
        } catch (e: HttpException) {
            Log.e(TAG, "Error delete pengajuan: ${e.message}")
            KonfirmasiKepalaBAUResult3.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                DetailPengajuanKepalaBAUViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    pengajuanRepository = application.container.pengajuanRepository
                )
            }
        }
    }
}

enum class KonfirmasiKepalaBAUResult1 {
    Success,
    Error
}

enum class KonfirmasiKepalaBAUResult2 {
    Success,
    Error
}

enum class KonfirmasiKepalaBAUResult3 {
    Success,
    Error
}

