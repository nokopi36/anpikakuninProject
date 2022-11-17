package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class PassWord (
    @JsonProperty("old_password") var old_password:String,
    @JsonProperty("new_password") var new_password:String
) {
    companion object {
        fun getPasswordData() : PassWord {
            return PassWord(
                PassWordInfo.old_password,
                PassWordInfo.new_password
            )
        }
    }
}