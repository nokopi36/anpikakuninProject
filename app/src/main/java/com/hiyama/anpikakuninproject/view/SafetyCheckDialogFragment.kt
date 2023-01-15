package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import java.time.LocalDateTime

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
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/mSQw4tCZaPM4ymAr6"))
                    startActivity(intent)
                }
                //閉じるボタン
                .setNegativeButton("閉じる") { dialog, _ ->
                    //dialogを閉じる
                    isShowChecked(isShowCheck.isChecked)
                    dialog.cancel()
                }
                this.isCancelable = false
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isShowChecked(flag: Boolean){
        if (flag) {
            val now = LocalDateTime.now()
//            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
//            val now = LocalDateTime.parse("2023/01/13 17:00:00", dtf)
            val sharedPreferences = activity?.getSharedPreferences("safetyCheckIsShowed", Context.MODE_PRIVATE)
            sharedPreferences?.edit()?.putString("nowTime", now.toString())?.apply()
            Log.i("nowTime", now.toString())
        }
    }

}