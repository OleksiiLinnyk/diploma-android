package com.demo.diploma.usecase.user

import android.util.Log
import com.demo.diploma.api.GroupAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.GroupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchGroupUseCase {

    private val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
        .create(GroupAPI::class.java)

    fun search(searchInput: String) : GroupResponse? {
        val groupByNameCall: Call<GroupResponse> = groupAPI.getGroupByName(
            "token=${TokenHolder.getToken()}", searchInput
        )
        var result: GroupResponse? = null
        groupByNameCall.enqueue(object : Callback<GroupResponse> {
            override fun onResponse(
                call: Call<GroupResponse>,
                response: Response<GroupResponse>
            ) {
                if (response.isSuccessful) {
                    val body: GroupResponse? = response.body()
                    result = body
                }
            }

            override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
        return result
    }
}