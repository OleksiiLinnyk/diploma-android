package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.response.GroupsProgressResponse
import com.demo.diploma.usecase.test.GetGroupsResultByTestIdUseCase

class CheckResultTeacherGroupsViewModel: ViewModel() {

    private val getGroupsResultByTestIdUseCase: GetGroupsResultByTestIdUseCase = GetGroupsResultByTestIdUseCase()

    fun getGroupsResultByTestId(testId: Long): LiveData<List<GroupsProgressResponse>> {
        return getGroupsResultByTestIdUseCase.execute(testId)
    }
}