package com.demo.diploma.api

import com.demo.diploma.model.request.TestUpsertRequest
import com.demo.diploma.model.response.GroupsProgressResponse
import com.demo.diploma.model.response.StudentTestResultResponse
import com.demo.diploma.model.response.StudentsProgressResponse
import com.demo.diploma.model.response.TestResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/api/test/{testId}")
    fun getTestById(
        @Header("Cookie") token: String,
        @Path("testId") testId: Long
    ): Call<TestResponse>

    @PUT("/api/test/{testId}")
    fun updateTest(
        @Header("Cookie") token: String,
        @Body testRequest: TestUpsertRequest,
        @Path("testId") testId: Long
    ): Call<TestResponse>

    @PUT("/api/test/assign-to-group")
    fun assignTestToGroup(
        @Header("Cookie") token: String,
        @Query("testId") testId: Long,
        @Query("groupId") groupId: Long
    ): Call<ResponseBody>

    @GET("/api/test/my/test")
    fun getStudentTests(
        @Header("Cookie") token: String,
        @Query("status") status: String
    ): Call<List<TestResponse>>

    @GET("/api/test/my")
    suspend fun getTeacherTests(@Header("Cookie") token: String): Response<List<TestResponse>>

    @GET("/api/test/progress/{testId}")
    suspend fun getGroupResultsByTestId(@Header("Cookie") token: String, @Path("testId") testId: Long): Response<List<GroupsProgressResponse>>

    @GET("/api/test/userProgress")
    suspend fun getUserResultsByTestAndGroupId(
        @Header("Cookie") token: String,
        @Query("testId") testId: Long,
        @Query("groupId") groupId: Long
    ): Response<List<StudentsProgressResponse>>

    @GET("/api/test/my/result")
    suspend fun getStudentTestsResult(
        @Header("Cookie") token: String
    ): Response<List<StudentTestResultResponse>>
}