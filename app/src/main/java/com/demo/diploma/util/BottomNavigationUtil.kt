package com.demo.diploma.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.demo.diploma.MainActivity
import com.demo.diploma.R
import com.demo.diploma.activity.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationUtil {

    fun setBottomNavigationMenu(applicationContext: Context, bottomNavigationView: BottomNavigationView, home: Class<*>) {
        bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home_menu -> {
                    val intent = Intent(applicationContext, home)
                    (applicationContext as Activity).startActivity(intent)
                }
                R.id.logout_menu -> {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    (applicationContext as Activity).startActivity(intent)
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