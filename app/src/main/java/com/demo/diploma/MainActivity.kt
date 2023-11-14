package com.demo.diploma

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.demo.diploma.activity.AdminActivity
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.api.AuthAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.request.LoginRequest
import com.demo.diploma.model.response.LoginResponse
import com.demo.diploma.model.Role.ADMIN
import com.demo.diploma.model.Role.Companion.getByName
import com.demo.diploma.model.Role.STUDENT
import com.demo.diploma.model.Role.TEACHER
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.button)

        val api: AuthAPI = RetrofitConfiguration.getInstance()
            .create(AuthAPI::class.java)

        button.setOnClickListener {
            val inputTextEmail: String = username.text.toString()
            val inputTextPassword: String = password.text.toString()

            val response: LiveData<Response<LoginResponse>> = liveData {
                val r = api.signIn(LoginRequest(inputTextEmail, inputTextPassword))
                emit(r)
            }

            response.observe(this, Observer {
                val loginResponse = it.body()
                if (it.code() != 200) {
                    showPopup("Wrong password or email")
                }
                if (loginResponse != null) {
                    login(loginResponse)
                    val cookieList: List<String> = it.headers().values("Set-Cookie")
                    TokenHolder.saveToken(cookieList[0].split("=")[1])
                }
            })
        }

    }

    private fun login(loginResponse: LoginResponse) {
        when (getByName(loginResponse.role)) {
            TEACHER -> {
                showPopup("Hi teacher, ${loginResponse.name}")
                switchActivity(TeacherActivity::class.java)
            }

            STUDENT -> {
                showPopup("Hi student, ${loginResponse.name}")
                switchActivity(StudentActivity::class.java)
            }

            ADMIN -> {
                showPopup("Hi admin, ${loginResponse.name}")
                switchActivity(AdminActivity::class.java)
            }
        }
    }

    private fun switchActivity(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(applicationContext, clazz)
        startActivity(intent)
    }

    private fun showPopup(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}