package com.demo.diploma.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.demo.diploma.R
import com.demo.diploma.activity.teacher.exercise.CustomArrayAdapter
import com.demo.diploma.callback.ExerciseByIdCallback
import com.demo.diploma.model.ExerciseType
import com.demo.diploma.model.response.ExerciseResponse
import com.demo.diploma.usecase.exercise.GetExerciseByIdCase
import com.demo.diploma.usecase.exercise.RemoveExerciseByIdCase

class ExerciseDetailsFragment : DialogFragment() {

    private val getExerciseByIdCase: GetExerciseByIdCase = GetExerciseByIdCase()
    private val removeExerciseByIdCase: RemoveExerciseByIdCase = RemoveExerciseByIdCase()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exerciseTypeView: TextView = view.findViewById(R.id.exercise_type_placeholder)
        val pointsView: TextView = view.findViewById(R.id.points_place_holder)
        val questionView: TextView = view.findViewById(R.id.question_place_holder)
        val optionsPlaceHolder: ListView = view.findViewById(R.id.options_placeholder)
        val optionsTextView: TextView = view.findViewById(R.id.options_text_view)
        val removeBtn: ImageView = view.findViewById(R.id.remove_exercise_btn)
        val id = arguments?.getLong("exerciseId")!!

        getExerciseByIdCase.execute(id, object :
            ExerciseByIdCallback {
            override fun onSuccess(body: ExerciseResponse) {
                exerciseTypeView.text = body.exercise.type.displayName
                pointsView.text = body.exercise.points.toString()
                questionView.text = body.exercise.question

                if (body.exercise.type == ExerciseType.SINGLE_ANSWER_EXERCISE) {
                    val arrayAdapter: ArrayAdapter<String> = CustomArrayAdapter(view.context, android.R.layout.simple_list_item_single_choice, body.exercise.options)
                    optionsPlaceHolder.choiceMode = ListView.CHOICE_MODE_SINGLE
                    optionsPlaceHolder.adapter = arrayAdapter
                    optionsPlaceHolder.setItemChecked(body.answer.toInt(), true)
                } else if (body.exercise.type == ExerciseType.MULTIPLE_ANSWER_EXERCISE) {
                    val arrayAdapter: ArrayAdapter<String> = CustomArrayAdapter(view.context, android.R.layout.simple_list_item_multiple_choice, body.exercise.options)

                    optionsPlaceHolder.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                    optionsPlaceHolder.adapter = arrayAdapter
                    body.answer.split(",").map {
                        it.trim()

                    }.forEach {
                        optionsPlaceHolder.setItemChecked(it.toInt(), true)
                    }
                } else if (body.exercise.type == ExerciseType.SHORT_OPEN_ANSWER_EXERCISE) {
                    val arrayAdapter: ArrayAdapter<String> = CustomArrayAdapter(view.context, android.R.layout.simple_list_item_1, mutableListOf(body.answer))

                    optionsPlaceHolder.choiceMode = ListView.CHOICE_MODE_NONE
                    optionsPlaceHolder.adapter = arrayAdapter
                    optionsTextView.text = "Correct Answer"
                } else if (body.exercise.type == ExerciseType.LONG_OPEN_ANSWER_EXERCISE) {
                    optionsTextView.text = "Answers should be checked manually"
                }
            }
        })

        removeBtn.setOnClickListener {
            removeExerciseByIdCase.execute(id, it.context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise_details, container, false)
    }
}