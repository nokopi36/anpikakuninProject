package com.hiyama.anpikakuninproject.data

object UserInfo {
    var userName = ""
    var password = ""
    var fcmToken = ""

    fun initialize(user: User) {
        userName = user.userName
        password = user.password
        fcmToken = user.fcmtoken
    }

}