package com.demo.diploma.activity.teacher

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.model.TestStatus
import com.demo.diploma.model.response.StudentsProgressResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil
import com.demo.diploma.viewmodel.CheckResultTeacherUsersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckResultTeacherUsersActivity : AppCompatActivity() {

    private lateinit var checkResultTeacherUsersViewModel: CheckResultTeacherUsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_result_users_teacher_layout)
        checkResultTeacherUsersViewModel = ViewModelProvider(this)[CheckResultTeacherUsersViewModel::class.java]
        val backBtn: ImageView = findViewById(R.id.back_to_btn)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val testId: Long = intent.getLongExtra("testId", 0)
        val groupId: Long = intent.getLongExtra("groupId", 0)
        val row = TableRow(this)
        val nameHeader: TextView = ViewUtil.prepareTextView("Name", 0, this@CheckResultTeacherUsersActivity, false)
        nameHeader.typeface = Typeface.DEFAULT_BOLD
        val statusHeader = ViewUtil.prepareTextView("Status", 1, this@CheckResultTeacherUsersActivity, false)
        statusHeader.typeface = Typeface.DEFAULT_BOLD
        val takenPointsHeader = ViewUtil.prepareTextView("Taken points", 2, this@CheckResultTeacherUsersActivity, false)
        takenPointsHeader.typeface = Typeface.DEFAULT_BOLD
        val totalPointsHeader = ViewUtil.prepareTextView("Total points", 3, this@CheckResultTeacherUsersActivity, false)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        BottomNavigationUtil.setBottomNavigationMenu(
            this@CheckResultTeacherUsersActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )
        totalPointsHeader.typeface = Typeface.DEFAULT_BOLD
        tableLayout.removeAllViews()
        row.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundTableColor))
        row.addView(nameHeader, 0)
        row.addView(statusHeader, 1)
        row.addView(takenPointsHeader, 2)
        row.addView(totalPointsHeader, 3)
        tableLayout.addView(row)

        GlobalScope.launch(Dispatchers.Main) {
            checkResultTeacherUsersViewModel.getUserResultsByTestAndGroupId(testId, groupId).observe(this@CheckResultTeacherUsersActivity) {
                it.forEach { studentResp ->
                    val tableRow = prepareTableRows(studentResp)
                    tableLayout.addView(tableRow)
                    tableRow.setOnClickListener {
                        val intent = Intent(applicationContext, CheckResultTeacherStudentExercisesActivity::class.java)
                        intent.putExtra("testId", testId)
                        intent.putExtra("groupId", groupId)
                        intent.putExtra("studentId", studentResp.id)
                        startActivity(intent)
                    }
                }
            }
        }

        backBtn.setOnClickListener {
            val intent = Intent(applicationContext, CheckResultTeacherGroupsActivity::class.java)
            intent.putExtra("testId", testId)
            startActivity(intent)
        }
    }

    private fun prepareTableRows(studentsProgressResponse: StudentsProgressResponse): TableRow {
        val tableRow = TableRow(this)
        val name: TextView = ViewUtil.prepareTextView(studentsProgressResponse.name, 0, this@CheckResultTeacherUsersActivity)
        val status: TextView =
            ViewUtil.prepareTextView(TestStatus.getByValue(studentsProgressResponse.status).displayName, 1, this@CheckResultTeacherUsersActivity)
        val takenPoints: TextView = ViewUtil.prepareTextView(studentsProgressResponse.takenPoints.toString(), 2, this@CheckResultTeacherUsersActivity)
        val totalPoints: TextView = ViewUtil.prepareTextView(studentsProgressResponse.totalPoints.toString(), 3, this@CheckResultTeacherUsersActivity)


        tableRow.addView(name, 0)
        tableRow.addView(status, 1)
        tableRow.addView(takenPoints, 2)
        tableRow.addView(totalPoints, 3)
        return tableRow
    }
}