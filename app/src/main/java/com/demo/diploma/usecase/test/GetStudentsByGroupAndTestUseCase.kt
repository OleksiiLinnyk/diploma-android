package com.demo.diploma.usecase.test

import androidx.lifecycle.LiveData
import com.demo.diploma.model.response.StudentsProgressResponse
import com.demo.diploma.repository.TestRepository

class GetStudentsByGroupAndTestUseCase {

    private val testRepository: TestRepository = TestRepository()

    fun execute(testId: Long, groupId: Long): LiveData<List<StudentsProgressResponse>> {
        return testRepository.getUserResultsByTestAndGroupId(testId, groupId)
    }
}