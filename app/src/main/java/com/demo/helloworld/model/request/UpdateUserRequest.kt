package com.demo.helloworld.model.request

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)