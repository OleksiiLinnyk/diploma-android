package com.demo.helloworld.api

import com.demo.helloworld.model.response.RoleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RoleAPI {

    @GET("/api/role")
    fun getAllRoles(@Header("Cookie") token: String): Call<List<RoleResponse>>
}