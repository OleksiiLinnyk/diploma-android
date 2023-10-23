package com.demo.diploma.api

import com.demo.diploma.model.request.TestUpsertRequest
import com.demo.diploma.model.response.TestResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TestAPI {

    @GET("/api/test/my")
    fun getTeacherTest(@Header("Cookie") token: String): Call<List<TestResponse>>

    @PUT("/api/test/enable/{testId}/{enabled}")
    fun enableTest(
        @Header("Cookie") token: String,
        @Path("testId") testId: Long,
        @Path("enabled") enabled: Boolean
    ): Call<TestResponse>

    @POST("/api/test")
    fun createTest(
        @Header("Cookie") token: String,
        @Body request: TestUpsertRequest
    ): Call<TestResponse>

    @DELETE("/api/test/{testId}")
    fun deleteTest(
        @Header("Cookie") token: String,
        @Path("testId") testId: Long
    ): Call<ResponseBody>
}