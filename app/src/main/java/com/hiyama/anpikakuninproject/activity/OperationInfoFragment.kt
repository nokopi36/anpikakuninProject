package com.hiyama.anpikakuninproject.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.OperationInfo
import com.hiyama.anpikakuninproject.view.NewOperationDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OperationInfoFragment : Fragment() {

    val commServer = CommServer()
    private val newOperationDialog = NewOperationDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_operationinfo, container, false)
        val addLinearLayout = fragmentView.findViewById<LinearLayout>(R.id.addLinearLayout)

        val hcuInfo = fragmentView.findViewById<Button>(R.id.hcu)
        hcuInfo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kuruken.jp/Approach?sid=8cdf9206-6a32-4ba9-8d8c-5dfdc07219ca&noribaChange=1"))
            startActivity(intent)
        }

        val numataInfo = fragmentView.findViewById<Button>(R.id.numata)
        numataInfo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kuruken.jp/Approach?sid=17a48ed6-eea0-4169-8314-04f97c6e9474&noribaChange=1"))
            startActivity(intent)
        }

        val createBtn = fragmentView.findViewById<Button>(R.id.createBtn)
        createBtn.setOnClickListener {
            parentFragment.run {
                newOperationDialog.show(childFragmentManager, "newOperation")
            }
        }

        val updateBtn = fragmentView.findViewById<Button>(R.id.updateBtn)
        updateBtn.setOnClickListener {
            for ( (index, elem) in OperationInfo.buttonTitle.withIndex()){
                val button = Button(context)
                button.text = OperationInfo.buttonTitle[index]
                addLinearLayout.addView(button)
            }
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