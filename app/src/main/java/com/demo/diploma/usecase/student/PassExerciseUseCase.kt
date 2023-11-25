package com.demo.diploma.usecase.student

import androidx.lifecycle.LiveData
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.PassExerciseRequest
import com.demo.diploma.repository.ExerciseRepository

class PassExerciseUseCase {

    private val exerciseRepository: ExerciseRepository = ExerciseRepository()
    fun execute(passExerciseRequest: PassExerciseRequest): LiveData<ExerciseOperationsStatus> {
        return exerciseRepository.passExercise(passExerciseRequest)
    }
}