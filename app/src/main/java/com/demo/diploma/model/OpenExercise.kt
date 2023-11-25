package com.demo.diploma.model

data class OpenExercise(
    override var type: ExerciseType?,
    val question: String?,
    val givenAnswer: String?,
    override var points: Int = 0
) : IExercise