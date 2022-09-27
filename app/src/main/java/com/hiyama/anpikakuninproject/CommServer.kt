package com.hiyama.anpikakuninproject

import android.util.Log
import android.widget.Toast

class CommServer {
    companion object{
        const val TIMEOUT_MILLIS = 5000 // タイムアウト5秒
        const val GET = "GET"
        const val POST = "POST"

        const val LOGIN = 0
        const val SCHEDULE = 1
        const val OPERATIONINFO = 2
        const val NOTIFICATION = 3
        const val INQUIRY = 4
    }

    private val ipAddress = ""
    private val port = ""
    private var request = ""
    private var url = ""
    private var postData = ""
    var responseCode = -1

    private fun setRequest(req: String){
        if (req == GET || req == POST){
            this.request = req
        } else {
            Log.e("ERROR", "存在しないリクエストモードです")
        }
    }

}