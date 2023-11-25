package com.demo.diploma.model.response

data class GroupsProgressResponse(
    val groupId: Long,
    val groupName: String,
    val totalUsers: Int = 0,
    val awaiting: Int = 0,
    val toCheck: Int = 0,
    val done: Int = 0
)