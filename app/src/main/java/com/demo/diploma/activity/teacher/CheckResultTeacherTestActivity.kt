package com.demo.diploma.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ShowPopupNotificationUtil
import com.demo.diploma.util.ViewUtil
import com.demo.diploma.viewmodel.CheckResultTeacherTestViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckResultTeacherTestActivity : AppCompatActivity() {

    private lateinit var checkResultTeacherTestViewModel: CheckResultTeacherTestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_result_test_teacher_layout)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        BottomNavigationUtil.setBottomNavigationMenu(
            this@CheckResultTeacherTestActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )
        checkResultTeacherTestViewModel = ViewModelProvider(this)[CheckResultTeacherTestViewModel::class.java]

        GlobalScope.launch(Dispatchers.Main) {
            tableLayout.removeAllViews()
            checkResultTeacherTestViewModel.getTeacherTests().observe(this@CheckResultTeacherTestActivity) {
                it.forEach { testResponse ->
                    val tableRow: TableRow = prepareRows(testResponse)
                    tableLayout.addView(tableRow)
                    tableRow.setOnClickListener {
                        if (testResponse.enabled) {
                            val intent = Intent(this@CheckResultTeacherTestActivity, CheckResultTeacherGroupsActivity::class.java)
                            intent.putExtra("testId", testResponse.id)
                            startActivity(intent)
                        } else {
                            ShowPopupNotificationUtil.showPopup("Test is not published", this@CheckResultTeacherTestActivity)
                        }
                    }
                }
            }
        }
    }

    private fun prepareRows(testResponse: TestResponse): TableRow {
        val tableRow = TableRow(this@CheckResultTeacherTestActivity)
        val tableRowParams = TableLayout.LayoutParams(200, TableLayout.LayoutParams.WRAP_CONTENT)
        tableRowParams.setMargins(0, 10, 0, 10)
        tableRow.layoutParams = tableRowParams

        val theme = ViewUtil.prepareTextView(
            if (!testResponse.theme.isNullOrEmpty()) testResponse.theme else "",
            0,
            this@CheckResultTeacherTestActivity,
            false
        )
        val subject = ViewUtil.prepareTextView(
            if (!testResponse.subject.isNullOrEmpty()) testResponse.subject else "",
            1,
            this@CheckResultTeacherTestActivity,
            false
        )

        if (testResponse.enabled) {
            tableRow.setBackgroundResource(R.drawable.corners_for_active_test_table)

        } else {
            tableRow.setBackgroundResource(R.drawable.corners_for_in_active_test_table)
        }

        tableRow.addView(subject, 0)
        tableRow.addView(theme, 1)
        return tableRow
    }
}