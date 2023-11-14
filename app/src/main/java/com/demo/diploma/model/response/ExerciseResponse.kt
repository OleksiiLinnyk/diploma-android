package com.demo.diploma.model.response

import com.demo.diploma.model.ExerciseType

data class ExerciseResponse(val id: Long, val testId: Long, val answer: String, val exercise: Exercise)


data class Exercise(val points: Int, val question: String, val options: List<String> = emptyList(), val type: ExerciseType)