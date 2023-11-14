package com.demo.diploma.usecase.exercise

import android.util.Log
import com.demo.diploma.api.ExerciseAPI
import com.demo.diploma.callback.ExerciseByIdCallback
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.ExerciseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetExerciseByIdCase {

    private val exerciseAPI: ExerciseAPI = RetrofitConfiguration.getInstance()
        .create(ExerciseAPI::class.java)

    fun execute(exerciseId: Long, callback: ExerciseByIdCallback) {
        val call: Call<ExerciseResponse> = exerciseAPI.getExerciseById(
            "token=${TokenHolder.getToken()}",
            exerciseId
        )

        call.enqueue(object : Callback<ExerciseResponse> {
            override fun onResponse(
                call: Call<ExerciseResponse>,
                response: Response<ExerciseResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<ExerciseResponse>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}