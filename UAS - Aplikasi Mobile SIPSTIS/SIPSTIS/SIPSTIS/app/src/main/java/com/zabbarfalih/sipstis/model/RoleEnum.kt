package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
enum class RoleEnum {
    ROLE_UNIT,
    ROLE_ADMIN,
    ROLE_PBJ,
    ROLE_PPK,
    ROLE_KEPALA_BAU
}