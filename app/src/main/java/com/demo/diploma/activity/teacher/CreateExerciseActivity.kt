package com.demo.diploma.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.demo.diploma.R
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.api.ExerciseAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.ExerciseType
import com.demo.diploma.model.TestOptionsDTO
import com.demo.diploma.model.request.ExerciseRequest
import com.demo.diploma.model.request.ExerciseTypedRequest
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ShowPopupNotificationUtil
import com.demo.diploma.util.ViewUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_exercise_layout)
        val pointsPicker: EditText = findViewById(R.id.pointsPicker)
        val layout: RelativeLayout = findViewById(R.id.exercise_type_place_layout)
        val question: EditText = findViewById(R.id.exerciseQuestion)
        val createBtn: Button = findViewById(R.id.create_exercise)
        val exerciseAPI: ExerciseAPI = RetrofitConfiguration.getInstance()
            .create(ExerciseAPI::class.java)

        val exerciseView: AutoCompleteTextView = findViewById(R.id.exerciseView)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(
            this@CreateExerciseActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.exercise_types,
            ExerciseType.values().map { it.displayName }.toList()
        )
        exerciseView.setAdapter(arrayAdapter)

        val testOptionsDTOS: MutableList<TestOptionsDTO> = mutableListOf()
        var answer = ""
        exerciseView.setOnItemClickListener { adapterView, view, i, l ->
            when (adapterView.getItemAtPosition(i) as String) {
                ExerciseType.SINGLE_ANSWER_EXERCISE.displayName -> {
                    layout.removeAllViews()
                    val color = ContextCompat.getDrawable(
                        this@CreateExerciseActivity,
                        R.color.myButtonColor
                    )
                    val color1 = ContextCompat.getColor(
                        this@CreateExerciseActivity,
                        R.color.tableBackgroundRow
                    )
                    val answerPlaceHolder = TextView(this@CreateExerciseActivity)
                    val answerTextParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerPlaceHolder.id = R.id.answer_exercise_text_id
                    answerPlaceHolder.text = "Answer :"
                    answerPlaceHolder.textSize = 20F
                    answerPlaceHolder.setPadding(20)
                    answerPlaceHolder.setTextColor(color1)
                    answerTextParams.setMargins(10)

                    val addOptionButton = Button(this@CreateExerciseActivity)
                    val buttonParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    addOptionButton.id = R.id.add_option_exercise_btn_id
                    addOptionButton.text = "Add option"
                    addOptionButton.background = color
                    addOptionButton.setPadding(20)
                    addOptionButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_create,
                        0,
                        0,
                        0
                    )
                    buttonParams.setMargins(10)
                    buttonParams.addRule(RelativeLayout.BELOW, answerPlaceHolder.id)

                    val rightAnswerPlace = TextView(this@CreateExerciseActivity)
                    val rightAnswerParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    rightAnswerPlace.id = R.id.right_answer_text_id
                    rightAnswerPlace.text = "Right answer"
                    rightAnswerPlace.textSize = 20F
                    rightAnswerPlace.setPadding(20)
                    rightAnswerPlace.setTextColor(color1)
                    rightAnswerParams.setMargins(10)
                    rightAnswerParams.addRule(RelativeLayout.BELOW, addOptionButton.id)

                    val answerOptionsPlace = TextView(this@CreateExerciseActivity)
                    val answerOptionsParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerOptionsPlace.id = R.id.answer_options_text_id
                    answerOptionsPlace.text = "Answer options"
                    answerOptionsPlace.textSize = 20F
                    answerOptionsPlace.setPadding(20)
                    answerOptionsPlace.setTextColor(color1)
                    answerOptionsParams.addRule(RelativeLayout.BELOW, addOptionButton.id)
                    answerOptionsParams.addRule(RelativeLayout.RIGHT_OF, rightAnswerPlace.id)
                    answerOptionsParams.setMargins(150, 10, 10, 10)

                    val tableLayout = TableLayout(this@CreateExerciseActivity)
                    val tableLayoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.setMargins(20, 0, 20, 0)
                    }
                    tableLayoutParams.addRule(RelativeLayout.BELOW, answerOptionsPlace.id)


                    layout.addView(answerPlaceHolder, answerTextParams)
                    layout.addView(addOptionButton, buttonParams)
                    layout.addView(rightAnswerPlace, rightAnswerParams)
                    layout.addView(answerOptionsPlace, answerOptionsParams)
                    layout.addView(tableLayout, tableLayoutParams)


                    //add option btn actions
                    addOptionButton.setOnClickListener {
                        val tableRow = TableRow(this@CreateExerciseActivity)
                        tableRow.layoutParams =
                            TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )

                        val checkBox = CheckBox(this@CreateExerciseActivity)
                        checkBox.id = View.generateViewId()
                        checkBox.setPadding(20)
                        checkBox.layoutParams = TableRow.LayoutParams(1).also {
                            it.setMargins(50, 10, 10, 10)
                        }

                        checkBox.setOnClickListener {
                            val checkboxValue = it as CheckBox
                            if (checkboxValue.isChecked) {
                                val checkedCheckBox =
                                    testOptionsDTOS.filter { cb -> cb.active?.id != checkboxValue.id }
                                        .firstOrNull { cb -> cb.active?.isChecked ?: false }
                                if (checkedCheckBox != null) {
                                    checkedCheckBox.active?.isChecked = false
                                }
                            }
                        }


                        val inputOptionField = EditText(this@CreateExerciseActivity)
                        inputOptionField.setPadding(20)
                        inputOptionField.setTextColor(color1)
                        inputOptionField.hint = "Type option"
                        inputOptionField.setHintTextColor(color1)
                        inputOptionField.layoutParams = TableRow.LayoutParams(
                            600,
                            TableRow.LayoutParams.WRAP_CONTENT
                        ).also {
                            it.setMargins(450, 10, 10, 10)
                            it.column = 2
                        }

                        val answerModel = TestOptionsDTO(checkBox, inputOptionField)

                        testOptionsDTOS.add(answerModel)
                        val imageView: ImageView =
                            ViewUtil.prepareImageView(3, this@CreateExerciseActivity, false)


                        tableRow.addView(checkBox)
                        tableRow.addView(inputOptionField)
                        tableRow.addView(imageView)
                        tableRow.setBackgroundResource(R.drawable.row_border)
                        tableLayout.addView(tableRow)

                        imageView.setOnClickListener {
                            testOptionsDTOS.remove(answerModel)
                            tableLayout.removeView(tableRow)
                        }
                    }
                }

                ExerciseType.MULTIPLE_ANSWER_EXERCISE.displayName -> {
                    layout.removeAllViews()
                    val color = ContextCompat.getDrawable(
                        this@CreateExerciseActivity,
                        R.color.myButtonColor
                    )
                    val color1 = ContextCompat.getColor(
                        this@CreateExerciseActivity,
                        R.color.tableBackgroundRow
                    )
                    val answerPlaceHolder = TextView(this@CreateExerciseActivity)
                    val answerTextParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerPlaceHolder.id = R.id.answer_exercise_text_id
                    answerPlaceHolder.text = "Answer :"
                    answerPlaceHolder.textSize = 20F
                    answerPlaceHolder.setPadding(20)
                    answerPlaceHolder.setTextColor(color1)
                    answerTextParams.setMargins(10)

                    val addOptionButton = Button(this@CreateExerciseActivity)
                    val buttonParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    addOptionButton.id = R.id.add_option_exercise_btn_id
                    addOptionButton.text = "Add option"
                    addOptionButton.background = color
                    addOptionButton.setPadding(20)
                    addOptionButton.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_create,
                        0,
                        0,
                        0
                    )
                    buttonParams.setMargins(10)
                    buttonParams.addRule(RelativeLayout.BELOW, answerPlaceHolder.id)

                    val rightAnswerPlace = TextView(this@CreateExerciseActivity)
                    val rightAnswerParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    rightAnswerPlace.id = R.id.right_answer_text_id
                    rightAnswerPlace.text = "Right answer"
                    rightAnswerPlace.textSize = 20F
                    rightAnswerPlace.setPadding(20)
                    rightAnswerPlace.setTextColor(color1)
                    rightAnswerParams.setMargins(10)
                    rightAnswerParams.addRule(RelativeLayout.BELOW, addOptionButton.id)

                    val answerOptionsPlace = TextView(this@CreateExerciseActivity)
                    val answerOptionsParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerOptionsPlace.id = R.id.answer_options_text_id
                    answerOptionsPlace.text = "Answer options"
                    answerOptionsPlace.textSize = 20F
                    answerOptionsPlace.setPadding(20)
                    answerOptionsPlace.setTextColor(color1)
                    answerOptionsParams.addRule(RelativeLayout.BELOW, addOptionButton.id)
                    answerOptionsParams.addRule(RelativeLayout.RIGHT_OF, rightAnswerPlace.id)
                    answerOptionsParams.setMargins(150, 10, 10, 10)

                    val tableLayout = TableLayout(this@CreateExerciseActivity)
                    val tableLayoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.setMargins(20, 0, 20, 0)
                    }
                    tableLayoutParams.addRule(RelativeLayout.BELOW, answerOptionsPlace.id)


                    layout.addView(answerPlaceHolder, answerTextParams)
                    layout.addView(addOptionButton, buttonParams)
                    layout.addView(rightAnswerPlace, rightAnswerParams)
                    layout.addView(answerOptionsPlace, answerOptionsParams)
                    layout.addView(tableLayout, tableLayoutParams)


                    //add option btn actions
                    addOptionButton.setOnClickListener {
                        val tableRow = TableRow(this@CreateExerciseActivity)
                        tableRow.layoutParams =
                            TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )

                        val checkBox = CheckBox(this@CreateExerciseActivity)
                        checkBox.setPadding(20)
                        checkBox.layoutParams = TableRow.LayoutParams(1).also {
                            it.setMargins(50, 10, 10, 10)
                        }

                        val inputOptionField = EditText(this@CreateExerciseActivity)
                        inputOptionField.setPadding(20)
                        inputOptionField.setTextColor(color1)
                        inputOptionField.hint = "Type option"
                        inputOptionField.setHintTextColor(color1)
                        inputOptionField.layoutParams = TableRow.LayoutParams(
                            600,
                            TableRow.LayoutParams.WRAP_CONTENT
                        ).also {
                            it.setMargins(450, 10, 10, 10)
                            it.column = 2
                        }

                        testOptionsDTOS.add(TestOptionsDTO(checkBox, inputOptionField))

                        val imageView: ImageView =
                            ViewUtil.prepareImageView(3, this@CreateExerciseActivity, false)


                        tableRow.addView(checkBox)
                        tableRow.addView(inputOptionField)
                        tableRow.addView(imageView)
                        tableRow.setBackgroundResource(R.drawable.row_border)
                        tableLayout.addView(tableRow)

                        imageView.setOnClickListener {
                            tableLayout.removeView(tableRow)
                        }
                    }
                }

                ExerciseType.SHORT_OPEN_ANSWER_EXERCISE.displayName -> {
                    layout.removeAllViews()
                    val color1 = ContextCompat.getColor(
                        this@CreateExerciseActivity,
                        R.color.tableBackgroundRow
                    )
                    val answerPlaceHolder = TextView(this@CreateExerciseActivity)
                    val answerTextParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerPlaceHolder.id = R.id.answer_exercise_text_id
                    answerPlaceHolder.text = "Answer :"
                    answerPlaceHolder.textSize = 20F
                    answerPlaceHolder.setPadding(20)
                    answerPlaceHolder.setTextColor(color1)
                    answerTextParams.setMargins(10)

                    val inputOptionField = EditText(this@CreateExerciseActivity)
                    inputOptionField.setPadding(20)
                    inputOptionField.setTextColor(color1)
                    inputOptionField.hint = "Type option"
                    inputOptionField.setHintTextColor(color1)
                    inputOptionField.layoutParams = TableRow.LayoutParams(
                        600,
                        TableRow.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.setMargins(450, 10, 10, 10)
                        it.column = 2
                    }
                    testOptionsDTOS.add(TestOptionsDTO(null, inputOptionField))
                    layout.addView(answerPlaceHolder, answerTextParams)
                    layout.addView(inputOptionField)
                }

                ExerciseType.LONG_OPEN_ANSWER_EXERCISE.displayName -> {
                    layout.removeAllViews()
                    val black = ContextCompat.getColor(
                        this@CreateExerciseActivity,
                        R.color.black
                    )
                    val answerPlaceHolder = TextView(this@CreateExerciseActivity)
                    val answerTextParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    answerPlaceHolder.id = R.id.answer_exercise_text_id
                    answerPlaceHolder.text =
                        "Answer expectation is not supported for this type of questions"
                    answerPlaceHolder.textSize = 14F
                    answerPlaceHolder.setPadding(20)
                    answerPlaceHolder.setTextColor(black)
                    answerTextParams.setMargins(50)

                    layout.addView(answerPlaceHolder, answerTextParams)
                }
            }


            createBtn.setOnClickListener {
                val points: Int = pointsPicker.text.toString().toInt()
                val exerciseType: ExerciseType =
                    ExerciseType.getByDisplayName(adapterView.getItemAtPosition(i) as String)
                val question: String = question.text.toString()
                var options: List<String>? = null

                if (!testOptionsDTOS.isNullOrEmpty() && testOptionsDTOS.size > 1) {
                    options = testOptionsDTOS.map { it.option.text.toString() }.toList()
                    answer = testOptionsDTOS.asSequence().withIndex()
                        .filter { (_, value) -> value.active?.isChecked ?: false }
                        .map { (index, _) -> index.toString() }
                        .toList()
                        .joinToString(",")
                } else if (testOptionsDTOS.size == 1) {
                    answer = testOptionsDTOS[0].option.text.toString()
                }

                val exerciseTypedRequest =
                    ExerciseTypedRequest(exerciseType, question, options, points, answer)
                val testId: Long = intent.getLongExtra("id", 0)
                val call: Call<ResponseBody> = exerciseAPI.createExercise(
                    "token=${TokenHolder.getToken()}",
                    ExerciseRequest(exerciseTypedRequest, answer, testId)
                )

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            ShowPopupNotificationUtil.showPopup(
                                "Exercise successfully created",
                                this@CreateExerciseActivity
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.v("Retrofit", "call failed ${t.message}")
                    }
                })
                val intent = Intent(applicationContext, TeacherTestActivity::class.java)
                startActivity(intent)
            }
        }
    }
}