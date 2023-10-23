package com.demo.diploma.api

import com.demo.diploma.model.request.UpdateUserRequest
import com.demo.diploma.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface UsersAPI {

    @GET("/api/user/all")
    fun getAllUsers(@Header("Cookie") token: String): Call<List<UserResponse>>

    @PUT("/api/user")
    fun updateUser(
        @Header("Cookie") token: String,
        @Body request: UpdateUserRequest
    ): Call<UserResponse>

    @GET("/api/user/students/group")
    fun getStudentsByGroup(
        @Header("Cookie") token: String,
        @Query("groupName") groupName: String
    ): Call<List<UserResponse>>
}