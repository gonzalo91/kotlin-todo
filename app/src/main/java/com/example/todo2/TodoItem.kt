package com.example.todo2

import java.util.*

class TodoItem (var name : String){

    var isUrgent = false;
    var date = Calendar.getInstance()
    var taskDate = ""

    constructor(name: String, isUrgent: Boolean, taskDate : String) : this(name) {
        this.isUrgent = isUrgent
        this.taskDate = taskDate
    }

    fun isUrgent() : Int{
        return if( isUrgent ) 1 else 0
    }



    fun getDateAsString() : String{
        val year = date.get(Calendar.YEAR).toString()
        val month = date.get(Calendar.MONTH).toString()
        val day = date.get(Calendar.DAY_OF_MONTH).toString()

        return "$day/$month/$year"
    }

}