package com.amrg.herafi.data.remote.models.responses

import android.os.Parcelable
import com.amrg.herafi.domain.models.Project
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectsResponse(
    @SerialName("projects")
    val projects: List<Project>,
    @SerialName("projectsCount")
    val projectsCount: Int
)

@Serializable
data class ProfileResponse(@Contextual val profile: Profile? = null) {
    @Serializable
    @Parcelize
    data class Profile(val username: String, val bio: String, val image: String?, val following: Boolean = false) : Parcelable
}