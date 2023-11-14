package com.demo.diploma.activity.student

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.callback.LoadTestCallback
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.usecase.student.GetStudentTestUseCase
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class StudentTestActivity : AppCompatActivity() {

    private val getStudentTestUseCase: GetStudentTestUseCase = GetStudentTestUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_test_layout)

        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(
            this@StudentTestActivity,
            bottomNavigationView,
            StudentActivity::class.java
        )

        getStudentTestUseCase.execute(object : LoadTestCallback {
            override fun onSuccess(body: List<TestResponse>) {
                tableLayout.removeAllViews()
                Log.i("BODY", body.toString())
                body.forEach {
                    val tableRow: TableRow = prepareRows(it)
                    tableLayout.addView(tableRow)
                }
            }
        })
    }

    private fun prepareRows(
        it: TestResponse
    ): TableRow {
        val tableRow = TableRow(this@StudentTestActivity)
        tableRow.layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        val id = ViewUtil.prepareTextView(it.id.toString(), 1, this@StudentTestActivity)
        val theme = ViewUtil.prepareTextView(
            if (!it.theme.isNullOrEmpty()) it.theme else "",
            3,
            this@StudentTestActivity
        )
        val subject = ViewUtil.prepareTextView(
            if (!it.subject.isNullOrEmpty()) it.subject else "",
            2,
            this@StudentTestActivity
        )

        tableRow.addView(id, 0)
        tableRow.addView(subject, 1)
        tableRow.addView(theme, 2)
        return tableRow
    }
}