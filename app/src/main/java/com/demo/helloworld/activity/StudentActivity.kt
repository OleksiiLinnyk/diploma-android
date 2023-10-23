package com.demo.helloworld.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.demo.helloworld.MainActivity
import com.demo.helloworld.R
import com.demo.helloworld.api.AuthAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.model.response.MessageResponse
import retrofit2.Response

class StudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.students_home_layout)

        val cardView: CardView = findViewById(R.id.logout)

        val api: AuthAPI = RetrofitConfiguration.getInstance()
            .create(AuthAPI::class.java)

        cardView.setOnClickListener {
            val response: LiveData<Response<MessageResponse>> = liveData {
                val r = api.signOut()
                emit(r)
            }
            response.observe(this, Observer {
                val messageResponse = it.body()
                if (messageResponse != null) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }
}