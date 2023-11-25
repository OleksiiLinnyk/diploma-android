package com.demo.diploma.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.demo.diploma.model.response.StudentTestResultResponse
import com.demo.diploma.usecase.student.GetStudentTestResultUseCase

class StudentTestResultViewModel : ViewModel() {

    private val getStudentTestResultUseCase: GetStudentTestResultUseCase = GetStudentTestResultUseCase()

    fun getStudentTestResult(): LiveData<List<StudentTestResultResponse>> {
        return getStudentTestResultUseCase.execute()
    }
}