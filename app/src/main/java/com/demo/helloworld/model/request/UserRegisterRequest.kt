package com.demo.helloworld.model.request

data class UserRegisterRequest(
    val name: String,
    val email: String,
    val groupName: String,
    val subject: String,
    val password: String,
    val role: String
)