package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class Notification(
    @JsonProperty("title") var title: String,
    @JsonProperty("content") var content: String
) {
}