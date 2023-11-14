package com.demo.diploma.usecase.test

import android.util.Log
import android.view.View
import android.widget.Toast
import com.demo.diploma.api.TestAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.request.TestUpsertRequest
import com.demo.diploma.model.response.TestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateTestUseCase {

    private val testAPI: TestAPI = RetrofitConfiguration.getInstance()
        .create(TestAPI::class.java)

    fun updateTest(id: Long, subject: String, theme: String, view: View) {
        val call: Call<TestResponse> =
            testAPI.updateTest(
                "token=${TokenHolder.getToken()}",
                TestUpsertRequest(subject, theme),
                id
            )

        call.enqueue(object : Callback<TestResponse> {
            override fun onResponse(
                call: Call<TestResponse>,
                response: Response<TestResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        view.context,
                        "Test successfully updated",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}