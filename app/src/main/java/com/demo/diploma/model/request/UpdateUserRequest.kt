package com.demo.diploma.model.request

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)