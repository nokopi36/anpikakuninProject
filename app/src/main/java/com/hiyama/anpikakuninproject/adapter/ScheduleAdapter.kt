package com.hiyama.anpikakuninproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.Lecture

class ScheduleAdapter :
        ListAdapter<Lecture, ScheduleAdapter.ViewHolder>(DiffCallback) {

    companion object{
        private val DiffCallback = object  : DiffUtil.ItemCallback<Lecture>(){
            override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean{
                return oldItem.lectureName == newItem.lectureName
            }

            override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(view: View) :RecyclerView.ViewHolder(view){
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textView)
        }

        fun bind(lecture: Lecture){
            textView.text = lecture.lectureName
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lecture_cell, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }
}