package com.hiyama.anpikakuninproject.data

object ChangePasswordResultInfo {
    var success = false

    fun initialize(changePasswordResult: ChangePasswordResult) {
        success = changePasswordResult.success
    }
}