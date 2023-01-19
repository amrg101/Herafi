package com.amrg.herafi.data.repositeries

import com.amrg.herafi.data.remote.models.requests.EditProfileRequest
import com.amrg.herafi.data.remote.models.requests.LoginRequest
import com.amrg.herafi.data.remote.models.requests.RegisterRequest
import com.amrg.herafi.data.remote.models.responses.UserResponse
import com.hero.ataa.di.BASE_API_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

private const val LOGIN_URL = "/users/login"
private const val REGISTER_URL = "/users/register"
private const val VALIDATE_TOKEN_URL = "/users/tokencheck"
private const val EDIT_PROFILE_URL = "/user"

class AuthRepository @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun login(loginRequest: LoginRequest): UserResponse {
        return httpClient.post {
            url("${BASE_API_URL}${LOGIN_URL}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun register(registerRequest: RegisterRequest) {
        httpClient.post {
            url("${BASE_API_URL}${REGISTER_URL}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(registerRequest)
        }
    }

    suspend fun validateToken(token: String) {
        httpClient.get {
            url("${BASE_API_URL}${VALIDATE_TOKEN_URL}")
            header(HttpHeaders.Authorization, "Token $token")
        }
    }

    suspend fun editProfile(editProfileRequest: EditProfileRequest, token: String): UserResponse {
        return httpClient.put {
            url("${BASE_API_URL}${EDIT_PROFILE_URL}")
            setBody(editProfileRequest)
            header(HttpHeaders.Authorization, "Token $token")
        }.body()
    }
}