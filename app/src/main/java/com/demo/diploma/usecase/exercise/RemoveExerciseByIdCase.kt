package com.demo.diploma.usecase.exercise

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.demo.diploma.activity.teacher.TestDetailsActivity
import com.demo.diploma.api.ExerciseAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.util.ShowPopupNotificationUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoveExerciseByIdCase {

    private val exerciseAPI: ExerciseAPI = RetrofitConfiguration.getInstance()
        .create(ExerciseAPI::class.java)


    fun execute(exerciseId: Long, context: Context) {
        val call: Call<ResponseBody> = exerciseAPI.deleteExerciseById(
            "token=${TokenHolder.getToken()}",
            exerciseId
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    ShowPopupNotificationUtil.showPopup("Exercise successfully deleted", context)
                    val intent = Intent(context, TestDetailsActivity::class.java)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}