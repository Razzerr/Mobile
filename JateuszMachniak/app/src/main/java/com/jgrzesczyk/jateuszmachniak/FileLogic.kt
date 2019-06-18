package com.jgrzesczyk.jateuszmachniak

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

object FileLogic {
    var files: ArrayList<FileCloud> = ArrayList()
    var curPath = ""
    var curFile = ""

    fun fetchFiles() {
        files.clear()
        try{
            val cloudFiles = CloudClient.getList(curPath)
            cloudFiles?.removeAt(0)
            for (file in cloudFiles!!) {
                files.add(FileCloud(file.name, file.contentType.split('/').last(), file.path, file.contentLength.toString()))
            }
        } catch (E : Exception){
            Log.d("Debug connection", E.message)
        }
    }
}
