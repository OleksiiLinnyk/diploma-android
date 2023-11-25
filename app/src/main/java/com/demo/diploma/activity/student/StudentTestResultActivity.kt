package com.demo.diploma.activity.student

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.model.TestStatus
import com.demo.diploma.model.response.StudentTestResultResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil.prepareTextView
import com.demo.diploma.viewmodel.StudentTestResultViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentTestResultActivity : AppCompatActivity() {

    private lateinit var studentTestResultViewModel: StudentTestResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_test_result_layout)
        studentTestResultViewModel = ViewModelProvider(this)[StudentTestResultViewModel::class.java]
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        tableLayout.removeAllViews()
        val tableRow = TableRow(this)
        val subjectTopView: TextView = prepareTextView("Subject", 0, this@StudentTestResultActivity, false)
        subjectTopView.typeface = Typeface.DEFAULT_BOLD
        val themeHeader = prepareTextView("Theme", 1, this@StudentTestResultActivity, false)
        themeHeader.typeface = Typeface.DEFAULT_BOLD
        val statusHeader = prepareTextView("Status", 2, this@StudentTestResultActivity, false)
        statusHeader.typeface = Typeface.DEFAULT_BOLD
        val pointsHeader = prepareTextView("Points", 3, this@StudentTestResultActivity, false)
        pointsHeader.typeface = Typeface.DEFAULT_BOLD

        tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundTableColor))
        tableRow.addView(subjectTopView, 0)
        tableRow.addView(themeHeader, 1)
        tableRow.addView(statusHeader, 2)
        tableRow.addView(pointsHeader, 3)
        tableLayout.addView(tableRow)

        GlobalScope.launch(Dispatchers.Main) {
            studentTestResultViewModel.getStudentTestResult().observe(this@StudentTestResultActivity) {
                it.forEach { studentTestResult ->
                    val tableRow = prepareRow(studentTestResult)
                    tableLayout.addView(tableRow)
                }
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        BottomNavigationUtil.setBottomNavigationMenu(
            this@StudentTestResultActivity,
            bottomNavigationView,
            StudentActivity::class.java
        )
    }

    private fun prepareRow(studentTestResult: StudentTestResultResponse): TableRow {
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableRow.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

        val subject: TextView = prepareTextView(studentTestResult.subject, 0, this)
        val theme: TextView = prepareTextView(studentTestResult.theme, 1, this)
        val status: TextView = prepareTextView(TestStatus.getByValue(studentTestResult.status).displayName, 2, this)
        val points: TextView = prepareTextView("${studentTestResult.takenPoints}/${studentTestResult.totalPoints}", 3, this)

        tableRow.addView(subject, 0)
        tableRow.addView(theme, 1)
        tableRow.addView(status, 2)
        tableRow.addView(points, 3)
        return tableRow
    }
}