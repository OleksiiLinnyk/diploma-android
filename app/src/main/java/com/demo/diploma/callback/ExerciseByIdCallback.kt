package com.demo.diploma.callback

import com.demo.diploma.model.response.ExerciseResponse

interface ExerciseByIdCallback {

    fun onSuccess(body: ExerciseResponse)
}