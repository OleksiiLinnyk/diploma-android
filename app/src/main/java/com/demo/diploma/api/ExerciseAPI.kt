package com.demo.diploma.api

import com.demo.diploma.model.request.ExerciseRequest
import com.demo.diploma.model.response.ExerciseResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseAPI {

    @POST("/api/exercise")
    fun createExercise(
        @Header("Cookie") token: String, @Body request: ExerciseRequest
    ): Call<ResponseBody>

    @GET("/api/exercise/all/test/{testId}")
    fun getAllByTestId(
        @Header("Cookie") token: String, @Path("testId") testId: Long
    ): Call<List<ExerciseResponse>>

    @GET("/api/exercise")
    fun getExerciseById(
        @Header("Cookie") token: String, @Query("exerciseId") exerciseId: Long
    ): Call<ExerciseResponse>

    @DELETE("/api/exercise")
    fun deleteExerciseById(
        @Header("Cookie") token: String, @Query("exerciseId") exerciseId: Long
    ): Call<ResponseBody>
}