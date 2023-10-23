package com.demo.diploma.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.demo.diploma.R
import com.demo.diploma.fragment.PopUpUpdateUserFragment
import com.demo.diploma.util.BottomNavigationUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        val className = intent.extras?.getString("class")
        val clazz: Class<*>? = className?.let { Class.forName(it) }

        val updateProfileBtn: Button = findViewById(R.id.update_profile_btn)

        updateProfileBtn.setOnClickListener {
            val showPopUp = PopUpUpdateUserFragment()
            showPopUp.show(this@SettingsActivity.supportFragmentManager, "showPopUp")
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        if (clazz != null) {
            BottomNavigationUtil.setBottomNavigationMenu(
                this@SettingsActivity,
                bottomNavigationView,
                clazz
            )
        }
    }
}