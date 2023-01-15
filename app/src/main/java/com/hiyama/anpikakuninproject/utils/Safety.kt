package com.hiyama.anpikakuninproject.utils

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.SafetyCheckInfo
import com.hiyama.anpikakuninproject.view.SafetyCheckDialogFragment
import java.net.HttpURLConnection

class Safety {

    private val commServer = CommServer()
    private val safetyCheckDialog = SafetyCheckDialogFragment()

    fun safetyCheck(fragmentManager: FragmentManager){ // 安否確認用の関数
        commServer.setURL(CommServer.SAFETY_CHECK)
        val result = commServer.getInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("Return Val From Server", "Value: $result")
            val safetyCheck = JsonParser.safetyCheckParse(result)
            SafetyCheckInfo.initialize(safetyCheck!!)
//            if (SafetyCheckInfo.check == "True"){
            if (true){
                safetyCheckDialog.show(fragmentManager, "safetyCheck")
            } else {
                /* do nothing */
            }
        }
    }

}