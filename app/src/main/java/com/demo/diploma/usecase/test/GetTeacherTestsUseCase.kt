package com.demo.diploma.usecase.test

import androidx.lifecycle.LiveData
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.repository.TestRepository

class GetTeacherTestsUseCase {

    private val testRepository: TestRepository = TestRepository()

    fun execute(): LiveData<List<TestResponse>> {
        return testRepository.getTeacherTests()
    }
}