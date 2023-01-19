package com.amrg.herafi.data.remote.models.requests

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EditProfileRequest(@Contextual val user: User) {
    @Serializable
    data class User(

        @SerialName("username")
        val username: String?,

        @SerialName("old_password")
        val oldPassword: String?,

        @SerialName("password")
        val password: String?
    )

}
