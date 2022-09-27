package com.hiyama.anpikakuninproject.ui.operationinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.R

class OperationInfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_operationinfo, container, false)



        return fragmentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}