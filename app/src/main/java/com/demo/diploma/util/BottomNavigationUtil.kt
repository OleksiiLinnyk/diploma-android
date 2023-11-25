package com.demo.diploma.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.demo.diploma.MainActivity
import com.demo.diploma.R
import com.demo.diploma.activity.SettingsActivity
import com.demo.diploma.api.AuthAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.model.response.MessageResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Response

object BottomNavigationUtil {

    val api: AuthAPI = RetrofitConfiguration.getInstance()
        .create(AuthAPI::class.java)
    fun setBottomNavigationMenu(
        applicationContext: Context,
        bottomNavigationView: BottomNavigationView,
        home: Class<*>
    ) {
        bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home_menu -> {
                    val intent = Intent(applicationContext, home)
                    (applicationContext as Activity).startActivity(intent)
                }

                R.id.logout_menu -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    val response: LiveData<Response<MessageResponse>> = liveData {
                        val r = api.signOut()
                        emit(r)
                    }
                    response.observe(applicationContext as AppCompatActivity) { res ->
                        val messageResponse = res.body()
                        if (messageResponse != null) {
                            applicationContext.startActivity(intent)
                        }
                    }
                }

                R.id.settings_menu -> {
                    val intent = Intent(applicationContext, SettingsActivity::class.java)
                    intent.putExtra("class", home.canonicalName)
                    (applicationContext as Activity).startActivity(intent)
                }
            }
        }
    }
}