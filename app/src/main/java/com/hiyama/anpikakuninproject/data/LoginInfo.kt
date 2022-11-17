package com.hiyama.anpikakuninproject.data

object LoginInfo {
    var success = false
    var message = ""
    var token = ""

    fun initialize(loginResult: LoginResult) {
        success = loginResult.success
        message = loginResult.message
        token = loginResult.token.toString()
    }
}