package com.bootcamp.todoeasy.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task

@Database(
    entities = [
        Task::class,
        Category::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}