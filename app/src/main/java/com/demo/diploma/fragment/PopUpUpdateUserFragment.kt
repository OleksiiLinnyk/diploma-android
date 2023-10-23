package com.demo.diploma.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.demo.diploma.R
import com.demo.diploma.api.UsersAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.request.UpdateUserRequest
import com.demo.diploma.model.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopUpUpdateUserFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pop_up_update_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updateBtn: Button = view.findViewById(R.id.update_user_btn)
        val usernameET: EditText = view.findViewById(R.id.update_user_name)
        val userEmailET: EditText = view.findViewById(R.id.update_user_email)
        val passwordET: EditText = view.findViewById(R.id.update_user_password)
        val confirmPasswordET: EditText = view.findViewById(R.id.update_user_repassword)
        val userAPI: UsersAPI = RetrofitConfiguration.getInstance()
            .create(UsersAPI::class.java)


        updateBtn.setOnClickListener {
            val username: String = usernameET.text.toString()
            val email: String = userEmailET.text.toString()
            val password: String = passwordET.text.toString()
            val confirmPassword: String = confirmPasswordET.text.toString()

            if (username.isNullOrEmpty() && email.isNullOrEmpty() && password.isNullOrEmpty()) {
                Toast.makeText(
                    view.context,
                    "Please fill empty blanket",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                val call = userAPI.updateUser(
                    "token=${TokenHolder.getToken()}",
                    UpdateUserRequest(username, email, password, confirmPassword)
                )

                call.enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        Toast.makeText(
                            view.context,
                            "User was successfully updated",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.v("Retrofit", "call failed ${t.message}")
                    }
                })
                dismiss()
            }
        }
    }
}