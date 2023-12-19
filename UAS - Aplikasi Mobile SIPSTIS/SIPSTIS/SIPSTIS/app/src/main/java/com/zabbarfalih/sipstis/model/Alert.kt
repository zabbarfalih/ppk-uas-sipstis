package com.zabbarfalih.sipstis.model

import kotlinx.serialization.Serializable

@Serializable
data class Alert(
    val id: Long?,
    val name: String
)