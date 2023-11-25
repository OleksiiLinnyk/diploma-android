package com.demo.diploma.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.demo.diploma.R
import com.demo.diploma.activity.teacher.CheckResultTeacherStudentExercisesActivity
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.ExerciseEstimateRequest
import com.demo.diploma.viewmodel.CheckResultTeacherStudentExercisesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EstimateExerciseFragment : DialogFragment() {

    private lateinit var checkResultTeacherStudentExercisesViewModel: CheckResultTeacherStudentExercisesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_estimate_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkResultTeacherStudentExercisesViewModel = ViewModelProvider(requireActivity())[CheckResultTeacherStudentExercisesViewModel::class.java]

        val createBtn = view.findViewById<Button>(R.id.submit_btn)
        val pointInput = view.findViewById<EditText>(R.id.input_point_name)
        val exerciseId = arguments?.getLong("exerciseId")!!
        val studentId = arguments?.getLong("studentId")!!
        val groupId = arguments?.getLong("groupId")!!
        val testId = arguments?.getLong("testId")!!
        val maxPoints = arguments?.getInt("maxPoints")!!

        createBtn.setOnClickListener {
            val points = pointInput.text.toString()
            if (points.isNullOrEmpty()) {
                Toast.makeText(view.context, "Please specify points", Toast.LENGTH_LONG).show()
            } else if (points.toInt() > maxPoints) {
                Toast.makeText(view.context, "Max amount of points = $maxPoints", Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    checkResultTeacherStudentExercisesViewModel.estimateExercise(ExerciseEstimateRequest(exerciseId, studentId, points.toInt()))
                        .observe(this@EstimateExerciseFragment) {
                            if (it == ExerciseOperationsStatus.SUCCESS) {
                                Toast.makeText(view.context, "Estimation has been applied", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(view.context, "Something went wrong", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                val intent = Intent(view.context, CheckResultTeacherStudentExercisesActivity::class.java)
                intent.putExtra("testId", testId)
                intent.putExtra("groupId", groupId)
                intent.putExtra("studentId", studentId)
                startActivity(intent)
            }
        }
    }
}