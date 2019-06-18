package com.jgrzesczyk.jateuszmachniak

import android.os.Handler
import android.os.Message
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jgrzesczyk.jateuszmachniak.FileLogic.curFile
import com.jgrzesczyk.jateuszmachniak.FileLogic.curPath
import com.jgrzesczyk.jateuszmachniak.FileLogic.files
import com.jgrzesczyk.jateuszmachniak.FileLogic.fetchFiles
import java.lang.Exception
import java.text.DecimalFormat


class FileViewHolder (inflater: LayoutInflater, parent: ViewGroup, private var adapter: RecyclerView.Adapter<FileViewHolder>,
                      private var mText: TextView, private var outputHandler : Handler, val color: Int) :

    RecyclerView.ViewHolder(inflater.inflate(R.layout.file_view, parent, false)),
    View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View?) {
        if (files[position].fileType == "unix-directory") {
            curPath += "/" + files[position].fileName
            val thread = Thread(Runnable {
                try {
                    fetchFiles()
                } catch (e: Exception) {

                }
            })
            thread.start()
            thread.join()

            val dispStr = "$curPath/"
            mText.text = dispStr
            adapter.notifyDataSetChanged()
        } else {
            curFile = files[position].fileName
            val message = Message()
            message.obj = "Download"
            outputHandler.sendMessage(message)
        }
    }

    override fun onLongClick(v: View?): Boolean {
        curFile = files[position].fileName
        val thread = Thread(Runnable {
            val message = Message()
            try {
                message.obj = "Delete"
                outputHandler.sendMessage(message)
            } catch (e: Exception) {
                message.obj = e.message
                outputHandler.sendMessage(message)
            }
        })
        thread.start()
        return true
    }

    private var mFileName: TextView? = null
    private var mFileSize: TextView? = null
    private var mSize: TextView? = null
    private var mFileImg: ImageView? = null

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
        mFileName = itemView.findViewById(R.id.txFileName)
        mFileSize = itemView.findViewById(R.id.txFileSize)
        mFileImg = itemView.findViewById(R.id.imgFileImg)
        mSize = itemView.findViewById(R.id.txSize)
    }

    fun bind(file: FileCloud) {
        mSize?.setText(R.string.size)
        mFileName?.text = file.fileName
        mFileSize?.text = fileSize(file.fileSize)

        mSize?.setTextColor(color)
        mFileName?.setTextColor(color)
        mFileSize?.setTextColor(color)

        if (file.fileSize.toInt() <= 0) {
            mSize?.text = ""
        }
        mFileImg?.setImageResource(file.getImg())
    }

    private fun fileSize(size2: String): String {
        val size = size2.toLong()
        if (size <= 0) return ""
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / Math.pow(
                1024.0,
                digitGroups.toDouble()
            )
        ) + " " + units[digitGroups]
    }
}