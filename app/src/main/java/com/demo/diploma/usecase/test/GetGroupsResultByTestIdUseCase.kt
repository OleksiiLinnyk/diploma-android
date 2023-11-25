package com.demo.diploma.usecase.test

import androidx.lifecycle.LiveData
import com.demo.diploma.model.response.GroupsProgressResponse
import com.demo.diploma.repository.TestRepository

class GetGroupsResultByTestIdUseCase {
    private val testRepository: TestRepository = TestRepository()

    fun execute(testId: Long): LiveData<List<GroupsProgressResponse>> {
        return testRepository.getGroupsResultByTestId(testId)
    }
}