package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.utils.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.InquiryInfo
import com.hiyama.anpikakuninproject.utils.Safety
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class InquiryFragment : Fragment() {

    private val commServer = CommServer()
    private val safety = Safety()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_inquiry, container, false)
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

        val inquiryTitleEdit = fragmentView.findViewById<EditText>(R.id.inquiry_title)
        val inquiryContentEdit = fragmentView.findViewById<EditText>(R.id.inquiry_content)
        val title = inquiryTitleEdit.text.toString()
        val content = inquiryContentEdit.text.toString()

        val sendBtn = fragmentView.findViewById<Button>(R.id.sendBtn)
        sendBtn.setOnClickListener {
            if (title.isEmpty() || content.isEmpty()){
                AlertDialog.Builder(activity!!)
                    .setTitle("●送信失敗")
                    .setMessage("タイトルもしくは内容が入力されていません")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            } else {
                InquiryInfo.title = title
                InquiryInfo.content = content
                commServer.setURL(CommServer.INQUIRY)
                commServer.postInfo()
            }
        }

        return  fragmentView
    }

}