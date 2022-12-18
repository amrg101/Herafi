package com.amrg.herafi.data.remote.models.responses

import com.amrg.herafi.domain.models.User
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(@Contextual val user: User) {

    @Serializable
    data class User(
        @SerialName("email")
        val email: String,

        @SerialName("token")
        val token: String,

        @SerialName("username")
        val username: String,

        @SerialName("bio")
        val bio: String,

        @SerialName("image")
        val image: String?
    )

}

fun UserResponse.toUser() = User(email = user.email, name = user.username, token = user.token)
