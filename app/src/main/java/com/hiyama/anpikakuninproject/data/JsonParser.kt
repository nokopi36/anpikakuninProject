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

    fun loginResultParse(str: String) : LoginResult? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val loginResult: LoginResult = mapper.readValue(str)
            loginResult
        }
    }

    fun safetyCheckParse(str: String) : SafetyCheck? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val safetyCheck: SafetyCheck = mapper.readValue(str)
            safetyCheck
        }
    }

    fun changePasswordParse(str: String) : ChangePasswordResult? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val changePasswordResult: ChangePasswordResult = mapper.readValue(str)
            changePasswordResult
        }
    }

    fun newsParse(str: String) : List<Notification>? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val news= mapper.readValue<News>(str).news
            news
        }
    }

    fun signInResultParse(str: String) : SignInResult? {
        return if (str == "") null // 文字列が何も含まれていない
        else {
            val mapper = jacksonObjectMapper()
            val signInResult: SignInResult = mapper.readValue(str)
            signInResult
        }
    }

}