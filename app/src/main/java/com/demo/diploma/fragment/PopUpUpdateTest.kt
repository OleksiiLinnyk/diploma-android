package com.demo.diploma.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.demo.diploma.R
import com.demo.diploma.activity.AdminGroupsActivity
import com.demo.diploma.activity.teacher.TestDetailsActivity
import com.demo.diploma.usecase.test.UpdateTestUseCase

class PopUpUpdateTest : DialogFragment() {

    private val updateTestUseCase: UpdateTestUseCase = UpdateTestUseCase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pop_up_update_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subjectInput = view.findViewById<EditText>(R.id.update_subject)
        val themeInput = view.findViewById<EditText>(R.id.update_theme)
        val updateBtn = view.findViewById<Button>(R.id.update_test_btn)

        updateBtn.setOnClickListener {
            val subject = subjectInput.text.toString()
            val theme = themeInput.text.toString()
            if (subject.isNullOrEmpty()) {
                Toast.makeText(view.context, "Please specify the subject", Toast.LENGTH_LONG).show()
            } else if (theme.isNullOrEmpty()) {
                Toast.makeText(view.context, "Please specify the theme", Toast.LENGTH_LONG).show()
            } else {
                val id = arguments?.getLong("id") ?: 0
                updateTestUseCase.updateTest(id, subject, theme, view)
                val intent = Intent(view.context, TestDetailsActivity::class.java)
                intent.putExtra("testId", id)
                startActivity(intent)
            }
        }
    }
}