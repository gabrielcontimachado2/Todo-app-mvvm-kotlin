package com.bootcamp.todoeasy.data.room


import androidx.room.Query
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.relantions.CategoryWithTask
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TaskDataSource {

    /** Delete */
    suspend fun deleteTask(task: Task)

    suspend fun deleteCategory(category: Category)

    /** Inserts */
    suspend fun insertTask(task: Task)

    suspend fun insertCategory(category: Category)


    /** Gets */
    suspend fun getTask(taskId: String): Task

    suspend fun getTaskByCreate(created: Date): Task

    suspend fun getCategoryByName(categoryName: String): Category

    fun getCategory(): Flow<List<Category>>

    suspend fun getCategoryWithTask(): List<CategoryWithTask>

    fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean,
    ): Flow<List<Task>>

    fun getTasksByDateTodayCategory(
        search: String,
        hide: Boolean,
        categoryName: String
    ): Flow<List<Task>>

    fun getTaskByDateWeek(
        search: String,
        hide: Boolean,
        startDayWeek: Date,
        endDayWeek: Date
    ): Flow<List<Task>>

    fun getTaskByDateMonth(
        search: String,
        hide: Boolean,
        startDayMonth: Date,
        endDayMonth: Date
    ): Flow<List<Task>>


    /** Updates */
    suspend fun updateTaskCategory(taskId: String, categoryName: String)

    suspend fun updateTaskChecked(taskId: String, status: Boolean)

    suspend fun updateTaskDueDate(taskId: String, dueDate: Date)

    suspend fun updateTaskHour(taskId: String, hour: String)

    suspend fun updateTaskTitle(taskId: String, taskTitle: String)

    suspend fun updateTaskDescription(taskId: String, taskDescription: String)

}