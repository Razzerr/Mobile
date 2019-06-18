package com.jgrzesczyk.jateuszmachniak

import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class FileAdapter(var textView: TextView, var inputHandler: Handler, var prefs : Prefs) :
    RecyclerView.Adapter<FileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val color = getColor(prefs.layout)
        return FileViewHolder(inflater, parent, this, textView, inputHandler, color)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file: FileCloud = FileLogic.files[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int = FileLogic.files.size

    private fun getColor(color: String): Int {
        return when (color) {
            "Light" -> Color.BLACK
            "Contrast" -> Color.YELLOW
            else -> Color.WHITE
        }
    }
}