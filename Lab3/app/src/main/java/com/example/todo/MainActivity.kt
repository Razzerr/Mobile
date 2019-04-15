package com.example.todo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var tasks: ArrayList<Task>
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasks = ArrayList()
        adapter  = TaskAdapter(this, tasks)
        setContentView(R.layout.activity_main)
        lstTasks.adapter = adapter
    }

    fun onAddClick(v: View?){
        val intent = Intent(this, DetailsEditor::class.java)
        intent.putExtra("taskName", edTask.text.toString())
        edTask.text.clear()
        startActivityForResult(intent, 1)
        lstTasks.setOnItemLongClickListener { _, _, idx, _ -> deleteTask(idx) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val taskName = data?.getStringExtra("taskName")
            val taskPriority: Int = data?.getIntExtra("taskPriority", 0)!!
            val taskDate: String = data.getStringExtra("taskDate")
            tasks.add(Task(taskName!!, taskDate, taskPriority))
            adapter.notifyDataSetChanged()
        }
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("indexMax", tasks.size)
        var index = 1
        for (task in tasks) {
            outState?.putString("taskName$index", task.taskName)
            outState?.putInt("taskPriority$index", task.taskPriority)
            outState?.putString("taskDate$index", task.taskDate)
            index++
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val indexMax = savedInstanceState?.getInt("indexMax")
        var taskName: String
        var taskPriority: Int
        var taskDate: String
        for (i in 1..indexMax!!) {
            taskName = savedInstanceState.getString("taskName$i")!!
            taskPriority = savedInstanceState.getInt("taskPriority$i")
            taskDate = savedInstanceState.getString("taskDate$i")!!
            tasks.add(Task(taskName, taskDate, taskPriority))
        }
        adapter.notifyDataSetChanged()
    }

    private fun deleteTask(index: Int): Boolean {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Delete task")
        alert.setMessage("Are you sure you want to delete this task?")
        alert.setPositiveButton("Yes") { _, _ ->
            tasks.removeAt(index)
            adapter.notifyDataSetChanged()
        }
        alert.setNegativeButton("No") { _, _ -> }
        alert.show()
        return true
    }

    fun sortByPriority(view: View) {
        tasks.sortBy { it.taskPriority }
        adapter.notifyDataSetChanged()
    }

    fun sortByDate(view: View) {
        tasks.sortBy { it.taskDate }
        adapter.notifyDataSetChanged()
    }
}

