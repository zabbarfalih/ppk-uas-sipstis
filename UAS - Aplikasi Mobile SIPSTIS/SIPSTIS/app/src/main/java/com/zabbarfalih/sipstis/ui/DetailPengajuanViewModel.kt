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
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val TAG = "DetailPengajuanViewModel"

class DetailPengajuanViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val pengajuanRepository: PengajuanRepository
) : ViewModel() {

    var showConfirmDialog by mutableStateOf(false)

    private val _pengajuan = MutableStateFlow<Pengajuan?>(null)
    val pengajuan: StateFlow<Pengajuan?> = _pengajuan

    fun getPengajuanById(pengajuanId: Long) {
        viewModelScope.launch {
            try {
                val result = pengajuanRepository.getPengajuanById(token, pengajuanId)
                _pengajuan.value = result
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

    var namaPengadaanField by mutableStateOf("")
        private set

    var deskripsiPengadaanField by mutableStateOf("")
        private set

    var jumlahField by mutableStateOf("")
        private set

    var estimasiHargaSatuanField by mutableStateOf("")
        private set

    var tanggalPengadaanField by mutableStateOf("")
        private set

    fun updateNamaPengadaan(namaPengadaan: String) {
        namaPengadaanField = namaPengadaan
    }

    fun updateDeskripsiPengadaan(deskripsiPengadaan: String) {
        deskripsiPengadaanField = deskripsiPengadaan
    }

    fun updateJumlah(jumlah: String) {
        jumlahField = jumlah
    }

    fun updateEstimasiHargaSatuan(estimasiHargaSatuan: String) {
        estimasiHargaSatuanField = estimasiHargaSatuan
    }

    fun updateTanggalPengadaan(tanggalPengadaan: String) {
        tanggalPengadaanField = tanggalPengadaan
    }

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

    suspend fun updatePengajuan(pengajuanId: Long, pengajuan: Pengajuan): DetailPengajuanResult {
        if (pengajuan.namaPengadaan.isBlank() || pengajuan.deskripsiPengadaan.isBlank() || pengajuan.jumlah.toString().isBlank() || pengajuan.estimasiHargaSatuan.toString().isBlank() || pengajuan.tanggalPengadaan.isBlank()) {
            Log.d("DetailPengajuanVM", "Bad input detected")
            return DetailPengajuanResult.BadInput
        }

        Log.d("DetailPengajuanVM", "Attempting to update pengajuan: $pengajuan")

        return try {
            pengajuanRepository.updatePengajuan(token, pengajuanId, pengajuan)
            Log.d("DetailPengajuanVM", "Update successful")
            DetailPengajuanResult.Success
        } catch (e: Exception) {
            Log.e("DetailPengajuanVM", "Error updating pengajuan: ${e.message}", e)
            DetailPengajuanResult.Error
        }
    }

    suspend fun deletePengajuan(pengajuanId: Long): DeletePengajuanResult {
        return try {
            pengajuanRepository.deletePengajuan(token, pengajuanId)
            DeletePengajuanResult.Success
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting pengajuan: ${e.message}")
            DeletePengajuanResult.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                DetailPengajuanViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    pengajuanRepository = application.container.pengajuanRepository
                )
            }
        }
    }
}

enum class DetailPengajuanResult {
    Success,
    BadInput,
    Error
}