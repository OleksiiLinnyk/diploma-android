package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.ExerciseEstimateRequest
import com.demo.diploma.model.response.PassExerciseResponse
import com.demo.diploma.usecase.exercise.EstimateExerciseUseCase
import com.demo.diploma.usecase.student.GetStudentExerciseByTestIdAndStudentIdUseCase

class CheckResultTeacherStudentExercisesViewModel : ViewModel() {

    private val getStudentExerciseByTestIdAndStudentIdUseCase: GetStudentExerciseByTestIdAndStudentIdUseCase =
        GetStudentExerciseByTestIdAndStudentIdUseCase()

    private val estimateExerciseUseCase: EstimateExerciseUseCase = EstimateExerciseUseCase()

    fun getStudentExerciseByTestIdAndStudentId(testId: Long, studentId: Long): LiveData<List<PassExerciseResponse>> {
        return getStudentExerciseByTestIdAndStudentIdUseCase.execute(testId, studentId)
    }

    fun estimateExercise(exerciseEstimateRequest: ExerciseEstimateRequest): LiveData<ExerciseOperationsStatus> {
        return estimateExerciseUseCase.estimateExercise(exerciseEstimateRequest)
    }
}