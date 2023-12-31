package com.demo.diploma.api

import com.demo.diploma.model.request.UpdateGroupRequest
import com.demo.diploma.model.response.GroupResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface GroupAPI {

    @GET("/api/group")
    fun getAllGroups(@Header("Cookie") token: String): Call<List<GroupResponse>>

    @POST("/api/group")
    fun createGroup(
        @Header("Cookie") token: String,
        @Query("groupName") name: String
    ): Call<ResponseBody>

    @GET("/api/group/name")
    fun getGroupByName(
        @Header("Cookie") token: String,
        @Query("groupName") name: String
    ): Call<GroupResponse>

    @DELETE("/api/group")
    fun deleteGroup(
        @Header("Cookie") token: String,
        @Query("groupId") groupId: Long
    ): Call<ResponseBody>

    @PUT("/api/group/update")
    fun updateGroup(
        @Header("Cookie") token: String,
        @Body request: UpdateGroupRequest
    ): Call<ResponseBody>

    @GET("/api/group/test")
    fun getGroupsByTestId(
        @Header("Cookie") token: String,
        @Query("testId") testId: Long
    ): Call<List<GroupResponse>>

    @GET("/api/group/available/test")
    fun getAvailableGroupsByTestId(
        @Header("Cookie") token: String,
        @Query("testId") testId: Long
    ): Call<List<GroupResponse>>
}