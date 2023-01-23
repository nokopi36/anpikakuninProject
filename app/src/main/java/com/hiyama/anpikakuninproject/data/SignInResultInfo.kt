package com.hiyama.anpikakuninproject.data

object SignInResultInfo{
    var succeed = false
    var message = ""

    fun initialize(signInResult: SignInResult) {
        succeed = signInResult.succeed
        message = signInResult.message
    }
}