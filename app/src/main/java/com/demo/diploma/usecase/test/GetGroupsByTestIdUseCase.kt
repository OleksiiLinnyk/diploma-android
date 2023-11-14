package com.demo.diploma.usecase.test

import android.util.Log
import com.demo.diploma.api.GroupAPI
import com.demo.diploma.callback.LoadAllGroupsCallback
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.GroupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGroupsByTestIdUseCase {

    private val groupAPI: GroupAPI =
        RetrofitConfiguration.getInstance().create(GroupAPI::class.java)
    fun execute(callback: LoadAllGroupsCallback, testId: Long) {
        val callGroup: Call<List<GroupResponse>> = groupAPI.getGroupsByTestId("token=${TokenHolder.getToken()}", testId)
        callGroup.enqueue(object : Callback<List<GroupResponse>> {
            override fun onResponse(
                call: Call<List<GroupResponse>>,
                response: Response<List<GroupResponse>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body().orEmpty()
                    callback.onSuccess(body)
                }
            }

            override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}