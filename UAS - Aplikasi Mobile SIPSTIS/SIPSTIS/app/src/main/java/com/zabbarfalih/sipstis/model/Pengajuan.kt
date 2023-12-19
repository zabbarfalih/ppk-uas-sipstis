package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class Pengajuan(
    val id: Long? = null,
    var namaPengadaan: String,
    var tanggalPengadaan: String,
    var deskripsiPengadaan: String,
    var jumlah: Int,
    var estimasiHargaSatuan: Double,
    val estimasiHargaTotal: Double? = null,
    val status: StatusEnum,
    val isPengajuanPPK: Boolean = false,
    val isPengajuanPBJ: Boolean = false,
)