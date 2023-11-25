package com.demo.diploma.configuration

import com.demo.diploma.deserializer.IExerciseDeserializer
import com.demo.diploma.model.IExercise
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfiguration {

    companion object {
        val BASE_URL = "http://10.0.2.2:8081/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                    .registerTypeAdapter(IExercise::class.java, IExerciseDeserializer())
                    .create()))
                .build()
        }
    }
}