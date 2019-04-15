package com.example.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class TaskAdapter(context: Context, var data: ArrayList<Task>) : ArrayAdapter<Task>(context, R.layout.task_view, data)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var tempView = convertView
        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            tempView = inflater.inflate(R.layout.task_view, parent, false)
        }
        tempView!!.findViewById<ImageView>(R.id.imgTaskImg).setImageResource(data[position].getImg())
        tempView.findViewById<TextView>(R.id.txTask).text = data[position].taskName
        tempView.findViewById<TextView>(R.id.txDueDate).text = data[position].taskDate.toString()
        return tempView
    }
}