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
import com.demo.diploma.activity.teacher.CheckResultTeacherTestActivity
import com.demo.diploma.activity.teacher.CreateExerciseActivity
import com.demo.diploma.activity.teacher.TeacherTestActivity
import com.demo.diploma.api.AuthAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.MessageResponse
import retrofit2.Response

class TeacherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_home_layout)

        val cardView: CardView = findViewById(R.id.logout)
        val testsView: CardView = findViewById(R.id.teacher_tests)
        val settingCardView: CardView = findViewById(R.id.settings_card)
        val checkTestCard: CardView = findViewById(R.id.check_test_card)

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

        testsView.setOnClickListener {
            val intent = Intent(applicationContext, TeacherTestActivity::class.java)
            startActivity(intent)
        }

        checkTestCard.setOnClickListener {
            val intent = Intent(applicationContext, CheckResultTeacherTestActivity::class.java)
            startActivity(intent)
        }

        settingCardView.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra("class", this::class.java.canonicalName)
            startActivity(intent)
        }
    }
}