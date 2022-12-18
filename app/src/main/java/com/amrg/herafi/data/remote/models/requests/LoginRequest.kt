package com.amrg.herafi.data.remote.models.requests

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(@Contextual val user: User) {
    @Serializable
    data class User(val email: String, val password: String)
}
