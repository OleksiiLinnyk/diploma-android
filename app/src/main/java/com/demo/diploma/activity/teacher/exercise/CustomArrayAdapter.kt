package com.demo.diploma.activity.teacher.exercise

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes

class CustomArrayAdapter(context: Context, @LayoutRes val layoutResource: Int, items: List<String>) : ArrayAdapter<String>(context, layoutResource, items) {

    override fun isEnabled(position: Int): Boolean {
        return false
    }
}