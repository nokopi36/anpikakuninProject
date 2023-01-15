package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R

class SafetyCheckDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.safetycheck_dialog, null)
            val isShowCheck = root.findViewById<CheckBox>(R.id.isShowCheck)

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                .setTitle("安否確認(訓練)")
                .setMessage("あなたの安否を報告してください")
                //報告ボタン
                .setPositiveButton("報告する") { _, _ ->
                    isShowChecked(isShowCheck.isChecked)
                }
                //閉じるボタン
                .setNegativeButton("閉じる") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
                this.isCancelable = false
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isShowChecked(flag: Boolean){

    }

}