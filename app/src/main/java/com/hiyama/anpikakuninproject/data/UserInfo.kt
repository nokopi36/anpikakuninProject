package com.hiyama.anpikakuninproject.data

object UserInfo {
    var userName = ""
    var password = ""

    fun initialize(user: User) {
        userName = user.userName
        password = user.password
    }

}