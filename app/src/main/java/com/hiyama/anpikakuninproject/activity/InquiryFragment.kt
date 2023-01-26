package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
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
    private var buttonTitle = arrayOf(
        "いちぽる",
        "WebClass",
        "UNIVERSAL PASSPORT RX",
        "2022年度学年歴",
        "2022年度授業カレンダー",
        "大学売店Twitter",
        "食堂メニュー公開Twitter",
        "広島市立大学",
        "情報処理センター",
        "語学センター",
        "付属図書館",
        "芸術資料館",
        "キャリアセンター"
    )
    private var buttonUrl = arrayOf(
        "https://ichipol.hiroshima-cu.ac.jp/uniprove_pt/UnLoginAction",
        "https://webclass.ipc.hiroshima-cu.ac.jp/webclass/login.php",
        "https://unipa.hiroshima-cu.ac.jp/uprx/up/pk/pky001/Pky00101.xhtml",
        "https://www.hiroshima-cu.ac.jp/uploads/2021/09/a7edd16830d8668a634cdd7d70ea6336-20220323120932580.pdf",
        "https://www.hiroshima-cu.ac.jp/uploads/2021/09/6e728d2876a768271bbabf5d8afe95bb-20220323120933103.pdf",
        "https://twitter.com/Kino_ichidai_bc?t=mq91y5kRZ217-IfU7S86CA&s=09",
        "https://twitter.com/HCU_ichipeer?t=wNtWF7XGKF8Emhtv0fBcBQ&s=09",
        "https://www.hiroshima-cu.ac.jp/",
        "http://www.ipc.hiroshima-cu.ac.jp/",
        "https://call.lang.hiroshima-cu.ac.jp/lang/",
        "https://www.lib.hiroshima-cu.ac.jp/",
        "http://museum.hiroshima-cu.ac.jp/index.cgi/ja?page=Home",
        "http://www.career.hiroshima-cu.ac.jp/"
    )

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

        val sendBtn = fragmentView.findViewById<Button>(R.id.sendBtn)
        sendBtn.setOnClickListener {
            val title = inquiryTitleEdit.text.toString()
            val content = inquiryContentEdit.text.toString()
            Log.i("title,content", "$title,$content")
            if (title.isEmpty() || content.isEmpty()){
                AlertDialog.Builder(activity!!)
                    .setTitle("●送信失敗")
                    .setMessage("タイトルもしくは内容が入力されていません")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            } else {
                AlertDialog.Builder(activity!!)
                    .setMessage("送信しますか？")
                    .setPositiveButton("OK") { _, _ ->
                        InquiryInfo.title = title
                        InquiryInfo.content = content
                        commServer.setURL(CommServer.INQUIRY)
                        commServer.postInfo()
                        Toast.makeText(activity, "送信しました", Toast.LENGTH_SHORT).show()
                        inquiryContentEdit.text.clear()
                        inquiryTitleEdit.text.clear()
                    }
                    .setNegativeButton("キャンセル") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        }

        val addLinearLayout = fragmentView.findViewById<LinearLayout>(R.id.addLinearLayout)
        for ( (index, _) in buttonTitle.withIndex()){
            val button = Button(context)
            button.text = buttonTitle[index]
            button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            button.isAllCaps = false
            button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buttonUrl[index]))
                startActivity(intent)
            }
            addLinearLayout.addView(button)
        }

        return  fragmentView
    }

}