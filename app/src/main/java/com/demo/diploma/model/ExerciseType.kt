package com.demo.diploma.model

enum class ExerciseType(val displayName: String) {
    SINGLE_ANSWER_EXERCISE("Single Answer"),
    MULTIPLE_ANSWER_EXERCISE("Multiple Answer"),
    SHORT_OPEN_ANSWER_EXERCISE("Short Open Answer"),
    LONG_OPEN_ANSWER_EXERCISE("Long Open Answer");

    companion object {
        infix fun getByDisplayName(name: String): ExerciseType = ExerciseType.values().first {
            it.displayName == name
        }
    }
}