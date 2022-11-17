package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class SafetyCheck(
    @JsonProperty("check") var check: String
) {
}