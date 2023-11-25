package com.demo.diploma.model

data class TestExercise(
    override var type: ExerciseType?,
    val question: String?,
    val options: List<String> = mutableListOf(),
    val givenAnswerIndexes: List<Int>?,
    override var points: Int = 0
) : IExercise