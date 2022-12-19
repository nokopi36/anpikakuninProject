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
import android.widget.LinearLayout
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.OperationInfo
import com.hiyama.anpikakuninproject.view.NewOperationDialogFragment
import kotlinx.coroutines.runBlocking
import org.json.JSONArray

class OperationInfoFragment : Fragment() {

    val commServer = CommServer()
    private val newOperationDialog = NewOperationDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_operationinfo, container, false)
        val addLinearLayout = fragmentView.findViewById<LinearLayout>(R.id.addLinearLayout)

        OperationInfo.buttonTitle = loadButtonData("buttonTitle")
        OperationInfo.url = loadButtonData("buttonUrl")
        Log.i("shared buttonTitle", OperationInfo.buttonTitle.toString())
        Log.i("shared buttonUrl", OperationInfo.url.toString())
        addButton(addLinearLayout)

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
                addButton(addLinearLayout)
            }
        }

        val updateBtn = fragmentView.findViewById<Button>(R.id.updateBtn)
        updateBtn.setOnClickListener {
            addButton(addLinearLayout)
        }

        return fragmentView

    }

    private fun saveButtonData(key: String, arrayList: ArrayList<String>){
        val sharedPreferences = activity?.getSharedPreferences("OperationInfoButton", Context.MODE_PRIVATE)
        val jsonArray = JSONArray(arrayList)
        sharedPreferences?.edit()?.putString(key, jsonArray.toString())?.apply()
    }

    private fun loadButtonData(key: String): ArrayList<String>{
        val sharedPreferences = activity?.getSharedPreferences("OperationInfoButton", Context.MODE_PRIVATE)
        val jsonArray = JSONArray(sharedPreferences?.getString(key, "[]"))
        val arrayList = arrayListOf<String>()
        for (i in 0 until jsonArray.length()) {
            arrayList.add(jsonArray.get(i) as String)
        }
        return arrayList
    }

    private fun addButton(linearLayout: LinearLayout){
        if (OperationInfo.buttonTitle.isEmpty()){
            /* do nothing */
        } else {
            linearLayout.removeAllViews()
            for ( (index, elem) in OperationInfo.buttonTitle.withIndex()){
                val button = Button(context)
                button.text = OperationInfo.buttonTitle[index]
                button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                button.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(OperationInfo.url[index]))
                    startActivity(intent)
                }
                linearLayout.addView(button)
            }
        }
    }

    @UiThread
    private fun getInfo(): String{
        var result: String
        runBlocking {
            result = commServer.getInfoBackGroundRunner("UTF-8")
            Log.i("GET",result)
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveButtonData("buttonTitle", OperationInfo.buttonTitle)
        saveButtonData("buttonUrl", OperationInfo.url)
    }

    override fun onPause() {
        super.onPause()
        saveButtonData("buttonTitle", OperationInfo.buttonTitle)
        saveButtonData("buttonUrl", OperationInfo.url)
    }
}