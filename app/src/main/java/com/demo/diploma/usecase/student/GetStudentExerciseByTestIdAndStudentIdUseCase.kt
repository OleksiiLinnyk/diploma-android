package com.demo.diploma.usecase.student

import androidx.lifecycle.LiveData
import com.demo.diploma.model.response.PassExerciseResponse
import com.demo.diploma.repository.ExerciseRepository

class GetStudentExerciseByTestIdAndStudentIdUseCase {

    private val exerciseRepository: ExerciseRepository = ExerciseRepository()
    fun execute(testId: Long, studentId: Long): LiveData<List<PassExerciseResponse>> {
        return exerciseRepository.getStudentExerciseByTestIdAndStudentId(testId, studentId)
    }
}