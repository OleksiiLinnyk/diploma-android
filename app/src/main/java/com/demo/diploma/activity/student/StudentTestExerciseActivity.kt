package com.demo.diploma.activity.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.model.OpenExercise
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.TestExercise
import com.demo.diploma.model.request.PassExerciseRequest
import com.demo.diploma.model.response.PassExerciseResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil
import com.demo.diploma.viewmodel.StudentTestViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentTestExerciseActivity : AppCompatActivity() {

    private lateinit var studentTestViewModel: StudentTestViewModel
    private lateinit var passExerciseResponse: List<PassExerciseResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_test_exercises_layout)
        studentTestViewModel = ViewModelProvider(this)[StudentTestViewModel::class.java]

        val exerciseLayout: LinearLayout = findViewById(R.id.exercise_options_layout)
        val questionTextView: TextView = findViewById(R.id.question_placeholder)
        val nextBtn: Button = findViewById(R.id.next_exercise_btn)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        val exerciseDescriptionView: TextView = findViewById(R.id.exercise_description_placeholder)
        val checkBoxList: MutableList<CheckBox> = mutableListOf()
        var editText: EditText? = null

        BottomNavigationUtil.setBottomNavigationMenu(
            this@StudentTestExerciseActivity,
            bottomNavigationView,
            StudentActivity::class.java
        )
        val testId: Long = intent.getLongExtra("testId", 0)
        Log.i("[STUDENT]", "test id = $testId")
        GlobalScope.launch(Dispatchers.Main) {
            studentTestViewModel.getStudentExercisesByTestId(testId)
                .observe(this@StudentTestExerciseActivity) {
                    Log.i("[STUDENT] ", "RESPONSE $it")
                    if (it.isNotEmpty()) {
                        passExerciseResponse = it
                        Log.i("[STUDENT] Exercises to pass", passExerciseResponse.toString())
                        if (passExerciseResponse[0].exercise is TestExercise) {
                            val testExercise: TestExercise =
                                (passExerciseResponse[0].exercise as TestExercise)
                            exerciseDescriptionView.text = testExercise.type?.description
                            questionTextView.text = testExercise.question
                            testExercise.options.forEach { option ->
                                val checkBox: CheckBox =
                                    ViewUtil.prepareCheckBoxView(this@StudentTestExerciseActivity)
                                checkBox.text = option
                                checkBoxList.add(checkBox)
                                exerciseLayout.addView(checkBox)
                            }
                        } else {
                            val openExercise: OpenExercise =
                                (passExerciseResponse[0].exercise as OpenExercise)
                            questionTextView.text = openExercise.question
                            exerciseDescriptionView.text = openExercise.type?.description
                            editText = ViewUtil.prepareTextView(this@StudentTestExerciseActivity)
                            editText!!.id = View.generateViewId()
                            exerciseLayout.addView(editText)
                        }
                    }
                }
        }

        var i = 1
        nextBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                var answer = ""
                if (checkBoxList.isNotEmpty()) {
                    val list = mutableListOf<Int>()
                    for ((index, value) in checkBoxList.withIndex()) {
                        if (value.isChecked) {
                            list.add(index)
                        }
                        answer = list.joinToString(",")
                    }
                    checkBoxList.clear()
                    exerciseLayout.removeAllViews()
                } else if (editText != null) {
                    answer = editText!!.text.toString()
                    editText = null
                    exerciseLayout.removeAllViews()
                }
                studentTestViewModel.passExercise(PassExerciseRequest(passExerciseResponse[i - 1].id, answer))
                    .observe(this@StudentTestExerciseActivity) { status ->
                        if (status == ExerciseOperationsStatus.SUCCESS) {
                            if (i < passExerciseResponse.size) {
                                passExerciseResponse.let {
                                    if (it[i].exercise is TestExercise) {
                                        val testExercise: TestExercise =
                                            (it[i].exercise as TestExercise)
                                        questionTextView.text = testExercise.question
                                        exerciseDescriptionView.text = testExercise.type?.description
                                        testExercise.options.forEach { option ->
                                            val checkBox: CheckBox =
                                                ViewUtil.prepareCheckBoxView(this@StudentTestExerciseActivity)
                                            checkBox.text = option
                                            checkBoxList.add(checkBox)
                                            exerciseLayout.addView(checkBox)
                                        }
                                    } else {
                                        val openExercise: OpenExercise =
                                            (it[i].exercise as OpenExercise)
                                        questionTextView.text = openExercise.question
                                        exerciseDescriptionView.text = openExercise.type?.description
                                        editText =
                                            ViewUtil.prepareTextView(this@StudentTestExerciseActivity)
                                        exerciseLayout.addView(editText)
                                    }

                                    if (i == it.size.minus(1)) {
                                        nextBtn.text = "Finish"
                                    }
                                    i++
                                }
                            } else {
                                val intent = Intent(applicationContext, StudentTestActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
            }
        }
    }
}