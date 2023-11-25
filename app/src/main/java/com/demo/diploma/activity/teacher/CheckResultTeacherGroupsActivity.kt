package com.demo.diploma.activity.teacher

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.model.response.GroupsProgressResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil
import com.demo.diploma.viewmodel.CheckResultTeacherGroupsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckResultTeacherGroupsActivity : AppCompatActivity() {

    private lateinit var checkResultTeacherGroupsViewModel: CheckResultTeacherGroupsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_result_test_groups_teacher_layout)
        checkResultTeacherGroupsViewModel = ViewModelProvider(this)[CheckResultTeacherGroupsViewModel::class.java]
        val testId: Long = intent.getLongExtra("testId", 0)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val backToBtn: ImageView = findViewById(R.id.back_to_btn)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        BottomNavigationUtil.setBottomNavigationMenu(
            this@CheckResultTeacherGroupsActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )

        GlobalScope.launch(Dispatchers.Main) {
            tableLayout.removeAllViews()
            checkResultTeacherGroupsViewModel.getGroupsResultByTestId(testId).observe(this@CheckResultTeacherGroupsActivity) { res ->
                res.forEach { group ->
                    val row = prepareTableRows(group)
                    tableLayout.addView(row)
                    row.setOnClickListener {
                        val intent = Intent(applicationContext, CheckResultTeacherUsersActivity::class.java)
                        intent.putExtra("testId", testId)
                        intent.putExtra("groupId", group.groupId)
                        startActivity(intent)
                    }
                }
            }
        }

        backToBtn.setOnClickListener {
            val intent = Intent(this@CheckResultTeacherGroupsActivity, CheckResultTeacherTestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun prepareTableRows(groupsProgressResponse: GroupsProgressResponse): TableRow {
        val tableRow = TableRow(this)
        tableRow.layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        val groupName: TextView = ViewUtil.prepareTextView(groupsProgressResponse.groupName, 0, this@CheckResultTeacherGroupsActivity)
        val totalStudents: TextView = ViewUtil.prepareTextView(groupsProgressResponse.totalUsers.toString(), 1, this@CheckResultTeacherGroupsActivity)
        val awaiting: TextView = ViewUtil.prepareTextView(groupsProgressResponse.awaiting.toString(), 2, this@CheckResultTeacherGroupsActivity)
        val toCheck: TextView = ViewUtil.prepareTextView(groupsProgressResponse.toCheck.toString(), 3, this@CheckResultTeacherGroupsActivity)
        val done: TextView = ViewUtil.prepareTextView(groupsProgressResponse.done.toString(), 4, this@CheckResultTeacherGroupsActivity)


        tableRow.addView(groupName, 0)
        tableRow.addView(totalStudents, 1)
        tableRow.addView(awaiting, 2)
        tableRow.addView(toCheck, 3)
        tableRow.addView(done, 4)
        return tableRow
    }
}