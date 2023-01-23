package com.hiyama.anpikakuninproject.utils

import android.util.Log
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hiyama.anpikakuninproject.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

@Suppress("BlockingMethodInNonBlockingContext")
class CommServer {
    companion object {
        private const val DEBUG_TAG = "anpikakuninProject"

        const val TIMEOUT_MILLIS = 5000 // タイムアウト5秒
        const val GET = "GET"
        const val POST = "POST"

        const val POSTTEST = -2
        const val TEST = -1
        const val LOGIN = 0
        const val SCHEDULE = 1
        const val OPERATIONINFO = 2
        const val NOTIFICATION = 3
        const val INQUIRY = 4
        const val SAFETY_CHECK = 5
        const val CHANGE_PASSWORD = 6
        const val NEW_USER = 7

        var ipAddress = "hcu-app.com"
        var port = "443"

    }

//    private val ipAddress = "160.248.2.236"
//    private val port = "3000"

//    本番用のipAddress
//    private val ipAddress = "35.73.177.154" or "ec2-35-73-177-154.ap-northeast-1.compute.amazonaws.com"
//    private val port = "3000"

    private var request = ""
    private var url = ""
    private var postData = ""
    var responseCode = -1

    private fun setRequest(req: String) {
        if (req == GET || req == POST) {
            this.request = req
        } else {
            Log.e("ERROR", "存在しないリクエストモードです")
        }
    }

    fun setURL(mode: Int) {
        when (mode) {
            POSTTEST -> {
                setRequest(POST)
                url = "https://$ipAddress:$port/api/login"
                postData = jacksonObjectMapper().writeValueAsString(PostTest.getPostData())
            }
            TEST -> {
                setRequest(GET)
                url = "https://$ipAddress:$port/test"
            }
            LOGIN -> {
                setRequest(POST)
                url = "https://$ipAddress:$port/api/login"
                postData = jacksonObjectMapper().writeValueAsString(User.getUserInfo())
                Log.i("postData", postData)
            }
            SCHEDULE -> {
                setRequest(GET)
                url = "https://$ipAddress:$port/api/timetable"
            }
            OPERATIONINFO -> {
                url = ""
            }
            NOTIFICATION -> {
                setRequest(GET)
                url = "https://$ipAddress:$port/api/news"
            }
            INQUIRY -> {
                setRequest(POST)
                url = "https://$ipAddress:$port/api/inquiry"
                postData = jacksonObjectMapper().writeValueAsString(Inquiry.getInquiryInfo())
                Log.i("postData", postData)
            }
            SAFETY_CHECK -> {
                setRequest(GET)
                url = "https://$ipAddress:$port/api/safety-check"
            }
            CHANGE_PASSWORD -> {
                setRequest(POST)
                url = "https://$ipAddress:$port/api/login/update"
                postData = jacksonObjectMapper().writeValueAsString(PassWord.getPasswordData())
                Log.i("postData", postData)
            }
            NEW_USER -> {
                setRequest(POST)
                url = "https://$ipAddress:$port/api/sign-in"
                postData = jacksonObjectMapper().writeValueAsString(SignIn.getSignInInfo())
                Log.i("postData", postData)
            }
        }
    }

    @WorkerThread
    suspend fun getInfoBackGroundRunner(encoding: String): String {
        val sb = StringBuffer("")
        val esb = StringBuffer("")
        val getUrl = URL(url)
        Log.i("checkAccessURL", "Access to URL: $getUrl")
        val huc = getUrl.openConnection() as? HttpURLConnection
        val returnVal = withContext(Dispatchers.IO) {
            huc?.let {
                try {
                    it.connectTimeout = TIMEOUT_MILLIS
                    it.readTimeout = TIMEOUT_MILLIS
                    it.requestMethod = request
                    it.useCaches = false
                    it.doOutput = false
                    it.doInput = true
                    it.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    it.setRequestProperty("x-access-token", LoginInfo.token)
                    it.connect()

                    val responseCode = it.responseCode
                    this@CommServer.responseCode = responseCode
                    Log.i("get Response", "ResponseCode: ${this@CommServer.responseCode}")

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val ism = it.inputStream
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            sb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
                    } else {
                        val ism = it.errorStream
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            esb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
                    }
                } catch (e: SocketTimeoutException) {
                    Log.w(DEBUG_TAG, "通信タイムアウト", e)
                    responseCode = 0
                } catch (e: IOException) {
                    Log.i(DEBUG_TAG, "接続できません", e)
                    throw e
                } finally {
                    it.disconnect()
                }
//                it.disconnect()
            }
            sb.toString()
        }
        Log.i("get:returnVAL", returnVal)
        return returnVal
    }

    @WorkerThread
    suspend fun postInfoBackGroundRunner(encoding: String): String {
        val sb = StringBuffer("")
        val esb = StringBuffer("")
        val getUrl = URL(url)
        Log.i("checkAccessURL", "Access to URL: $getUrl")
        val huc = getUrl.openConnection() as? HttpURLConnection
        val returnVal = withContext(Dispatchers.IO) {
            huc?.let {
                try {
                    it.connectTimeout = TIMEOUT_MILLIS
                    it.readTimeout = TIMEOUT_MILLIS
                    it.requestMethod = request
                    it.useCaches = false
                    it.doOutput = true
                    it.doInput = true
                    it.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    it.setRequestProperty("x-access-token", LoginInfo.token)
                    it.connect()

                    val outputStream = it.outputStream
                    outputStream.write(postData.toByteArray())
                    outputStream.flush()
                    outputStream.close()
                    Log.i("Send Data to Server", "DATA: $postData")

                    val responseCode = it.responseCode
                    this@CommServer.responseCode = responseCode
                    Log.i("get Response", "ResponseCode: ${this@CommServer.responseCode}")

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val ism = it.inputStream
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            sb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
                    } else {
                        val ism = it.errorStream
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            esb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
                    }
                } catch (e: SocketTimeoutException) {
                    Log.w(DEBUG_TAG, "通信タイムアウト", e)
                }
                it.disconnect()
            }
            sb.toString()
        }
        Log.i("post:returnVAL", returnVal)
        return returnVal
    }

    @UiThread
    fun getInfo(): String{
        var result: String
        runBlocking {
            result = getInfoBackGroundRunner("UTF-8")
            Log.i("GET",result)
        }
        return result
    }

    @UiThread
    fun postInfo(): String{ //postTest
        var result: String
        runBlocking { // postして結果が返ってくるまで待機
            result = postInfoBackGroundRunner("UTF-8")
            Log.i("POST",result)
        }
        return result
    }

}