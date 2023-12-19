package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Long?,
    val name: String,
    val nip: String,
    val email: String,
    val noTelepon: String
)