package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

class Notification(
    @JsonProperty("author_id") var auth_id : Int,
    @JsonProperty("title") var title: String?,
    @JsonProperty("content") var content: String?,
    @JsonProperty("tag") var tag: List<String>?,
    @JsonProperty("datetime") var datetime: Date?,
    @JsonProperty("isPublished") var isPublished: Boolean,
    @JsonProperty("news_id") var news_id: Int
)