package com.hiyama.anpikakuninproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.*
import com.hiyama.anpikakuninproject.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection

import org.junit.Test

class NotificationsFragment : Fragment() {

    val commServer = CommServer()
    lateinit var notification: List<Notification>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false)
        val addLinearLayout = fragmentView.findViewById<LinearLayout>(R.id.addLinearLayout)

        getNotification(addLinearLayout)

        val updateBtn = fragmentView.findViewById<Button>(R.id.updateBtn)
            updateBtn.setOnClickListener {
            getNotification(addLinearLayout)
        }

        return fragmentView
    }

//    fun getNotification(){
//        val result = getInfo()
//        val notificationTitle = arrayListOf<String>()
//        val notificationContext = arrayListOf<String>()
////        while(commServer.responseCode == -1){/* wait for response */}
////        if (commServer.responseCode == HttpURLConnection.HTTP_OK) {
//            Log.i("Return Value From Server", "Value: $result")
//            val notification = JsonParser.notificationParse(result)
//        Log.i("notification parser", notification.toString())
//            if (notification == null){
//                Toast.makeText(activity, "予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
//            } else {
////                NotificationInfo.initialize(notification)
//                for (s in notification){
//                    notificationTitle.add(s.title)
//                    notificationContext.add(s.content)
//                }
//            }
////        }
//    }

    private fun getNotification(linearLayout: LinearLayout){
        val result = getInfo()
//        while(commServer.responseCode == -1){/* wait for response */}
        Log.i("Return Val From Server", "Value: $result")
        val notification = JsonParser.newsParse(result)
        Log.i("notification parser", notification!![0].toString())
        Log.i("ccccc", notification[0].news_id.toString())
        Log.i("aaaaa", notification[0].title!!)
        Log.i("bbbbb", notification[0].content!!)
        Log.i("ccccc", notification[1].news_id.toString())
        Log.i("aaaaa", notification[1].title!!)
        Log.i("bbbbb", notification[1].content!!)
        linearLayout.removeAllViews()
        if (notification.isEmpty()){
            /* do nothing */
        } else {
            for ((index, _) in notification.withIndex()){
                val button = Button(context)
                button.text = notification[index].title
                button.setOnClickListener {
                    Toast.makeText(activity, notification[index].content, Toast.LENGTH_SHORT).show()
                }
                linearLayout.addView(button)
            }
        }
    }

    @UiThread
    fun getInfo(): String{
        val result = "{\"news\":[{\"news_id\": 2, \"title\": \"1\", \"content\": \"news1\"}, {\"news_id\": 1, \"title\": \"2\", \"content\": \"news2\"}]}"
        Log.i("test result", result)
//        commServer.setURL(CommServer.NOTIFICATION)
//        runBlocking {
//            result = commServer.getInfoBackGroundRunner("UTF-8")
//            Log.i("GET",result)
//        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}