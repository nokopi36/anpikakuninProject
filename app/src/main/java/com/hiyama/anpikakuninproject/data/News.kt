package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class News(
    @JsonProperty("news") var news: List<Notification> = emptyList()
)