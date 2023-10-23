package com.demo.helloworld.api

import com.demo.helloworld.model.request.ExerciseRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ExerciseAPI {

    @POST("/api/exercise")
    fun createExercise(@Header("Cookie") token: String, @Body request: ExerciseRequest) : Call<ResponseBody>
}