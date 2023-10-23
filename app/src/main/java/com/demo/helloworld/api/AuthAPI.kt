package com.demo.helloworld.api

import com.demo.helloworld.model.request.LoginRequest
import com.demo.helloworld.model.response.LoginResponse
import com.demo.helloworld.model.response.MessageResponse
import com.demo.helloworld.model.response.UserResponse
import com.demo.helloworld.model.request.UserRegisterRequest
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