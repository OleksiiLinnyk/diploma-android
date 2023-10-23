package com.demo.diploma.api

import com.demo.diploma.model.request.LoginRequest
import com.demo.diploma.model.response.LoginResponse
import com.demo.diploma.model.response.MessageResponse
import com.demo.diploma.model.response.UserResponse
import com.demo.diploma.model.request.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAPI {

    @POST("/api/auth/signin")
    suspend fun signIn(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/signout")
    suspend fun signOut(): Response<MessageResponse>

    @POST("/api/auth/signup")
    suspend fun signUp(
        @Body request: UserRegisterRequest,
        @Header("Cookie") token: String
    ): Response<UserResponse>
}