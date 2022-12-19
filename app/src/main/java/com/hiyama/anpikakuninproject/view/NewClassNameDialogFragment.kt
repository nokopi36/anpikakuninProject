package com.hiyama.anpikakuninproject.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.ScheduleInfo

class NewClassNameDialogFragment: DialogFragment() {
    private val dayOfWeekSpinner = arrayOf(
        "月曜日",
        "火曜日",
        "水曜日",
        "木曜日",
        "金曜日"
    )

    private val classTimeSpinner = arrayOf(
        "1限",
        "2限",
        "3限",
        "4限",
        "5限",
        "6限"
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            //Dialogレイアウトにviewを取得
            val inflater = requireActivity().layoutInflater
            val root = inflater.inflate(R.layout.newclassname_dialog, null)

            val classNameEditText = root.findViewById<EditText>(R.id.className)
            val lectureLocationEditText = root.findViewById<EditText>(R.id.lectureLocation)

            val dayOfWeek = root.findViewById<Spinner>(R.id.dayOfWeek)
            val dayOfWeekAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, dayOfWeekSpinner)
            dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dayOfWeek.adapter = dayOfWeekAdapter
            dayOfWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val text = p0?.selectedItem as String
                    ScheduleInfo.dayOfweek = text
                    Log.i("itemSelected", text)

                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* do nothing */
                }
            }

            val classTime = root.findViewById<Spinner>(R.id.classTime)
            val classTimeAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, classTimeSpinner)
            classTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            classTime.adapter = classTimeAdapter
            classTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val text = p0?.selectedItem as String
                    ScheduleInfo.classTime = text
                    Log.i("itemSelected", text)

                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* do nothing */
                }
            }

            //DialogBuilderにdialogのviewをセット
            builder.setView(root)
                //登録ボタン
                .setPositiveButton("登録") { _, _ ->
                    val className = classNameEditText.text.toString()
                    val lectureLocation = lectureLocationEditText.text.toString()
                    ScheduleInfo.className = className
                    ScheduleInfo.lectureLocation = lectureLocation
                    Log.i(">>>", "className:$className, lectureLocation:$lectureLocation")
                    Log.i(">>>", "Info.className:${ScheduleInfo.className}, Info.lectureLocation:${ScheduleInfo.lectureLocation}")
                }
                //キャンセルボタン
                .setNegativeButton("キャンセル") { dialog, _ ->
                    //dialogを閉じる
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}