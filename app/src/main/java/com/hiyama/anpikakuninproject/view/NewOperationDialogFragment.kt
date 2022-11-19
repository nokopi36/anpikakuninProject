package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.OperationInfo
import com.hiyama.anpikakuninproject.data.ScheduleInfo

class NewOperationDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater;
            val root = inflater.inflate(R.layout.newoperation_dialog, null)

            val buttonTitleEditText = root.findViewById<EditText>(R.id.buttonTitle)
            val urlEditText = root.findViewById<EditText>(R.id.url)

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                //OKボタン
                .setPositiveButton("OK") { _, _ ->
                    val buttonTitle = buttonTitleEditText.text.toString()
                    val url = urlEditText.text.toString()
                    OperationInfo.buttonTitle = buttonTitle
                    OperationInfo.url = url
                    Log.i(">>>", "Info.buttonTitle:${OperationInfo.buttonTitle}, Info.url:${OperationInfo.url}")
                }
                //cancelボタン
                .setNegativeButton("CANCEL") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}