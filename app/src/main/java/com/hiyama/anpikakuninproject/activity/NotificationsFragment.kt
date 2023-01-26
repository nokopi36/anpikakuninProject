package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.utils.CommServer
import com.hiyama.anpikakuninproject.utils.Safety
import com.hiyama.anpikakuninproject.view.NotificationDialogFragment
import java.net.HttpURLConnection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NotificationsFragment : Fragment() {

    private val commServer = CommServer()
    private val safety = Safety()
    private val notificationDialogFragment = NotificationDialogFragment()

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
            Toast.makeText(activity, "更新完了", Toast.LENGTH_LONG).show()

            }

        return fragmentView
    }

    private fun getNotification(linearLayout: LinearLayout): Boolean{
        commServer.setURL(CommServer.NOTIFICATION)
        val result = commServer.getInfo()
        val urls = mutableListOf<String>()
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
                    val url = extractURLs(sortedNotification[index].content!!)
                    if (url.isEmpty()){
                        urls.add("noUrl")
                    } else {
                        urls.add(url[0])
                    }
                    Log.i("urls", urls.toString())
                    val button = Button(context)
                    button.text = sortedNotification[index].title
                    button.gravity = Gravity.START
                    button.gravity = Gravity.CENTER_VERTICAL
                    button.isAllCaps = false
                    button.setOnClickListener {
                        val args = Bundle()
                        args.putString("title", sortedNotification[index].title)
                        args.putString("content", sortedNotification[index].content)
                        if (urls.isNotEmpty()) args.putString("url", urls[index])
                        notificationDialogFragment.arguments = args
                        notificationDialogFragment.show(childFragmentManager, "notification")
                    }
                    linearLayout.addView(button)
                }
                true
            }
        } else {
            return true
        }
    }

    private fun extractURLs(text: String): List<String> {
        val regex = "(http://|https://)[\\w.\\-/:#?=&;%~+]+"
        val urls = regex.toRegex(RegexOption.IGNORE_CASE).findAll(text).map{it.value}
        return urls.toList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}