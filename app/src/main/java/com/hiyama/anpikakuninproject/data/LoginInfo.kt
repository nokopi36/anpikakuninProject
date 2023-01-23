package com.hiyama.anpikakuninproject.data

object LoginInfo {
    var succeed = false
    var message = ""
    var token = ""

    fun initialize(loginResult: LoginResult) {
        succeed = loginResult.succeed
        message = loginResult.message
        token = loginResult.token.toString()
    }
}