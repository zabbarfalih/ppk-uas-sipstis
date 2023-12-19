package com.zabbarfalih.sipstis.model

import androidx.compose.ui.graphics.Color
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


fun StatusEnum.toColor(): Color {
    return when (this) {
        StatusEnum.MENUNGGU_PERSETUJUAN -> Color.Yellow
        StatusEnum.DITOLAK -> Color.Red
        StatusEnum.REVISI -> Color.Blue
        StatusEnum.DIPROSES_PBJ -> Color.Cyan
        StatusEnum.DIPROSES_PPK -> Color.Cyan
        StatusEnum.DIPROSES_PBJ_PEMBELIAN -> Color.Magenta
        StatusEnum.DIPROSES_PPK_PEMBELIAN -> Color.Magenta
        StatusEnum.SELESAI -> Color.Green
    }
}

fun StatusEnum.toTextColor(): Color {
    return when (this) {
        StatusEnum.MENUNGGU_PERSETUJUAN,
        StatusEnum.DITOLAK,
        StatusEnum.DIPROSES_PBJ,
        StatusEnum.DIPROSES_PPK,
        StatusEnum.SELESAI -> Color.Black
        else -> Color.White
    }
}


fun StatusEnum.isDeletable(): Boolean {
    return this in setOf(
        StatusEnum.MENUNGGU_PERSETUJUAN,
        StatusEnum.DITOLAK
    )
}

fun StatusEnum.isRevisable(): Boolean {
    return this in setOf(
        StatusEnum.REVISI
    )
}