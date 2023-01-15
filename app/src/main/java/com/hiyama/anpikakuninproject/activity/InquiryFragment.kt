package com.hiyama.anpikakuninproject.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.utils.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.InquiryInfo
import kotlinx.coroutines.runBlocking

class InquiryFragment : Fragment() {

    private val commServer = CommServer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_inquiry, container, false)

        val inquiryTitleEdit = fragmentView.findViewById<EditText>(R.id.inquiry_title)
        val inquiryContentEdit = fragmentView.findViewById<EditText>(R.id.inquiry_content)

        val sendBtn = fragmentView.findViewById<Button>(R.id.sendBtn)
        sendBtn.setOnClickListener {
            InquiryInfo.title = inquiryTitleEdit.text.toString()
            InquiryInfo.content = inquiryContentEdit.text.toString()
            postInfo()
        }

        return  fragmentView
    }

    @UiThread
    private fun postInfo(): String{
        commServer.setURL(CommServer.INQUIRY)
        var result: String
        runBlocking { // postして結果が返ってくるまで待機
            result = commServer.postInfoBackGroundRunner("UTF-8")
            Log.i("POST",result)
        }
        return result
    }

}