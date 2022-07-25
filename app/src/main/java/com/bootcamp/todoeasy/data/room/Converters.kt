package com.bootcamp.todoeasy.data.room


import androidx.room.TypeConverter
import java.util.*


class Converters {
    @TypeConverter
    fun fromLong(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

}
