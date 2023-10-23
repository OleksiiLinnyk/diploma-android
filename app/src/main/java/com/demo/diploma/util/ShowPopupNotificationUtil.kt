package com.demo.diploma.util

import android.content.Context
import android.widget.Toast

object ShowPopupNotificationUtil {

    fun showPopup(text: String, context: Context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}