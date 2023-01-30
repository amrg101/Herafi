package com.amrg.herafi.data.repositeries

import com.amrg.herafi.data.remote.models.responses.ProjectsResponse
import com.amrg.herafi.di.BASE_API_URL
import com.amrg.herafi.domain.models.Project
import com.amrg.herafi.domain.models.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject


private const val PROJECTS_URL = "/projects"
private const val FAVORITE_PROJECTS_URL = "/favorite"

class ProjectsRepository @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun getProjects(user: User, tag: String? = null) : ProjectsResponse{
        return httpClient.get {
            url("$BASE_API_URL${PROJECTS_URL}").apply {
                parameter("tag", tag)
            }
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }.body()
    }

    suspend fun getProject(slug: String, user: User) : ProjectsResponse{
        return httpClient.get {
            url("$BASE_API_URL$slug")
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }.body()
    }

    suspend fun createProject(project: Project, user: User){
        httpClient.post {
            url("$BASE_API_URL${PROJECTS_URL}")
            setBody(project)
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }
    }

    suspend fun updateProject(project: Project, slug: String, user: User){
        httpClient.put {
            url("$BASE_API_URL$PROJECTS_URL$slug")
            setBody(project)
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }
    }

    suspend fun deleteProject(slug: String, user: User){
        httpClient.delete {
            url("$BASE_API_URL$PROJECTS_URL$slug")
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }
    }

    suspend fun addFavoriteProject(slug: String, user: User){
        httpClient.post {
            url("$BASE_API_URL$PROJECTS_URL/$slug$FAVORITE_PROJECTS_URL")
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }
    }

    suspend fun deleteFavoriteProject(slug: String, user: User){
        httpClient.delete {
            url("$BASE_API_URL$PROJECTS_URL/$slug$FAVORITE_PROJECTS_URL")
            header(HttpHeaders.Authorization, "Token ${user.token}")
        }
    }
}