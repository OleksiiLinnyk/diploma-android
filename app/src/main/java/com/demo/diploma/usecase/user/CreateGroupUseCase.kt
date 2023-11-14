package com.demo.diploma.usecase.user

import android.util.Log
import android.view.View
import android.widget.Toast
import com.demo.diploma.api.GroupAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroupUseCase {

    private val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
        .create(GroupAPI::class.java)

    fun createGroup(groupName: String, view: View) {
        val call: Call<ResponseBody> =
            groupAPI.createGroup("token=${TokenHolder.getToken()}", groupName)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    view.context,
                    "Group created $groupName successfully",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }
}