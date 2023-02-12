package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class User (
    @JsonProperty("uuid") var userName:String,
    @JsonProperty("password") var password:String,
    @JsonProperty("fcm_token") var fcmtoken:String
) {
    companion object {
        fun getUserInfo() : User {
            return User(
                UserInfo.userName,
                UserInfo.password,
                UserInfo.fcmToken
            )
        }
    }
}