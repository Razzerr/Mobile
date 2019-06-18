package com.jgrzesczyk.jateuszmachniak

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
import android.os.Environment.getExternalStoragePublicDirectory
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.jgrzesczyk.jateuszmachniak.CloudClient.getFile
import com.jgrzesczyk.jateuszmachniak.CloudClient.putFile
import com.jgrzesczyk.jateuszmachniak.FileLogic.curFile
import com.jgrzesczyk.jateuszmachniak.FileLogic.curPath
import com.jgrzesczyk.jateuszmachniak.FileLogic.fetchFiles
import com.jgrzesczyk.jateuszmachniak.FileLogic.files
import kotlinx.android.synthetic.main.activity_file_manager.*
import kotlinx.android.synthetic.main.alert_layout.view.*
import java.lang.Thread.sleep
import java.nio.file.Files
import java.util.*


class FileManagerActivity : AppCompatActivity() {
    var prefs: Prefs? = null
    private val fileUploadPickerCode = 111
    private val fileDownloadPickerCode = 112
    private val settingsCode = 100
    private lateinit var mHandler: Handler
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: FileAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = Prefs(this)

        val myLocale = Locale(prefs!!.locale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        setLayout(prefs!!.layout)

        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                when (message.obj.toString()) {
                    "Refresh" -> viewAdapter.notifyDataSetChanged()
                    "Delete" -> askOption()
                    "DeleteOK" -> {
                        val thread = Thread(Runnable {
                            try {
                                CloudClient.deleteResource("$curPath/", curFile)
                            } catch (e: Exception) {
                                val messageLoc = Message()
                                messageLoc.obj = e.message
                                mHandler.sendMessage(messageLoc)
                            }
                        })
                        thread.start()
                        thread.join()
                        getFiles()
                    }
                    "Download" -> {
                        if (checkPermission()) downloadFile()
                    }
                    "Neg" -> {}
                    else -> Toast.makeText(
                        applicationContext,
                        getString(R.string.upload_error) + message.obj,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = FileAdapter(txPath, mHandler, prefs!!)
        getFiles()

        recyclerView = findViewById<RecyclerView>(R.id.rvFiles).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        btnUpload.setOnClickListener {
            if (checkPermission()) {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent, fileUploadPickerCode)
            }
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, settingsCode)
        }

        btnNewFolder.setOnClickListener {
            val dialogBuilder: AlertDialog.Builder = when (prefs!!.layout) {
                "Light" -> AlertDialog.Builder(this,  R.style.alertDialogWhiteTheme)
                "Contrast" -> AlertDialog.Builder(this, R.style.alertDialogContrastTheme)
                else -> AlertDialog.Builder(this, R.style.alertDialogBlackTheme)
            }

            dialogBuilder.setTitle(R.string.folder_name)

            val view = layoutInflater.inflate(R.layout.alert_layout, null)

            when(prefs!!.layout) {
                "Light" -> {
                    view.cancel.setTextColor(Color.WHITE)
                    view.accept.setTextColor(Color.WHITE)
                    view.editText4.setHintTextColor(Color.WHITE)
                    view.editText4.setTextColor(Color.WHITE)
                }
                "Contrast" -> {
                    view.cancel.setTextColor(Color.BLUE)
                    view.accept.setTextColor(Color.BLUE)
                    view.editText4.setHintTextColor(Color.BLUE)
                    view.editText4.setTextColor(Color.BLUE)
                }
                else -> {
                    view.cancel.setTextColor(Color.BLACK)
                    view.accept.setTextColor(Color.BLACK)
                    view.editText4.setHintTextColor(Color.BLACK)
                    view.editText4.setTextColor(Color.BLACK)
                }
            }

            dialogBuilder.setView(view)

            val alertDialog = dialogBuilder.create()

            alertDialog.show()

            view.cancel.setOnClickListener{
                alertDialog.dismiss()
            }

            view.accept.setOnClickListener{
                val name = view.editText4.text.toString()
                val regex = Regex("[a-zA-Z0-9]+")
                if (regex.matches(name)){
                    val thread = Thread(Runnable {
                        try {
                            CloudClient.createDirectory(curPath, name)
                            getFiles()
                        } catch (e: Exception) {

                        }
                    })
                    thread.start()
                    thread.join()
                }
                else{
                    Toast.makeText(applicationContext, R.string.folder_error, Toast.LENGTH_LONG).show()
                }
                alertDialog.dismiss()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun downloadFile() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        startActivityForResult(Intent.createChooser(i, "Choose directory"), fileDownloadPickerCode)
    }

    private fun getFiles() {
        val thread = Thread(Runnable {
            try {
                fetchFiles()
            } catch (e: Exception) {

            }
        })
        thread.start()
        thread.join()
        viewAdapter.notifyDataSetChanged()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermission() : Boolean {
        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
            (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1)

            return ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        }
        return true
    }

    private fun askOption(): AlertDialog {
        val message = Message()

        val dialogBuilder: AlertDialog.Builder = when (prefs!!.layout) {
            "Light" -> AlertDialog.Builder(this,  R.style.alertDialogWhiteTheme)
            "Contrast" -> AlertDialog.Builder(this, R.style.alertDialogContrastTheme)
            else -> AlertDialog.Builder(this, R.style.alertDialogBlackTheme)
        }

        return dialogBuilder
            .setTitle(R.string.delete)
            .setMessage(getString(R.string.removal).replace("\$name", curFile))
            .setPositiveButton(R.string.delete) { dialog, _ ->
                message.obj = "DeleteOK"
                mHandler.sendMessage(message)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                message.obj = "Neg"
                mHandler.sendMessage(message)
                dialog.dismiss()
            }
            .show()
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                fileUploadPickerCode -> {
                    if (data == null) return
                    val thread = Thread(Runnable {
                        val message = Message()
                        try {
                            val path = data.data.path.substringAfter("/").substringAfter("/")
                            val file = getExternalStoragePublicDirectory(path.substringAfter(':'))
                            val noFiles = files.size
                            putFile("$curPath/", file.name.substringAfterLast('/'), file.readBytes())

                            while (noFiles == files.size) {
                                sleep(500)
                                fetchFiles()
                            }
                            message.obj = "Refresh"
                            mHandler.sendMessage(message)
                        } catch (e: Exception) {
                            message.obj = e.message
                            mHandler.sendMessage(message)
                        }
                    })
                    thread.start()
                }
                fileDownloadPickerCode -> {
                    if (data == null) return
                    val thread = Thread(Runnable {
                        try {
                            val file = getExternalStoragePublicDirectory(
                                data.data.path.substringAfter("primary:") + "/" + curFile
                            )
                            val stream = getFile("$curPath/", curFile)
                            Files.copy(stream, file.toPath())
                        } catch (e: java.lang.Exception) {
                            val message = Message()
                            message.obj = e.message
                            mHandler.sendMessage(message)
                        }
                    })
                    thread.start()
                }
                settingsCode -> if (prefs!!.reload) {
                    prefs!!.reload = false
                    val refresh = Intent(this, FileManagerActivity::class.java)
                    finish()
                    startActivity(refresh)
                }
            }
        }
        if (prefs!!.reload) {
            prefs!!.reload = false
            val refresh = Intent(this, FileManagerActivity::class.java)
            finish()
            startActivity(refresh)
        }
    }

