package com.example.todo

class Task (var taskName: String, var taskDate: String, var taskPriority: Int) {
    fun getImg(): Int {
        return when (this.taskPriority) {
            3 -> R.drawable.very_important    //high
            2 -> R.drawable.semi_important    //semi
            1 -> R.drawable.not_important     //low
            else -> R.drawable.not_important
        }
    }
}