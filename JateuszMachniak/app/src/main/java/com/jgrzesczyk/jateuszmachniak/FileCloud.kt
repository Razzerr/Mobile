package com.jgrzesczyk.jateuszmachniak

class FileCloud(var fileName: String, var fileType: String, var fileCloudPath: String, var fileSize : String)  {
    fun getImg(): Int {
        return when (this.fileType) {
            "jpeg" -> R.drawable.jpg
            "plain" -> R.drawable.txt
            "mp4" -> R.drawable.mp4
            "mp3" -> R.drawable.mp3
            "doc" -> R.drawable.doc
            "dotx", "vnd.oasis.opendocument.text" -> R.drawable.dotx
            "exe" -> R.drawable.exe
            "pdf" -> R.drawable.pdf
            "zip" -> R.drawable.zip
            "unix-directory" -> R.drawable.folder
            else -> R.drawable._blank
        }
    }
}