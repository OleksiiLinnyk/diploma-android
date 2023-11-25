package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.response.StudentsProgressResponse
import com.demo.diploma.usecase.test.GetStudentsByGroupAndTestUseCase

class CheckResultTeacherUsersViewModel : ViewModel() {

    private val getStudentsByGroupAndTestUseCase: GetStudentsByGroupAndTestUseCase = GetStudentsByGroupAndTestUseCase()

    fun getUserResultsByTestAndGroupId(testId: Long, groupId: Long): LiveData<List<StudentsProgressResponse>> {
        return getStudentsByGroupAndTestUseCase.execute(testId, groupId)
    }
}