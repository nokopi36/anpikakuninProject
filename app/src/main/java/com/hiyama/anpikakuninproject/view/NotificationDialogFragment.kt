package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R

class NotificationDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.notification_dialog, null)
            val notificationTitle = root.findViewById<TextView>(R.id.notificationTitle)
            val notificationContent = root.findViewById<TextView>(R.id.notificationContent)
            val title = arguments?.getString("title", "noTitle")
            val content = arguments?.getString("content", "noContent")
            notificationTitle.text = title
            notificationContent.text = content

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                //閉じるボタン
                .setNegativeButton("閉じる") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
            this.isCancelable = false
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}