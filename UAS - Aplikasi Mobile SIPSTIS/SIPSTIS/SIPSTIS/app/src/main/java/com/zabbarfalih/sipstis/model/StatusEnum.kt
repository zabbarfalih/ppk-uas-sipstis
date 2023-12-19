package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
enum class StatusEnum {
    MENUNGGU_PERSETUJUAN,
    DITOLAK,
    REVISI,
    DIPROSES_PBJ,
    DIPROSES_PPK,
    DIPROSES_PBJ_PEMBELIAN,
    DIPROSES_PPK_PEMBELIAN,
    SELESAI
}
