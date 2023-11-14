package com.demo.diploma.usecase.test

import android.content.Context
import android.content.Intent
import android.util.Log
import com.demo.diploma.activity.teacher.TestDetailsActivity
import com.demo.diploma.api.TestAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssignTestToGroupUseCase {

    private val testAPI: TestAPI = RetrofitConfiguration.getInstance()
        .create(TestAPI::class.java)

    fun execute(testId: Long, groupId: Long, context: Context) {
        val call: Call<ResponseBody> =
            testAPI.assignTestToGroup("token=${TokenHolder.getToken()}", testId, groupId)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(context, TestDetailsActivity::class.java)
                    intent.putExtra("testId", testId)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    (context.applicationContext).startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}