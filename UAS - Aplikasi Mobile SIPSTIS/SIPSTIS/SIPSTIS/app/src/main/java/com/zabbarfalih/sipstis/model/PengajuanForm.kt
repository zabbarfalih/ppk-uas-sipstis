package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class PengajuanForm(
    val namaPengadaan: String,
    val tanggalPengadaan: String,
    val deskripsiPengadaan: String,
    val jumlah: Int,
    val estimasiHargaSatuan: Double
)