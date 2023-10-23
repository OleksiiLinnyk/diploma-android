package com.demo.helloworld.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.demo.helloworld.util.BottomNavigationUtil
import com.demo.helloworld.R
import com.demo.helloworld.api.AuthAPI
import com.demo.helloworld.api.GroupAPI
import com.demo.helloworld.api.RoleAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.configuration.TokenHolder
import com.demo.helloworld.model.response.GroupResponse
import com.demo.helloworld.model.Role
import com.demo.helloworld.model.response.RoleResponse
import com.demo.helloworld.model.response.UserResponse
import com.demo.helloworld.model.request.UserRegisterRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user_layout)
        val roleAPI: RoleAPI = RetrofitConfiguration.getInstance()
            .create(RoleAPI::class.java)
        val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
            .create(GroupAPI::class.java)
        val authAPI: AuthAPI = RetrofitConfiguration.getInstance()
            .create(AuthAPI::class.java)
        val call: Call<List<RoleResponse>> = roleAPI.getAllRoles("token=${TokenHolder.getToken()}")
        val callGroup: Call<List<GroupResponse>> =
            groupAPI.getAllGroups("token=${TokenHolder.getToken()}")

        val roles: MutableList<String> = mutableListOf()
        val groups: MutableList<String> = mutableListOf()

        setupRolesToSelector(call, roles)
        setupGroupsToSelector(callGroup, groups)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(this@CreateUserActivity, bottomNavigationView, AdminActivity::class.java)

        val registerBtn: Button = findViewById(R.id.registration_btn)
        val name: EditText = findViewById(R.id.userName)
        val email: EditText = findViewById(R.id.userEmail)
        val password: EditText = findViewById(R.id.userPassword)
        val rePassword: EditText = findViewById(R.id.userRePassword)
        val role: AutoCompleteTextView = findViewById(R.id.roleTextView)
        val group: AutoCompleteTextView = findViewById(R.id.groupTextView)
        val subject: EditText = findViewById(R.id.userSubject)
        //TODO do passwords check

        registerBtn.setOnClickListener {
            val inputTextName: String = name.text.toString()
            val inputTextPassword: String = password.text.toString()
            val inputEmail: String = email.text.toString()
            val inputSubject: String = subject.text.toString()
            val inputRole: String = role.text.toString()
            val inputGroup: String = group.text.toString()

            val response: LiveData<Response<UserResponse>> = liveData {
                val r = authAPI.signUp(
                    UserRegisterRequest(
                        inputTextName,
                        inputEmail,
                        inputGroup,
                        inputSubject,
                        inputTextPassword,
                        Role.valueOf(inputRole.uppercase()).value
                    ),
                    "token=${TokenHolder.getToken()}"
                )
                emit(r)
            }

            response.observe(this, Observer {
                val user = it.body()
                Log.v("User=", it.toString())
                if (it.code() != 200) {
                    Toast.makeText(
                        this@CreateUserActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (user != null) {
                    Toast.makeText(
                        this@CreateUserActivity,
                        "User successfully created $inputTextName",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(applicationContext, AdminUsersActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setupRolesToSelector(
        call: Call<List<RoleResponse>>,
        roles: MutableList<String>
    ) {
        call.enqueue(object : Callback<List<RoleResponse>> {
            override fun onResponse(
                call: Call<List<RoleResponse>>,
                response: Response<List<RoleResponse>>
            ) {
                if (response.isSuccessful) {
                    val body: List<RoleResponse> = response.body().orEmpty()
                    roles.addAll(body.map { Role.getByName(it.name).displayName }
                        .toList())
                }
            }

            override fun onFailure(call: Call<List<RoleResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        }
        )
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.roleTextView)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.roles, roles)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun setupGroupsToSelector(
        callGroup: Call<List<GroupResponse>>,
        groups: MutableList<String>
    ) {
        callGroup.enqueue(object : Callback<List<GroupResponse>> {
            override fun onResponse(
                call: Call<List<GroupResponse>>,
                response: Response<List<GroupResponse>>
            ) {
                if (response.isSuccessful) {
                    val body: List<GroupResponse> = response.body().orEmpty()
                    groups.addAll(body.map { it.name }
                        .toList())
                }
            }

            override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        }
        )
        val autoCompleteTextView: AutoCompleteTextView = findViewById(R.id.groupTextView)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.groups, groups)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }
}