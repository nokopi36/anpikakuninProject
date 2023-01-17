package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.utils.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.*
import com.hiyama.anpikakuninproject.utils.Safety
import java.net.HttpURLConnection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationsFragment : Fragment() {

    val commServer = CommServer()
    private val safety = Safety()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false)
        val sharedPreferences = activity?.getSharedPreferences("safetyCheckIsShowed", Context.MODE_PRIVATE)
        val pastTime = sharedPreferences?.getString("nowTime", "NoPastTime")
        val isAnswered: Boolean
        if (pastTime == "NoPastTime"){
            isAnswered = true
        } else {
            val target = LocalDateTime.parse(pastTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val now = LocalDateTime.now()
            isAnswered = ChronoUnit.DAYS.between(target, now) >= 1
            Log.i("now", now.toString())
            Log.i("past", target.toString())
        }
        if (isAnswered){
            safety.safetyCheck(childFragmentManager)
        }
        Log.i("isAnswered", isAnswered.toString())

        val addLinearLayout = fragmentView.findViewById<LinearLayout>(R.id.addLinearLayout)
        val nothingNotification = fragmentView.findViewById<TextView>(R.id.nothingNotification)

        if (getNotification(addLinearLayout)){
            nothingNotification.text = ""
        } else {
            nothingNotification.text = "お知らせはありません"
        }

        val updateBtn = fragmentView.findViewById<Button>(R.id.updateBtn)
            updateBtn.setOnClickListener {
            getNotification(addLinearLayout)
        }

        return fragmentView
    }

    private fun getNotification(linearLayout: LinearLayout): Boolean{
        commServer.setURL(CommServer.NOTIFICATION)
        val result = commServer.getInfo()
//        val result = "{\"news\":[{\"auth_id\": 2, \"title\": \"1\", \"content\": \"news1\"}, {\"news_id\": 1, \"title\": \"2\", \"content\": \"news2\"}, {\"news_id\": 3, \"title\": \"3\", \"content\": \"news3\"}]}"
        while(commServer.responseCode == -1){/* wait for response */}
        Log.i("Return Val From Server", "Value: $result")
        val notification = JsonParser.newsParse(result)
        linearLayout.removeAllViews()
        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
            return if (notification.isNullOrEmpty()){
                false
            } else {
                val sortedNotification = notification.sortedWith(compareByDescending{ it.news_id })
                for ((index, _) in sortedNotification.withIndex()){
                    val button = Button(context)
                    button.text = sortedNotification[index].title
                    button.setOnClickListener {
                        Toast.makeText(activity, sortedNotification[index].content, Toast.LENGTH_SHORT).show()
                    }
                    linearLayout.addView(button)
                }
                true
            }
        } else {
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}