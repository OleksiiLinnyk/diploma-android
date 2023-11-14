package com.demo.diploma.callback

import com.demo.diploma.model.response.ExerciseResponse

interface LoadExerciseDetailsCallback {

    fun onSuccess(body: List<ExerciseResponse>)
}