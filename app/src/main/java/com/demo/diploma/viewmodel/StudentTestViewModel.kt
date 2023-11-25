package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.PassExerciseRequest
import com.demo.diploma.model.response.PassExerciseResponse
import com.demo.diploma.usecase.student.GetStudentExerciseByTestUseCase
import com.demo.diploma.usecase.student.PassExerciseUseCase

class StudentTestViewModel : ViewModel() {
    private val getStudentExerciseByTestUseCase: GetStudentExerciseByTestUseCase =
        GetStudentExerciseByTestUseCase()

    private val passExerciseUseCase: PassExerciseUseCase = PassExerciseUseCase()
    fun getStudentExercisesByTestId(testId: Long): LiveData<List<PassExerciseResponse>> {
        return getStudentExerciseByTestUseCase.execute(testId)
    }

    fun passExercise(passExerciseRequest: PassExerciseRequest): LiveData<ExerciseOperationsStatus> {
        return passExerciseUseCase.execute(passExerciseRequest)
    }
}