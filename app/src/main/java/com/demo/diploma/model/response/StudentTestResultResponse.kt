package com.demo.diploma.model.response

data class StudentTestResultResponse(
    val testId: Long,
    val theme: String,
    val subject: String,
    val status: String,
    val takenPoints: Int,
    val totalPoints: Int
)