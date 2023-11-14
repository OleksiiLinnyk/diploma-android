package com.demo.diploma.usecase.user

import android.util.Log
import com.demo.diploma.api.GroupAPI
import com.demo.diploma.callback.LoadAllGroupsCallback
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.GroupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGroupsUseCase {

    private val groupAPI: GroupAPI =
        RetrofitConfiguration.getInstance().create(GroupAPI::class.java)

    fun execute(callback: LoadAllGroupsCallback) {
        val callGroup: Call<List<GroupResponse>> = groupAPI.getAllGroups("token=${TokenHolder.getToken()}")
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