package com.demo.diploma.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.diploma.api.ExerciseAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.ExerciseOperationsStatus
import com.demo.diploma.model.request.ExerciseEstimateRequest
import com.demo.diploma.model.request.PassExerciseRequest
import com.demo.diploma.model.response.PassExerciseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ExerciseRepository {

    private var exerciseAPI: ExerciseAPI = RetrofitConfiguration.getInstance()
        .create(ExerciseAPI::class.java)

    fun getStudentExerciseByTestId(testId: Long): LiveData<List<PassExerciseResponse>> {
        val data = MutableLiveData<List<PassExerciseResponse>>()
        var exercises: List<PassExerciseResponse>

        GlobalScope.launch(Dispatchers.IO) {

            val response: Response<List<PassExerciseResponse>> =
                exerciseAPI.getStudentExercisesByTestId("token=${TokenHolder.getToken()}", testId)
            if (response.isSuccessful && response.body() != null) {
                exercises = response.body()!!
                data.postValue(exercises)
            }
        }
        return data
    }

    fun passExercise(passExerciseRequest: PassExerciseRequest): LiveData<ExerciseOperationsStatus> {
        val data = MutableLiveData<ExerciseOperationsStatus>()
        GlobalScope.launch(Dispatchers.IO) {
            val response: Response<Void> =
                exerciseAPI.passExercise("token=${TokenHolder.getToken()}", passExerciseRequest)
            if (response.isSuccessful) {
                data.postValue(ExerciseOperationsStatus.SUCCESS)
            } else {
                data.postValue(ExerciseOperationsStatus.FAILED)
            }
        }
        return data
    }

    fun getStudentExerciseByTestIdAndStudentId(testId: Long, studentId: Long): LiveData<List<PassExerciseResponse>> {
        val data = MutableLiveData<List<PassExerciseResponse>>()
        var exercises: List<PassExerciseResponse>

        GlobalScope.launch(Dispatchers.IO) {

            val response: Response<List<PassExerciseResponse>> =
                exerciseAPI.getExercisesByStudentAndTestId("token=${TokenHolder.getToken()}", testId, studentId)
            if (response.isSuccessful && response.body() != null) {
                exercises = response.body()!!
                data.postValue(exercises)
            }
        }
        return data
    }

    fun estimateExercise(estimateRequest: ExerciseEstimateRequest): LiveData<ExerciseOperationsStatus> {
        val data = MutableLiveData<ExerciseOperationsStatus>()
        GlobalScope.launch(Dispatchers.IO) {
            val response: Response<Void> = exerciseAPI.estimateExercise("token=${TokenHolder.getToken()}", estimateRequest)
            if (response.isSuccessful) {
                data.postValue(ExerciseOperationsStatus.SUCCESS)
            } else {
                data.postValue(ExerciseOperationsStatus.FAILED)
            }
        }
        return data
    }
}