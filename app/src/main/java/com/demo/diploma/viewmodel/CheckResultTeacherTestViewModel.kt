package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.usecase.test.GetTeacherTestsUseCase

class CheckResultTeacherTestViewModel : ViewModel() {

    private val getTeacherTestsUseCase: GetTeacherTestsUseCase = GetTeacherTestsUseCase()

    fun getTeacherTests(): LiveData<List<TestResponse>> {
        return getTeacherTestsUseCase.execute()
    }
}