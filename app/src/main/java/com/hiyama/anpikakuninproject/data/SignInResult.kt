package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class SignInResult(
    @JsonProperty("succeed") var succeed: Boolean,
    @JsonProperty("message") var message:String
)