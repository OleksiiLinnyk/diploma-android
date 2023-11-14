package com.demo.diploma.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.demo.diploma.MainActivity
import com.demo.diploma.R
import com.demo.diploma.activity.student.StudentTestActivity
import com.demo.diploma.api.AuthAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.model.response.MessageResponse
import retrofit2.Response

class StudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.students_home_layout)

        val cardView: CardView = findViewById(R.id.logout)
        val testCardView: CardView = findViewById(R.id.tests)

        val api: AuthAPI = RetrofitConfiguration.getInstance()
            .create(AuthAPI::class.java)


        cardView.setOnClickListener {
            val response: LiveData<Response<MessageResponse>> = liveData {
                val r = api.signOut()
                emit(r)
            }
            response.observe(this) {
                val messageResponse = it.body()
                if (messageResponse != null) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        testCardView.setOnClickListener {
            val intent = Intent(applicationContext, StudentTestActivity::class.java)
            startActivity(intent)
        }
    }
}