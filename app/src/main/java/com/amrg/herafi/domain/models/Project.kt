package com.amrg.herafi.domain.models

import android.os.Parcelable
import com.amrg.herafi.data.remote.models.responses.ProfileResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Project(
    @SerialName("slug")
    val slug: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("body")
    val body: String,
    @SerialName("tagList")
    val tagList: List<String>,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("favorited")
    val favorited: Boolean = false,
    @SerialName("favoritesCount")
    val favoritesCount: Int = 0,
    @SerialName("author")
    val author: ProfileResponse.Profile
) : Parcelable