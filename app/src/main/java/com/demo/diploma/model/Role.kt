package com.demo.diploma.model

enum class Role(val value: String, val displayName: String) {

    TEACHER("ROLE_TEACHER", "Teacher"),
    STUDENT("ROLE_STUDENT", "Student"),
    ADMIN("ROLE_ADMIN", "Admin");

    companion object {
        infix fun getByName(name: String): Role = Role.values().first {
            it.value == name
        }
    }
}