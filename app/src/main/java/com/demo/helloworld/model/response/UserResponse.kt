package com.demo.helloworld.model.response

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val subject: String,
    val role: RoleResponse,
    val password: String,
    val group: GroupResponse?
)