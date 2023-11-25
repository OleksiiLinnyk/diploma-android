package com.demo.diploma.model

enum class TestStatus(val value: String, val displayName: String) {

    TODO("todo", "Not started"), REVIEW("review", "In Review"), DONE("done", "Finished");

    companion object {
        infix fun getByValue(value: String): TestStatus = TestStatus.values().first {
            it.value == value
        }
    }
}