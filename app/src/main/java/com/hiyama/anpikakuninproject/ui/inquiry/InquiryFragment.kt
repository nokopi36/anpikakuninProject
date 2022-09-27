package com.hiyama.anpikakuninproject.ui.inquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hiyama.anpikakuninproject.R

class InquiryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_inquiry, container, false)

//        val mPushBtm = fragmentView.findViewById<Button>(R.id.pushBtn)
//        val pushText = fragmentView.findViewById<TextView>(R.id.text)
//        mPushBtm.setOnClickListener {
//            pushText.text = "iiiii"
//        }

        return  fragmentView
    }

}