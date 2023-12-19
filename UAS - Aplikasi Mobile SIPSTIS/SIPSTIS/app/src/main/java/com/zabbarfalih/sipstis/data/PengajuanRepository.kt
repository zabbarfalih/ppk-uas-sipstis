package com.zabbarfalih.sipstis.data

import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.service.PengajuanService

interface PengajuanRepository {
    suspend fun createPengajuan(token: String, pengajuan: PengajuanForm)
    suspend fun getAllPengajuan(token: String): List<Pengajuan>
    suspend fun getPengajuanById(token: String, pengajuanId: Long): Pengajuan
    suspend fun deletePengajuan(token: String, pengajuanId: Long)
    suspend fun updatePengajuan(token: String, pengajuanId: Long, pengajuan: Pengajuan)

    // Kepala BAU
    suspend fun setujuiPengajuan(token: String, pengajuanId: Long)
    suspend fun tolakPengajuan(token: String, pengajuanId: Long)
    suspend fun revisiPengajuan(token: String, pengajuanId: Long)

    // PBJ
    suspend fun pembelianPengajuanPbj(token: String, pengajuanId: Long)
    suspend fun selesaikanPengajuanPbj(token: String, pengajuanId: Long)

    // PPK
    suspend fun pembelianPengajuanPpk(token: String, pengajuanId: Long)
    suspend fun selesaikanPengajuanPpk(token: String, pengajuanId: Long)
}

class NetworkPengajuanRepository(private val pengajuanService: PengajuanService) : PengajuanRepository {
    override suspend fun createPengajuan(token: String, pengajuan: PengajuanForm) = pengajuanService.createPengajuan("Bearer $token", pengajuan)
    override suspend fun getAllPengajuan(token: String) = pengajuanService.getAllPengajuan("Bearer $token")
    override suspend fun getPengajuanById(token: String, pengajuanId: Long): Pengajuan = pengajuanService.getPengajuanById("Bearer $token", pengajuanId)
    override suspend fun deletePengajuan(token: String, pengajuanId: Long) = pengajuanService.deletePengajuan("Bearer $token", pengajuanId)
    override suspend fun updatePengajuan(token: String, pengajuanId: Long, pengajuan: Pengajuan) = pengajuanService.updatePengajuan("Bearer $token", pengajuanId, pengajuan)

    // Kepala BAU
    override suspend fun setujuiPengajuan(token: String, pengajuanId: Long) = pengajuanService.setujuiPengajuan("Bearer $token", pengajuanId)
    override suspend fun tolakPengajuan(token: String, pengajuanId: Long) = pengajuanService.tolakPengajuan("Bearer $token", pengajuanId)
    override suspend fun revisiPengajuan(token: String, pengajuanId: Long) = pengajuanService.revisiPengajuan("Bearer $token", pengajuanId)

    // PBJ
    override suspend fun pembelianPengajuanPbj(token: String, pengajuanId: Long) = pengajuanService.pembelianPengajuanPbj("Bearer $token", pengajuanId)
    override suspend fun selesaikanPengajuanPbj(token: String, pengajuanId: Long) = pengajuanService.selesaikanPengajuanPbj("Bearer $token", pengajuanId)

    // PPK
    override suspend fun pembelianPengajuanPpk(token: String, pengajuanId: Long) = pengajuanService.pembelianPengajuanPpk("Bearer $token", pengajuanId)
    override suspend fun selesaikanPengajuanPpk(token: String, pengajuanId: Long) = pengajuanService.selesaikanPengajuanPpk("Bearer $token", pengajuanId)

}