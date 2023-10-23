package com.demo.helloworld.model.request

import com.demo.helloworld.model.ExerciseType

data class ExerciseTypedRequest(val type: ExerciseType, val question: String, val options: List<String>? = null, val points: Int, val answer: String)