package com.hiyama.anpikakuninproject.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.room.Room
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.Lecture
import com.hiyama.anpikakuninproject.data.ScheduleDB
import com.hiyama.anpikakuninproject.data.ScheduleInfo
import com.hiyama.anpikakuninproject.view.NewClassNameDialogFragment
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ScheduleFragment : Fragment() {

//    private val fileName = "Schedule.txt"
//    var file = File(requireContext().filesDir, fileName)

    private val newClassNameDialog = NewClassNameDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false)

        val db = Room.databaseBuilder(
            requireContext(),
            ScheduleDB::class.java, "schedule-db"
        ).build()

        val classDao = db.lecturesDao()

        val newClassBtn = fragmentView.findViewById<Button>(R.id.newClass)
        newClassBtn.setOnClickListener {
            parentFragment?.run {
                newClassNameDialog.show(childFragmentManager, "newClass")
            }
        }

        val updateBtn = fragmentView.findViewById<Button>(R.id.updateBtn)
        updateBtn.setOnClickListener {
            Log.i(">>>", "dayOfWeek:${ScheduleInfo.dayOfweek}, classTime:${ScheduleInfo.classTime}")
            Log.i(">>>", "className:${ScheduleInfo.className}, lectureLocation:${ScheduleInfo.lectureLocation}")

            val lecture = Lecture(0,
                ScheduleInfo.className,
                ScheduleInfo.dayOfweek,
                ScheduleInfo.classTime[0].digitToInt(),
                ScheduleInfo.lectureLocation.let{
                    if(it == ""){
                        null
                    }else {
                        it
                    }
                }
            )

            if(ScheduleInfo.className != "") {
                lifecycle.coroutineScope.launch {
                    classDao.insert(lecture)
                }
            }
        }

        val deleteBtn = fragmentView.findViewById<Button>(R.id.deleteBtn)
        deleteBtn.setOnClickListener {
            lifecycle.coroutineScope.launch{
                classDao.deleteAll()
            }
        }

        val scheduleTable = initScheduleTable(fragmentView.findViewById<TableLayout>(R.id.schedule_table_layout))

        lifecycle.coroutineScope.launch {
            classDao.getAllLectures().collect { lecturesList ->
                setDataToTable(customScheduleFormat(lecturesList), scheduleTable)

                lecturesList.forEach {
                    Log.i(">>>", "DB dayOfWeek:${it.dayOfWeek}, lectureTime:${it.lectureTime}")
                    Log.i(">>>", "DB lectureName:${it.lectureName}, lectureLocation:${it.lectureLocation}")
                }
            }
        }

        return fragmentView
    }

    private fun dpToPx(dp : Float) : Int{
        return (dp * requireContext().resources.displayMetrics.density).roundToInt()
    }

    private fun initScheduleTable(scheduleTableLayout : TableLayout) : List<List<TextView>>{
        val weekList : List<String> = listOf("", "月", "火", "水", "木", "金")
        scheduleTableLayout.addView(TableRow(requireContext()).also{
            for(day in weekList){
                val cell = TextView(requireContext()).also { textview ->
                    textview.text = day
                    textview.gravity = Gravity.CENTER
                    val layoutParams = TableRow.LayoutParams()
                    if(day != "") {
                        textview.background = requireContext().getDrawable(R.color.purple_200)
                        layoutParams.weight = 1F
                        layoutParams.width = 0
                        layoutParams.marginStart = dpToPx(0.5f)
                        layoutParams.marginEnd = dpToPx(0.5f)
                    } else {
                        layoutParams.width = dpToPx(32f)
                    }
                    textview.layoutParams = layoutParams
                }
                it.addView(cell)
            }
        })

        val scheduleTable = mutableListOf<MutableList<TextView>>()
        for(time in 1..6){
            val row = mutableListOf<TextView>()
            scheduleTableLayout.addView(TableRow(requireContext()).also{
                val rowLayoutParams = TableLayout.LayoutParams()
                rowLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                rowLayoutParams.topMargin = dpToPx(2f)
                it.layoutParams = rowLayoutParams

                it.addView(TextView(requireContext()).also { textview ->
                    textview.text = time.toString()
                    textview.background = requireContext().getDrawable(R.color.purple_200)
                    textview.gravity = Gravity.CENTER

                    val layoutParams = TableRow.LayoutParams()
                    layoutParams.width = dpToPx(32f)
                    layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                    layoutParams.gravity = Gravity.CENTER_VERTICAL
                    textview.layoutParams = layoutParams
                })

                for(date in 1..weekList.size){
                    val cell = TextView(requireContext()).also { textview ->
                        textview.text = ""
                        textview.background = requireContext().getDrawable(R.color.lemonchiffon)
                        textview.gravity = Gravity.CENTER
                        val layoutParams = TableRow.LayoutParams()
                        layoutParams.weight = 1F
                        layoutParams.width = 0
                        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
                        layoutParams.marginStart = dpToPx(1f)
                        layoutParams.marginEnd = dpToPx(1f)
                        layoutParams.gravity = Gravity.CENTER_VERTICAL
                        textview.layoutParams = layoutParams
                    }
                    row.add(cell)
                    it.addView(cell)
                }
            }, time)
            scheduleTable.add(row)
        }
        return scheduleTable
    }

    private fun customScheduleFormat(scheduleData : List<Lecture>) : List<List<Lecture>>{
        val weekList : List<String> = listOf("月曜日", "火曜日", "水曜日", "木曜日", "金曜日")
        val timeTable = mutableListOf<MutableList<Lecture>>()
        for(time in 1..6){
            val scheduleRow = mutableListOf<Lecture>()
            for(day in weekList) {
                scheduleRow.add(Lecture(0, "", "", 0, ""))
            }
            timeTable.add(scheduleRow)
        }

        for(lecture in scheduleData) {
            for (time in 1..6) {
                for (day in weekList.indices) {
                    if (lecture.dayOfWeek == weekList[day] && lecture.lectureTime == time) {
                        Log.i(">>>", "compare")
                        timeTable[time-1][day] = lecture.copy()
                    }
                }
            }
        }

        return timeTable
    }

    private fun setDataToTable(scheduleData : List<List<Lecture>>, scheduleTable : List<List<TextView>>) {
        for (rowIdx in scheduleData.indices) {
            for (itemIdx in scheduleData[rowIdx].indices) {
                val lectureLocation = scheduleData[rowIdx][itemIdx].lectureLocation ?: "未登録"

                scheduleTable[rowIdx][itemIdx].text =
                    scheduleData[rowIdx][itemIdx].lectureName + "\n" + lectureLocation
            }
        }
    }

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // This callback will only be called when MyFragment is at least Started.
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            // Handle the back button event
//
//        }
//
//        // The callback can be enabled or disabled here or in the lambda
//    }

}
