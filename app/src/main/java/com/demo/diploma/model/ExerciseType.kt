package com.demo.diploma.model

enum class ExerciseType(val displayName: String, val description: String) {
    SINGLE_ANSWER_EXERCISE("Single Answer", "Please select only one option"),
    MULTIPLE_ANSWER_EXERCISE("Multiple Answer", "You can select many options"),
    SHORT_OPEN_ANSWER_EXERCISE("Short Open Answer", "Fill te graph"),
    LONG_OPEN_ANSWER_EXERCISE("Long Open Answer", "Fill te graph");

    companion object {
        infix fun getByDisplayName(name: String): ExerciseType = ExerciseType.values().first {
            it.displayName == name
        }
    }
}