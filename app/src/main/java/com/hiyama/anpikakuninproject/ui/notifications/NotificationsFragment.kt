package com.hiyama.anpikakuninproject.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false)



        return fragmentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}