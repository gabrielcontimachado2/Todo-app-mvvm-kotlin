package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.relantions.CategoryWithTask
import kotlinx.coroutines.flow.Flow
import java.util.*


interface Repository {

    /** Deletes */

    suspend fun deleteTask(task: Task)

    /** Inserts */
    suspend fun insertCategory(category: Category)

    suspend fun insertTask(task: Task)

    /** Gets */
    suspend fun getTask(taskId: String): Task

    suspend fun getTaskByCreate(created: Date): Task

    fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean,
    ): Flow<List<Task>>

    fun getCategory(): Flow<List<Category>>

    suspend fun getCategoryWithTask(): List<CategoryWithTask>

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
}
