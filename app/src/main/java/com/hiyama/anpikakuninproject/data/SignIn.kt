package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class SignIn(
    @JsonProperty("uuid") var signInNumber:String,
    @JsonProperty("name") var signInName:String,
    @JsonProperty("email") var signInEmail:String,
    @JsonProperty("password") var password:String,
    @JsonProperty("fcm_token") var fcm_token:String,
) {
    companion object {
        fun getSignInInfo() : SignIn {
            return SignIn(
                SignInInfo.signInNumber,
                SignInInfo.signInName,
                SignInInfo.signInEmail,
                SignInInfo.signInPassword,
                SignInInfo.fcm_token
            )
        }
    }
}