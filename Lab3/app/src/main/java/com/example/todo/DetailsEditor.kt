package com.example.todo

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.details_view.*
import java.time.LocalDate
import java.util.*

class DetailsEditor : AppCompatActivity() {
    private lateinit var taskName: String
    private lateinit var taskDate: String
    private var taskPriority : Int = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_view)
        taskName = intent.getStringExtra("taskName")
        taskDate = LocalDate.now().toString()
        updateImportance()
        updateDue()
    }

    fun btnChangePriotiyToLow(view: View){
        taskPriority = 1
        updateImportance()
    }
    fun btnChangePriotiyToMid(view: View){
        taskPriority = 2
        updateImportance()
    }
    fun btnChangePriotiyToHigh(view: View){
        taskPriority = 3
        updateImportance()
    }

    fun updateImportance(){
        txImportance.text = when(taskPriority) {
            1 -> "Not so important"
            2 -> "Important"
            3 -> "Very important"
            else -> ""
        }
    }

    fun updateDue(){
        txDue.text = taskDate
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.N)
    fun btnDate(view: View) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val pickDate = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, chooseYear, chooseMonth, chooseDay ->
            taskDate = LocalDate.of(chooseYear, chooseMonth+1, chooseDay).toString()
            updateDue()
        }, year, month, day)
        pickDate.show()
    }

    fun btnAccept(view: View) {
        val intent = Intent()
        intent.putExtra("taskPriority", taskPriority)
        intent.putExtra("taskName", taskName)
        intent.putExtra("taskDate", taskDate)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}