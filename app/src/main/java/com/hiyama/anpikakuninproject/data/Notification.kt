package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class Notification(
    @JsonProperty("news_id") var news_id : Int,
    @JsonProperty("title") var title: String?,
    @JsonProperty("content") var content: String?
)