package com.hiyama.anpikakuninproject.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JsonParser {
    fun userParse(str: String) : User? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val user: User = mapper.readValue(str)
            user
        }
    }

    fun postResultTestParse(str: String) : PostResultTest? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val postResultTest: PostResultTest = mapper.readValue(str)
            postResultTest
        }
    }

}