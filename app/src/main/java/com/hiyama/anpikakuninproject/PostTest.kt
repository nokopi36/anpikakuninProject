package com.hiyama.anpikakuninproject

import com.fasterxml.jackson.annotation.JsonProperty

class PostTest (
    @JsonProperty("uuid") var uuid:String,
    @JsonProperty("name") var userName:String,
    @JsonProperty("password") var passwpord:String,
    @JsonProperty("fcmtoken") var fcmtoken:String
) {
    companion object {
        fun getPostData() : PostTest{
            return PostTest(
                PostData.uuid,
                PostData.userName,
                PostData.password,
                PostData.fcmtoken
            )
        }
    }
}