package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class ChangePasswordResult(
    @JsonProperty("success") var success: Boolean
    ) {
}