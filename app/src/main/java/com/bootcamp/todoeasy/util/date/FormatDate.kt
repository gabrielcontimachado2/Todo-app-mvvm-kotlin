package com.bootcamp.todoeasy.util.date

import java.text.SimpleDateFormat
import java.util.*

class FormatDate{

    fun formatDate(date: Date): String{
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatLong(long: Long): String{
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(long)
    }



}