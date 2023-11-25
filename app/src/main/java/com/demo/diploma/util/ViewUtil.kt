package com.demo.diploma.util

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout.LayoutParams
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.demo.diploma.R

object ViewUtil {

    fun prepareTextView(textField: String, layoutColumn: Int, context: Context, isEnableBackground: Boolean = true): TextView {
        val textView = TextView(context)
        textView.text = textField
        textView.gravity = Gravity.CENTER
        textView.setPadding(20)
        if (isEnableBackground) {
            textView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.tableBackgroundRow
                )
            )
        }
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        val params = TableRow.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.column = layoutColumn
        textView.layoutParams = params
        return textView
    }

    fun prepareTextView(textField: String,context: Context): TextView {
        val textView = TextView(context)
        textView.text = textField
        textView.gravity = Gravity.CENTER
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        val params = TableRow.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = params
        return textView
    }

    fun prepareTextView(context: Context): EditText {
        val textView = EditText(context)
        textView.hint = "Type your answer"
        textView.gravity = Gravity.CENTER
        textView.setPadding(20)
        textView.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.black
            )
        )
        textView.layoutParams = ViewGroup.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
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

    fun prepareCheckBoxView(context: Context): CheckBox {
        val checkBox = CheckBox(context)
        checkBox.gravity = Gravity.CENTER
        checkBox.layoutParams = ViewGroup.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        return checkBox
    }

    fun prepareImageView(
        layoutColumn: Int,
        context: Context,
        setBackground: Boolean = true,
        drawable: Int = R.drawable.ic_trash
    ): ImageView {
        val imageView = ImageView(context)
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                drawable
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