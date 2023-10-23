package com.demo.diploma.util

import android.content.Context
import android.view.Gravity
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.demo.diploma.R

object ViewUtil {

    fun prepareTextView(textField: String, layoutColumn: Int, context: Context): TextView {
        val textView = TextView(context)
        textView.text = textField
        textView.gravity = Gravity.CENTER
        textView.setPadding(20)
        textView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.tableBackgroundRow
            )
        )
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        textView.layoutParams = TableRow.LayoutParams(layoutColumn)
        return textView
    }

    fun prepareCheckBoxView(enabled: Boolean, layoutColumn: Int, context: Context): CheckBox {
        val checkBox = CheckBox(context)
        checkBox.isChecked = enabled
        checkBox.gravity = Gravity.CENTER
        checkBox.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.tableBackgroundRow
            )
        )
        checkBox.layoutParams = TableRow.LayoutParams(layoutColumn)
        checkBox.width = 106
        checkBox.height = 106
        return checkBox
    }

    fun prepareImageView(
        layoutColumn: Int,
        context: Context,
        setBackground: Boolean = true
    ): ImageView {
        val imageView = ImageView(context)
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_trash
            )
        )
        if (setBackground) {
            imageView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.tableBackgroundRow
                )
            )
        }
        imageView.setPadding(20)
        imageView.layoutParams = TableRow.LayoutParams(layoutColumn)
        return imageView
    }
}