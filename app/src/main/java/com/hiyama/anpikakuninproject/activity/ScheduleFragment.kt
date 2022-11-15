package com.hiyama.anpikakuninproject.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.R
import java.io.File

class ScheduleFragment : Fragment() {

//    private val fileName = "Schedule.txt"
//    var file = File(requireContext().filesDir, fileName)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false)



        return fragmentView
    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // This callback will only be called when MyFragment is at least Started.
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            // Handle the back button event
//
//        }
//
//        // The callback can be enabled or disabled here or in the lambda
//    }

}