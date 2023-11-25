package com.demo.diploma.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.diploma.api.TestAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.GroupsProgressResponse
import com.demo.diploma.model.response.StudentTestResultResponse
import com.demo.diploma.model.response.StudentsProgressResponse
import com.demo.diploma.model.response.TestResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TestRepository {

    private var testAPI: TestAPI = RetrofitConfiguration.getInstance()
        .create(TestAPI::class.java)

    fun getTeacherTests(): LiveData<List<TestResponse>> {
        val data = MutableLiveData<List<TestResponse>>()
        GlobalScope.launch(Dispatchers.IO) {
            val response = testAPI.getTeacherTests("token=${TokenHolder.getToken()}")
            if (response.isSuccessful) {
                data.postValue(response.body())
            }
        }
        return data
    }

    fun getGroupsResultByTestId(testId: Long): LiveData<List<GroupsProgressResponse>> {
        val data = MutableLiveData<List<GroupsProgressResponse>>()
        GlobalScope.launch(Dispatchers.IO) {
            val response = testAPI.getGroupResultsByTestId("token=${TokenHolder.getToken()}", testId)
            if (response.isSuccessful) {
                data.postValue(response.body())
            }
        }
        return data
    }

    fun getUserResultsByTestAndGroupId(testId: Long, groupId: Long): LiveData<List<StudentsProgressResponse>> {
        val data = MutableLiveData<List<StudentsProgressResponse>>()
        GlobalScope.launch(Dispatchers.IO) {
            val response = testAPI.getUserResultsByTestAndGroupId("token=${TokenHolder.getToken()}", testId, groupId)
            if (response.isSuccessful) {
                data.postValue(response.body())
            }
        }
        return data
    }

    fun getStudentTestResult(): LiveData<List<StudentTestResultResponse>> {
        val data = MutableLiveData<List<StudentTestResultResponse>>()
        GlobalScope.launch(Dispatchers.IO) {
            val response = testAPI.getStudentTestsResult("token=${TokenHolder.getToken()}")
            if (response.isSuccessful) {
                data.postValue(response.body())
            }
        }
        return data
    }
}