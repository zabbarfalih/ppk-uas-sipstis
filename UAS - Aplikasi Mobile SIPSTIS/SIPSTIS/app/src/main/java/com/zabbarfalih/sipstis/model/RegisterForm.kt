package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterForm(
    val name: String,
    val nip: String,
    val email: String,
    val noTelepon: String,
    val password: String
)