package com.demo.diploma.util

import android.util.Base64
import android.util.Log
import com.demo.diploma.model.JWTModel
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

object JWTUtils {

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Throws(Exception::class)
    fun decoded(jwtEncoded: String): JWTModel? {
        try {
            val split =
                jwtEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return objectMapper.readValue(getJson(split[1]), JWTModel::class.java)

        } catch (e: UnsupportedEncodingException) {
            //Error
        }
        return null;
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, Charset.forName("UTF-8"))
    }
}