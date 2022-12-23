package com.hiyama.anpikakuninproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.LoginInfo
import com.hiyama.anpikakuninproject.data.NotificationInfo
import com.hiyama.anpikakuninproject.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection

class NotificationsFragment : Fragment() {

    val commServer = CommServer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false)
        val testTxt = fragmentView.findViewById<TextView>(R.id.testText)

        val testBtn = fragmentView.findViewById<Button>(R.id.testBtn)
        testBtn.setOnClickListener {
            commServer.setURL(CommServer.TEST)
            val result = getInfo()
            Log.i("receive result", result)
            testTxt.text = result
        }

        return fragmentView
    }

    private fun getNotification(){
        val result = getInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        if (commServer.responseCode == HttpURLConnection.HTTP_OK) {
            Log.i("Return Value From Server", "Value: $result")
            val notification = JsonParser.notificationParse(result)
            if (notification == null){
                Toast.makeText(activity, "予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
            } else {
                NotificationInfo.initialize(notification)
            }
        }
    }

    @UiThread
    private fun getInfo(): String{
        var result = ""
        runBlocking {
            result = commServer.getInfoBackGroundRunner("UTF-8")
            Log.i("GET",result)
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}