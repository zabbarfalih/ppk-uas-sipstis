package com.zabbarfalih.sipstis.data

import com.zabbarfalih.sipstis.model.ChangePasswordForm
import com.zabbarfalih.sipstis.model.LoginForm
import com.zabbarfalih.sipstis.model.LoginResponse
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.User
import com.zabbarfalih.sipstis.service.PengajuanService
import com.zabbarfalih.sipstis.service.UserService

interface PengajuanRepository {
    suspend fun createPengajuan(token: String, pengajuan: PengajuanForm)
    suspend fun getAllPengajuan(token: String): List<Pengajuan>
    suspend fun getPengajuanById(token: String, pengajuanId: Long): Pengajuan
    suspend fun deletePengajuan(token: String, pengajuanId: Long)
    suspend fun updatePengajuan(token: String, pengajuanId: Long, pengajuan: Pengajuan)
}

class NetworkPengajuanRepository(private val pengajuanService: PengajuanService) : PengajuanRepository {
    override suspend fun createPengajuan(token: String, pengajuan: PengajuanForm) = pengajuanService.createPengajuan("Bearer $token", pengajuan)
    override suspend fun getAllPengajuan(token: String) = pengajuanService.getAllPengajuan("Bearer $token")
    override suspend fun getPengajuanById(token: String, pengajuanId: Long): Pengajuan = pengajuanService.getPengajuanById("Bearer $token", pengajuanId)
    override suspend fun deletePengajuan(token: String, pengajuanId: Long) = pengajuanService.deletePengajuan("Bearer $token", pengajuanId)
    override suspend fun updatePengajuan(token: String, pengajuanId: Long, pengajuan: Pengajuan) = pengajuanService.updatePengajuan("Bearer $token", pengajuanId, pengajuan)
}