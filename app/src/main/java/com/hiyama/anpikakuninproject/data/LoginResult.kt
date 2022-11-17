package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class LoginResult (
    @JsonProperty("success") var success: Boolean,
    @JsonProperty("message") var message:String,
    @JsonProperty("token") var token:String?
){

}