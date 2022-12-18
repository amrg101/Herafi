package com.amrg.herafi.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("username")
    val name: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("token")
    val token: String = "",

    @SerialName("first_launch")
    val firstLaunch: Boolean = true
)
