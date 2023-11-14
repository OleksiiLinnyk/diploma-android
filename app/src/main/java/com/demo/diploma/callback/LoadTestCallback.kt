package com.demo.diploma.callback

import com.demo.diploma.model.response.TestResponse

interface LoadTestCallback {

   fun onSuccess(body: List<TestResponse>)
}