package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.activity.WebActivity

class NotificationDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.notification_dialog, null)
            val notificationTitle = root.findViewById<TextView>(R.id.notificationTitle)
            val notificationContent = root.findViewById<TextView>(R.id.notificationContent)
            val notificationUrl = root.findViewById<Button>(R.id.notificationUrl)
            val title = arguments?.getString("title", "noTitle")
            val content = arguments?.getString("content", "noContent")
            val url = arguments?.getString("url", "noUrl")
            notificationTitle.text = title
            notificationContent.text = content
            if (url == "noUrl"){
                notificationUrl.visibility = View.GONE
            } else {
                notificationUrl.text = url
                notificationUrl.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    val intent = Intent(activity, WebActivity::class.java)
                    intent.putExtra("url", url)
                    startActivity(intent)
                }
            }

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