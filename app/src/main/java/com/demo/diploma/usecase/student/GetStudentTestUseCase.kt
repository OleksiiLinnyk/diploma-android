package com.demo.diploma.usecase.student

import android.util.Log
import com.demo.diploma.api.TestAPI
import com.demo.diploma.callback.LoadTestCallback
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.TestStatus
import com.demo.diploma.model.response.TestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetStudentTestUseCase {

    private val testAPI: TestAPI = RetrofitConfiguration.getInstance()
        .create(TestAPI::class.java)
    fun execute(callback: LoadTestCallback) {
        val call: Call<List<TestResponse>> =
            testAPI.getStudentTests("token=${TokenHolder.getToken()}", TestStatus.TODO.value)

        call.enqueue(object : Callback<List<TestResponse>> {
            override fun onResponse(
                call: Call<List<TestResponse>>,
                response: Response<List<TestResponse>>
            ) {
                if (response.isSuccessful) {
                    val body: List<TestResponse> = response.body().orEmpty()
                    callback.onSuccess(body)
                }
            }

            override fun onFailure(call: Call<List<TestResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}