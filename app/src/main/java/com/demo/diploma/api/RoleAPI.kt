package com.demo.diploma.api

import com.demo.diploma.model.response.RoleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RoleAPI {

    @GET("/api/role")
    fun getAllRoles(@Header("Cookie") token: String): Call<List<RoleResponse>>
}