package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.annotation.JsonProperty

class Inquiry(
    @JsonProperty("title") var title : String,
    @JsonProperty("content") var content : String
){
    companion object {
        fun getInquiryInfo() : Inquiry {
            return Inquiry(
                InquiryInfo.title,
                InquiryInfo.content
            )
        }
    }
}