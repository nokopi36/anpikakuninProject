package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.OperationInfo
import java.net.MalformedURLException


class NewOperationDialogFragment: DialogFragment() {

    interface OperationDialogListener{
        fun onDialogPositiveClick(dialog: DialogFragment)
    }
    private var listener: OperationDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.newoperation_dialog, null)

            val buttonTitleEditText = root.findViewById<EditText>(R.id.buttonTitle)
            val urlEditText = root.findViewById<EditText>(R.id.url)

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                //追加ボタン
                .setPositiveButton("追加") { _, _ ->
                    val buttonTitle = buttonTitleEditText.text.toString()
                    val url = urlEditText.text.toString()
                    if (buttonTitle.isEmpty() || url.isEmpty()){
                        androidx.appcompat.app.AlertDialog.Builder(activity!!)
                            .setTitle("●追加失敗")
                            .setMessage("登録名もしくはURLが入力されていません")
                            .setPositiveButton("OK") { _, _ -> }
                            .show()
                    } else {
                        if (OperationInfo.buttonTitle.contains(buttonTitle) || OperationInfo.url.contains(url)){
                            androidx.appcompat.app.AlertDialog.Builder(activity!!)
                                .setTitle("●追加失敗")
                                .setMessage("名前もしくはURLがすでに登録されています")
                                .setPositiveButton("OK") { _, _ -> }
                                .show()
                        } else {
                            if (isValid(url)){
                                OperationInfo.buttonTitle.add(buttonTitle)
                                OperationInfo.url.add(url)
                            } else {
                                androidx.appcompat.app.AlertDialog.Builder(activity!!)
                                    .setTitle("●追加失敗")
                                    .setMessage("URLが無効です")
                                    .setPositiveButton("OK") { _, _ -> }
                                    .show()
                            }
                        }
                    }
                    listener?.onDialogPositiveClick(this)
                    Log.i(">>>", "Info.buttonTitle:${OperationInfo.buttonTitle}, Info.url:${OperationInfo.url}")
                }
                //キャンセルボタン
                .setNegativeButton("キャンセル") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isValid(urlString: String): Boolean {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (_: MalformedURLException) { }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            val fragment = parentFragment
            listener = fragment as OperationDialogListener
        } catch (e: ClassCastException){
            Log.e("OperationDialog", "no interface")
        }
    }

}