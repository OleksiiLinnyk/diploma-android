package com.demo.diploma.usecase.student

import androidx.lifecycle.LiveData
import com.demo.diploma.model.response.StudentTestResultResponse
import com.demo.diploma.repository.TestRepository

class GetStudentTestResultUseCase {

    private val testRepository: TestRepository = TestRepository()

    fun execute(): LiveData<List<StudentTestResultResponse>> {
        return testRepository.getStudentTestResult()
    }
}