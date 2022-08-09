package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.relantions.CategoryWithTask
import com.bootcamp.todoeasy.data.room.TaskDataSourceImp
import kotlinx.coroutines.flow.Flow
import java.util.*

import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val taskDataSourceImp: TaskDataSourceImp,
) : Repository {

    /** Deletes */
    override suspend fun deleteTask(task: Task) = taskDataSourceImp.deleteTask(task)

    /** Inserts */
    override suspend fun insertTask(task: Task) = taskDataSourceImp.insertTask(task)

    override suspend fun insertCategory(category: Category) =
        taskDataSourceImp.insertCategory(category)


    /** Gets */
    override fun getCategory() = taskDataSourceImp.getCategory()

    override suspend fun getTask(taskId: String) = taskDataSourceImp.getTask(taskId)

    override suspend fun getTaskByCreate(created: Date) = taskDataSourceImp.getTaskByCreate(created)

    override fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean,
    ) = taskDataSourceImp.getTasksByDateToday(search, hideCompletedTask)

    override suspend fun getCategoryWithTask(): List<CategoryWithTask> =
        taskDataSourceImp.getCategoryWithTask()

    override fun getTasksByDateTodayCategory(
        search: String,
        hide: Boolean,
        categoryName: String
    ) = taskDataSourceImp.getTasksByDateTodayCategory(search, hide, categoryName)

    override fun getTaskByDateWeek(
        search: String,
        hide: Boolean,
        startDayWeek: Date,
        endDayWeek: Date
    ) = taskDataSourceImp.getTaskByDateWeek(search, hide, startDayWeek, endDayWeek)

    override fun getTaskByDateMonth(
        search: String,
        hide: Boolean,
        startDayMonth: Date,
        endDayMonth: Date
    ) = taskDataSourceImp.getTaskByDateMonth(search, hide, startDayMonth, endDayMonth)

    /** Inserts */
    override suspend fun updateTaskCategory(taskId: String, categoryName: String) =
        taskDataSourceImp.updateTaskCategory(taskId, categoryName)

    override suspend fun updateTaskChecked(taskId: String, status: Boolean) =
        taskDataSourceImp.updateTaskChecked(taskId, status)

    override suspend fun updateTaskDueDate(taskId: String, dueDate: Date) =
        taskDataSourceImp.updateTaskDueDate(taskId, dueDate)

    override suspend fun updateTaskHour(taskId: String, hour: String) =
        taskDataSourceImp.updateTaskHour(taskId, hour)

}
//override fun getTask(
//    searchQuery: StateFlow<String>,
//    hideCompleted: StateFlow<Boolean>,
//    taskDay: StateFlow<String>
//): Flow<List<Task>> {

//    val _taskFlow = combine(searchQuery, hideCompleted) {

//    }

//    when (taskDay) {
//Constants.TODAY -> taskDataSourceImp.getTasksByDateToday(search, hideCompletedTask)
//Constants.WEEKLY -> TODO()
//Constants.MONTH -> TODO()
//else -> {
//    flow {
//        emptyList<List<Task>>()
//    }
//}

//    }
//}



