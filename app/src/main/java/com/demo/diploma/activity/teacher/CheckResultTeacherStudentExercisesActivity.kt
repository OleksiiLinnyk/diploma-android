package com.demo.diploma.activity.teacher

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.StudentActivity
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.fragment.EstimateExerciseFragment
import com.demo.diploma.fragment.PopUpCreateGroupFragment
import com.demo.diploma.model.ExerciseType
import com.demo.diploma.model.OpenExercise
import com.demo.diploma.model.TestExercise
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ViewUtil
import com.demo.diploma.viewmodel.CheckResultTeacherStudentExercisesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckResultTeacherStudentExercisesActivity : AppCompatActivity() {

    private lateinit var checkResultTeacherStudentExercisesViewModel: CheckResultTeacherStudentExercisesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_result_users_exercises_teacher_layout)
        checkResultTeacherStudentExercisesViewModel = ViewModelProvider(this)[CheckResultTeacherStudentExercisesViewModel::class.java]
        val exerciseLayout: LinearLayout = findViewById(R.id.exercises_content)
        val backBtn: ImageView = findViewById(R.id.back_to_btn)
        val studentId = intent.getLongExtra("studentId", 0)
        val testId = intent.getLongExtra("testId", 0)
        val groupId = intent.getLongExtra("groupId", 0)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        BottomNavigationUtil.setBottomNavigationMenu(
            this@CheckResultTeacherStudentExercisesActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )

        GlobalScope.launch(Dispatchers.Main) {
            checkResultTeacherStudentExercisesViewModel.getStudentExerciseByTestIdAndStudentId(testId, studentId)
                .observe(this@CheckResultTeacherStudentExercisesActivity) {
                    it.forEachIndexed { index, exerciseRes ->
                        Log.i("Exercise", exerciseRes.toString())
                        if (exerciseRes.exercise is TestExercise) {
                            val exercise: TestExercise = exerciseRes.exercise
                            val questionText: TextView = ViewUtil.prepareTextView(
                                "${index + 1}. ${exercise.question} - points(${exerciseRes.takenPoints}/${exercise.points})",
                                this@CheckResultTeacherStudentExercisesActivity
                            )
                            exerciseLayout.addView(questionText)
                            exercise.options.forEachIndexed { optionIndex, option ->
                                val checkBox = CheckBox(this@CheckResultTeacherStudentExercisesActivity)
                                checkBox.layoutParams = ViewGroup.LayoutParams(
                                    TableLayout.LayoutParams.WRAP_CONTENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT
                                )
                                checkBox.text = option
                                checkBox.gravity = Gravity.CENTER
                                checkBox.isEnabled = false
                                checkBox.setTextColor(ContextCompat.getColor(this@CheckResultTeacherStudentExercisesActivity, R.color.black))
                                if (!exercise.givenAnswerIndexes.isNullOrEmpty()) {
                                    checkBox.isChecked = exercise.givenAnswerIndexes.contains(optionIndex)
                                }
                                exerciseLayout.addView(checkBox)
                            }
                        } else if (exerciseRes.exercise is OpenExercise) {
                            val exercise: OpenExercise = exerciseRes.exercise
                            val questionText: TextView = ViewUtil.prepareTextView(
                                "${index + 1}. ${exercise.question} - points(${exerciseRes.takenPoints}/${exercise.points})",
                                this@CheckResultTeacherStudentExercisesActivity
                            )
                            exerciseLayout.addView(questionText)
                            val givenAnswer = TextView(this@CheckResultTeacherStudentExercisesActivity)
                            givenAnswer.layoutParams = ViewGroup.LayoutParams(
                                TableLayout.LayoutParams.WRAP_CONTENT,
                                TableLayout.LayoutParams.WRAP_CONTENT
                            )
                            givenAnswer.setTextColor(
                                ContextCompat.getColor(
                                    this@CheckResultTeacherStudentExercisesActivity,
                                    R.color.textColorForAnswer
                                )
                            )
                            givenAnswer.setTypeface(givenAnswer.typeface, Typeface.ITALIC)
                            givenAnswer.text = if (exercise.givenAnswer.isNullOrEmpty()) "Not answered" else exercise.givenAnswer
                            givenAnswer.setPadding(30, 0, 0, 0)
                            exerciseLayout.addView(givenAnswer)
                            if (exercise.type == ExerciseType.LONG_OPEN_ANSWER_EXERCISE && !exercise.givenAnswer.isNullOrEmpty() && exerciseRes.takenPoints == 0) {
                                val btn = Button(this@CheckResultTeacherStudentExercisesActivity)
                                val params = TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    130
                                )
                                params.setMargins(40, 40, 0, 40)
                                btn.layoutParams = params
                                btn.setBackgroundColor(ContextCompat.getColor(this@CheckResultTeacherStudentExercisesActivity, R.color.myButtonColor))
                                btn.text = "Add estimation"
                                btn.textSize = 10F
                                exerciseLayout.addView(btn)

                                btn.setOnClickListener {
                                    val showPopUp = EstimateExerciseFragment()
                                    val bundle = Bundle()
                                    bundle.putLong("exerciseId", exerciseRes.id)
                                    bundle.putLong("testId", testId)
                                    bundle.putLong("groupId", groupId)
                                    bundle.putLong("studentId", studentId)
                                    bundle.putInt("maxPoints", exercise.points)
                                    showPopUp.arguments = bundle
                                    showPopUp.show(this@CheckResultTeacherStudentExercisesActivity.supportFragmentManager, "showPopUp")
                                }
                            }
                        }
                    }
                }
        }

        backBtn.setOnClickListener {
            val intent = Intent(applicationContext, CheckResultTeacherUsersActivity::class.java)
            intent.putExtra("testId", testId)
            intent.putExtra("groupId", groupId)
            startActivity(intent)
        }
    }
}