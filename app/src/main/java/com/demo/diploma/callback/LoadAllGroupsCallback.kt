package com.demo.diploma.callback

import com.demo.diploma.model.response.GroupResponse

interface LoadAllGroupsCallback {

    fun onSuccess(body: List<GroupResponse>)
}