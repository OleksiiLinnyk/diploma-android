package com.demo.diploma.usecase.exercise

import androidx.lifecycle.LiveData
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.ExerciseEstimateRequest
import com.demo.diploma.repository.ExerciseRepository

class EstimateExerciseUseCase {

    private val exerciseRepository: ExerciseRepository = ExerciseRepository()

    fun estimateExercise(estimateRequest: ExerciseEstimateRequest): LiveData<ExerciseOperationsStatus> {
        return exerciseRepository.estimateExercise(estimateRequest)
    }
}