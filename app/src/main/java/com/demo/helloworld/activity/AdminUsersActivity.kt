package com.demo.helloworld.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.demo.helloworld.R
import com.demo.helloworld.api.GroupAPI
import com.demo.helloworld.api.UsersAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.configuration.TokenHolder
import com.demo.helloworld.model.Role
import com.demo.helloworld.model.response.GroupResponse
import com.demo.helloworld.model.response.UserResponse
import com.demo.helloworld.util.BottomNavigationUtil
import com.demo.helloworld.util.ShowPopupNotificationUtil
import com.demo.helloworld.util.ViewUtil.prepareImageView
import com.demo.helloworld.util.ViewUtil.prepareTextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_layout)

        val usersAPI: UsersAPI = RetrofitConfiguration.getInstance()
            .create(UsersAPI::class.java)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val createUserBtn: Button = findViewById(R.id.create_user_btn)


        loadAllUsers(usersAPI, tableLayout)

        createUserBtn.setOnClickListener {
            val intent = Intent(applicationContext, CreateUserActivity::class.java)
            startActivity(intent)
        }

        searchButtonBehavior(usersAPI, tableLayout)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(this@AdminUsersActivity, bottomNavigationView, AdminActivity::class.java)
    }

    private fun loadAllUsers(
        usersAPI: UsersAPI,
        tableLayout: TableLayout
    ) {
        val call: Call<List<UserResponse>> = usersAPI.getAllUsers("token=${TokenHolder.getToken()}")

        call.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    tableLayout.removeAllViews()
                    val body: List<UserResponse> = response.body().orEmpty()
                    body.forEach { res ->
                        prepareRows(res, tableLayout)
                    }
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }

    private fun searchButtonBehavior(
        userAPI: UsersAPI,
        tableLayout: TableLayout
    ) {
        val searchBtn: Button = findViewById(R.id.search_user_btn)
        searchBtn.setOnClickListener {
            val searchInput: EditText = findViewById(R.id.search_user_input)
            if (searchInput.text.toString().isNullOrEmpty()) {
                loadAllUsers(userAPI, tableLayout)
            } else {
                val userResponsesCall: Call<List<UserResponse>> = userAPI.getStudentsByGroup(
                    "token=${TokenHolder.getToken()}",
                    searchInput.text.toString()
                )

                userResponsesCall.enqueue(object : Callback<List<UserResponse>> {
                    override fun onResponse(
                        call: Call<List<UserResponse>>,
                        response: Response<List<UserResponse>>
                    ) {
                        if (response.isSuccessful) {
                            tableLayout.removeAllViews()
                            val body: List<UserResponse>? = response.body()
                            if (body != null) {
                                body.sortedBy { it.id }
                                    .forEach {
                                        prepareRows(it, tableLayout)
                                    }
                            } else {
                                ShowPopupNotificationUtil.showPopup(
                                    "Users by group has not been found",
                                    this@AdminUsersActivity
                                )

                            }
                        }
                    }

                    override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                        Log.v("Retrofit", "call failed ${t.message}")
                    }
                })
            }
        }
    }

    private fun prepareRows(
        it: UserResponse,
        tableLayout: TableLayout
    ) {
        val tableRow = TableRow(this@AdminUsersActivity)
        tableRow.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val name = prepareTextView(it.name, 1, this@AdminUsersActivity)
        val email = prepareTextView(it.email, 2, this@AdminUsersActivity)
        val subject = prepareTextView(
            if (!it.subject.isNullOrEmpty()) it.subject else "",
            3,
            this@AdminUsersActivity
        )
        val role = prepareTextView(
            Role.getByName(it.role.name).displayName,
            4,
            this@AdminUsersActivity
        )
        val group = prepareTextView(
            if (it.group != null) it.group.name else "",
            5,
            this@AdminUsersActivity
        )
        val btn = prepareImageView(6, this@AdminUsersActivity)

        tableRow.addView(name)
        tableRow.addView(email)
        tableRow.addView(subject)
        tableRow.addView(role)
        tableRow.addView(group)
        tableRow.addView(btn)

        tableLayout.addView(tableRow)
    }
}