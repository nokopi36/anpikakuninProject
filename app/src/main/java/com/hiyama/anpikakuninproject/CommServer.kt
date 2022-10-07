package com.hiyama.anpikakuninproject

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class CommServer {
    companion object {
        private const val DEBUG_TAG = "anpikakuninProject"

        const val TIMEOUT_MILLIS = 5000 // タイムアウト5秒
        const val GET = "GET"
        const val POST = "POST"

        const val LOGIN = 0
        const val SCHEDULE = 1
        const val OPERATIONINFO = 2
        const val NOTIFICATION = 3
        const val INQUIRY = 4

//        val UB: Uri.Builder = Uri.Builder()

//        @RequiresApi(Build.VERSION_CODES.M)
//        fun isConnected(activity: AppCompatActivity) : Boolean{
//            val connMgr = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val caps = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
//            if (caps!=null){
//                return true
//            }
//            return false
//        }
    }

    private val ipAddress = ""
    private val port = ""
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
            LOGIN -> {
                url = ""
            }
            SCHEDULE -> {
                url = ""
            }
            OPERATIONINFO -> {
                url = ""
            }
            NOTIFICATION -> {
                url = ""
            }
            INQUIRY -> {
                url = ""
            }
        }
    }

    suspend fun get(encoding: String): String {
        val sb = StringBuffer("")
        val esb = StringBuffer("")
//        var br: BufferedReader?
//        var ism: InputStream?
//        var isr: InputStreamReader?

        val getUrl = URL(url)
        Log.i("checkAccessURL", "Access to URL: $getUrl")
        val huc = getUrl.openConnection() as HttpURLConnection
        val returnVal = withContext(Dispatchers.IO) {
            huc.let {
                try {
                    it.connectTimeout = TIMEOUT_MILLIS
                    it.readTimeout = TIMEOUT_MILLIS
                    it.requestMethod = request
                    it.useCaches = false
                    it.doOutput = (request == POST)
                    it.doInput = true
                    it.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    it.connect()

                    if (request == POST) {
                        val outputStream = it.outputStream
                        outputStream.write(postData.toByteArray())
                        outputStream.flush()
                        outputStream.close()
                        Log.i("Send Data to Server", "DATA: $postData")
                    }

                    val responseCode = it.responseCode
                    this@CommServer.responseCode = responseCode
                    Log.i("get Response", "ResponseCode: ${this@CommServer.responseCode}")

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val ism = it.inputStream
                        //                isr = InputStreamReader(ism, encoding)
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            sb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
//                        sb.toString()
                    } else {
                        val ism = it.errorStream
                        //                isr = InputStreamReader(ism, encoding)
                        val br = BufferedReader(InputStreamReader(ism, encoding))
                        var line: String? = ""
                        while (line != null) {
                            esb.append(line)
                            line = br.readLine()
                        }
                        ism.close()
//                        sb.toString()
                    }
                } catch (e: SocketTimeoutException) {
                    Log.w(DEBUG_TAG, "通信タイムアウト", e)
                }
                it.disconnect()
            }
            sb.toString()
        }
        return returnVal
    }
}