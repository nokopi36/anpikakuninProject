package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.OperationInfo

class OperationDeleteBtnDialogFragment: DialogFragment() {

    interface OperationDeleteBtnDialogListener{
        fun onDeleteBtnDialogPositiveClick(dialog: DialogFragment)
    }
    private var listener: OperationDeleteBtnDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.operation_deletbtn_dialog, null)

            val allButtons = root.findViewById<LinearLayout>(R.id.allButtons)

            val deleteBtnTitleList = arrayListOf<String>()
            val deleteBtnUrlList = arrayListOf<String>()

            if (OperationInfo.buttonTitle.isEmpty()){
                /* do nothing */
            } else {
                allButtons.removeAllViews()
                for ( (index, _) in OperationInfo.buttonTitle.withIndex()){
                    val checkBox = CheckBox(context)
                    checkBox.text = OperationInfo.buttonTitle[index]
                    checkBox.setOnClickListener {
                        if (checkBox.isChecked){
                            deleteBtnTitleList.add(OperationInfo.buttonTitle[index])
                            deleteBtnUrlList.add(OperationInfo.url[index])
                            Log.i("checkBox true", "remove$index")
                        } else {
                            deleteBtnTitleList.remove(OperationInfo.buttonTitle[index])
                            deleteBtnUrlList.remove(OperationInfo.url[index])
                            Log.i("checkBox false", "add$index")
                        }
                    }
                    allButtons.addView(checkBox)
                }
            }

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                //追加ボタン
                .setPositiveButton("削除") { _, _ ->
                    Log.i("deleteBtnList", deleteBtnTitleList.toString())
                    if (deleteBtnTitleList.isEmpty()){
                        /* do nothing */
                    } else {
                        for ((index, _) in deleteBtnTitleList.withIndex()){
                            Log.i("buttonTitle", OperationInfo.buttonTitle.toString())
                            OperationInfo.buttonTitle.remove(deleteBtnTitleList[index])
                            Log.i("delete buttonTitle", deleteBtnTitleList[index])
                            OperationInfo.url.remove(deleteBtnUrlList[index])
                            Log.i("delete url", deleteBtnUrlList[index])
                        }
                    }
                    listener?.onDeleteBtnDialogPositiveClick(this)
                }
                //キャンセルボタン
                .setNegativeButton("キャンセル") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            val fragment = parentFragment
            listener = fragment as OperationDeleteBtnDialogListener
        } catch (e: ClassCastException){
            Log.e("OperationDialog", "no interface")
        }
    }

}