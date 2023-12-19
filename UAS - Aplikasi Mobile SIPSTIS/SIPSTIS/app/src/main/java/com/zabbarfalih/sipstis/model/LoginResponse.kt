package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val email: String,
    val accessToken: String,
    val roles: List<String>
)
