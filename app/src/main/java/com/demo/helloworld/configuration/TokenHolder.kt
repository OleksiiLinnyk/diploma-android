package com.demo.helloworld.configuration

object TokenHolder {

    private var token: String = ""

    fun saveToken(tokenValue: String) {
        token = tokenValue
    }

    fun getToken(): String {
        return token
    }
}