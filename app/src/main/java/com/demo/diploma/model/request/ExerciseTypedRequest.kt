package com.demo.diploma.model.request

import com.demo.diploma.model.ExerciseType

data class ExerciseTypedRequest(val type: ExerciseType, val question: String, val options: List<String>? = null, val points: Int, val answer: String)