package com.demo.helloworld.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.demo.helloworld.R
import com.demo.helloworld.activity.AdminGroupsActivity
import com.demo.helloworld.api.GroupAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.configuration.TokenHolder
import com.demo.helloworld.model.request.UpdateGroupRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopUpUpdateGroupFragment() : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updateBtn = view.findViewById<Button>(R.id.update_group_btn)
        val groupInput = view.findViewById<EditText>(R.id.input_group_name)
        val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
            .create(GroupAPI::class.java)

        updateBtn.setOnClickListener {
            val groupName = groupInput.text.toString()
            val id = arguments?.getLong("id")
            if (groupName.isNullOrEmpty() || id == null) {
                Toast.makeText(view.context, "Please specify the group name", Toast.LENGTH_LONG).show()
            } else {
                val call: Call<ResponseBody> =
                    groupAPI.updateGroup(
                        "token=${TokenHolder.getToken()}",
                        UpdateGroupRequest(id, groupName)
                    )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Toast.makeText(
                            view.context,
                            "Group name update to $groupName successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.v("Retrofit", "call failed ${t.message}")
                    }
                })
                val intent = Intent(view.context, AdminGroupsActivity::class.java)
                startActivity(intent)
            }

        }
    }
}