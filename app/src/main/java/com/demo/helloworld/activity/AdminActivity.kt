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

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_home_layout)

        val cardView: CardView = findViewById(R.id.logout)
        val groupCardView: CardView = findViewById(R.id.groups)
        val usersCardView: CardView = findViewById(R.id.users)
        val settingCardView: CardView = findViewById(R.id.settings_card)

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

        groupCardView.setOnClickListener {
            val intent = Intent(applicationContext, AdminGroupsActivity::class.java)
            startActivity(intent)
        }

        usersCardView.setOnClickListener {
            val intent = Intent(applicationContext, AdminUsersActivity::class.java)
            startActivity(intent)
        }

        settingCardView.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra("class", this::class.java.canonicalName)
            startActivity(intent)
        }
    }
}