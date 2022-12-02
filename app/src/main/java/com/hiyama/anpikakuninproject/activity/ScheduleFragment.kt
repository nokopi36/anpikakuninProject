package com.hiyama.anpikakuninproject.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.Lecture
import com.hiyama.anpikakuninproject.data.ScheduleDB
import com.hiyama.anpikakuninproject.data.ScheduleInfo
import com.hiyama.anpikakuninproject.view.NewClassNameDialogFragment

class ScheduleFragment : Fragment() {

//    private val fileName = "Schedule.txt"
//    var file = File(requireContext().filesDir, fileName)

    private val newClassNameDialog = NewClassNameDialogFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val fragmentView = inflater.inflate(R.layout.fragment_schedule, container, false)

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

            val db = Room.databaseBuilder(
                requireContext(),
                ScheduleDB::class.java, "database-name"
            ).build()

            val classDao = db.lecturesDao()
            val lectures: List<Lecture> = classDao.getAllLectures()
        }


        return fragmentView
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