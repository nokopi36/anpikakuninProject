package com.hiyama.anpikakuninproject.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.runBlocking

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

        val changePasswordBtn = fragmentView.findViewById<Button>(R.id.changePasswordBtn)
        changePasswordBtn.setOnClickListener {
            val intent = Intent(activity, PasswordActivity::class.java)
            startActivity(intent)
        }

        return fragmentView
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