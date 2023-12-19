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
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val TAG = "CreatePengajuanViewModel"

class CreatePengajuanViewModel (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val pengajuanRepository: PengajuanRepository
) : ViewModel() {

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

    fun updateTanggalPengadaan(tanggalEstimasiPengadaan: String) {
        tanggalPengadaanField = tanggalEstimasiPengadaan
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

    suspend fun createPengajuan(): CreatePengajuanResult {
        if (namaPengadaanField.isBlank() ||
            deskripsiPengadaanField.isBlank() ||
            jumlahField.isBlank() ||
            estimasiHargaSatuanField.isBlank() ||
            tanggalPengadaanField.isBlank()
        ) {
            return CreatePengajuanResult.BadInput
        }

        try {
            val pengajuanForm = PengajuanForm(
                namaPengadaan = namaPengadaanField,
                deskripsiPengadaan = deskripsiPengadaanField,
                jumlah = jumlahField.toInt(),
                estimasiHargaSatuan = estimasiHargaSatuanField.toDouble(),
                tanggalPengadaan = tanggalPengadaanField
            )
            pengajuanRepository.createPengajuan(token, pengajuanForm)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating pengajuan: ${e.message}")
            return CreatePengajuanResult.Error
        }

        return CreatePengajuanResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SipstisApplication)
                CreatePengajuanViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    pengajuanRepository = application.container.pengajuanRepository
                )
            }
        }
    }
}
enum class CreatePengajuanResult {
    Success,
    BadInput,
    Error
}