    override fun onBackPressed() {
        if (curPath == "") {
            finish()
        }
        val splitPath = curPath.split('/')
        curPath = ""
        for (i in 1..splitPath.size - 2) {
            curPath += "/" + splitPath[i]
        }
        val dispText = "$curPath/"
        txPath.text = dispText
        getFiles()
    }


    private fun setLayout(layout: String) {
        if (layout == "Light") {
            background_fm.setBackgroundColor(Color.WHITE)
            logo_fm.setTextColor(Color.BLACK)
            btnUpload.setTextColor(Color.WHITE)
            btnSettings.setTextColor(Color.WHITE)
            btnNewFolder.setTextColor(Color.WHITE)
            txPath.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnUpload.background = this.getDrawable(R.drawable.button_circle_black)
                btnSettings.background = this.getDrawable(R.drawable.button_circle_black)
                btnNewFolder.background = this.getDrawable(R.drawable.button_circle_black)
            }
        } else if (layout == "Contrast") {
            background_fm.setBackgroundColor(Color.BLUE)
            logo_fm.setTextColor(Color.YELLOW)
            btnUpload.setTextColor(Color.BLUE)
            btnSettings.setTextColor(Color.BLUE)
            btnNewFolder.setTextColor(Color.BLUE)
            txPath.setTextColor(Color.YELLOW)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnUpload.background = this.getDrawable(R.drawable.button_circle_yellow)
                btnSettings.background = this.getDrawable(R.drawable.button_circle_yellow)
                btnNewFolder.background = this.getDrawable(R.drawable.button_circle_yellow)
            }
        } else {
            background_fm.setBackgroundColor(Color.BLACK)
            logo_fm.setTextColor(Color.WHITE)
            btnUpload.setTextColor(Color.BLACK)
            btnSettings.setTextColor(Color.BLACK)
            btnNewFolder.setTextColor(Color.BLACK)
            txPath.setTextColor(Color.WHITE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnUpload.background = this.getDrawable(R.drawable.button_circle)
                btnSettings.background = this.getDrawable(R.drawable.button_circle)
                btnNewFolder.background = this.getDrawable(R.drawable.button_circle)
            }
        }
    }
}
