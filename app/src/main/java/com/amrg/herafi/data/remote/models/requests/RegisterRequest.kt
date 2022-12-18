package com.amrg.herafi.data.remote.models.requests

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(@Contextual val user: User) {
    @Serializable
    data class User(
        @SerialName("email")
        val email: String,

        @SerialName("username")
        val username: String,

        @SerialName("password")
        val password: String
        )
}
