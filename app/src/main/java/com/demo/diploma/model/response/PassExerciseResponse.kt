package com.demo.diploma.model.response

import com.demo.diploma.model.IExercise

data class PassExerciseResponse(
    val id: Long,
    val testId: Long,
    val exercise: IExercise,
    val givenAnswer: String,
    val checked: Boolean,
    val takenPoints: Int
)