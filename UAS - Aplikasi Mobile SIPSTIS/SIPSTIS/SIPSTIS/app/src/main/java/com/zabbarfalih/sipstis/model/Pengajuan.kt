package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class Pengajuan(
    val id: Long? = null,
    val namaPengadaan: String,
    val tanggalPengadaan: String,
    val deskripsiPengadaan: String,
    val jumlah: Int,
    val estimasiHargaSatuan: Double,
    val estimasiHargaTotal: Double? = null,
    val status: StatusEnum,
    val isPengajuanPPK: Boolean = false,
    val isPengajuanPBJ: Boolean = false,
)