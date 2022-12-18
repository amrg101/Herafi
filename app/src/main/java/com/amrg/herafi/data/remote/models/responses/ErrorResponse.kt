package com.amrg.herafi.data.remote.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("errors")
    val message: String
)
