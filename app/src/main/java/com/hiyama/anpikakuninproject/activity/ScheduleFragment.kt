package com.hiyama.anpikakuninproject.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.room.Room
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.adapter.GridItemDecoration
import com.hiyama.anpikakuninproject.adapter.ScheduleAdapter
import com.hiyama.anpikakuninproject.adapter.ScheduleLabelAdapter
import com.hiyama.anpikakuninproject.data.Lecture
import com.hiyama.anpikakuninproject.data.ScheduleDB
import com.hiyama.anpikakuninproject.data.ScheduleInfo
import com.hiyama.anpikakuninproject.view.NewClassNameDialogFragment
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {

//    private val fileName = "Schedule.txt"
//    var file = File(requireContext().filesDir, fileName)

    private val newClassNameDialog = NewClassNameDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false)

        val recyclerview = fragmentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.addItemDecoration(GridItemDecoration())
        val gridLayoutManager = GridLayoutManager(requireContext(), 6, GridLayoutManager.HORIZONTAL, false)
        val scheduleLabelAdapter = ScheduleLabelAdapter()
        val scheduleAdapter = ScheduleAdapter()
        val concatAdapter = ConcatAdapter().apply{
            addAdapter(scheduleLabelAdapter)
            addAdapter(scheduleAdapter)
        }
        recyclerview.layoutManager = gridLayoutManager
        recyclerview.adapter = concatAdapter

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

            val lecture = Lecture(0, ScheduleInfo.className, ScheduleInfo.dayOfweek, ScheduleInfo.classTime[0].digitToInt(), ScheduleInfo.lectureLocation)

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

        scheduleLabelAdapter.submitList(listOf("1", "2", "3", "4", "5", "6"))

        lifecycle.coroutineScope.launch {
            classDao.getAllLectures().collect { lecturesList ->
                scheduleAdapter.submitList(customScheduleFormat(lecturesList))

                lecturesList.forEach {
                    Log.i(">>>", "DB dayOfWeek:${it.dayOfWeek}, lectureTime:${it.lectureTime}")
                    Log.i(">>>", "DB lectureName:${it.lectureName}, lectureLocation:${it.lectureLocation}")
                }
            }
        }

        return fragmentView
    }

    private fun customScheduleFormat(scheduleData : List<Lecture>) : List<Lecture>{
        val weekList : List<String> = listOf("月曜日", "火曜日", "水曜日", "木曜日", "金曜日")
        val timeTable = mutableListOf<Lecture>()
        for(i in 1..weekList.size*6){
            timeTable.add(Lecture(0,"","",0,""))
        }

        for(lecture in scheduleData) {
            for (day in weekList.indices) {
                for (time in 1..6) {
                    if(lecture.dayOfWeek==weekList[day] && lecture.lectureTime==time) {
                        Log.i(">>>", "compare")
                        timeTable[day * 6 + time-1] = lecture.copy()
                    }
                }
            }
        }
        return timeTable
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
