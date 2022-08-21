package com.bootcamp.todoeasy.data.room


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import java.util.*
import javax.inject.Inject

class TaskDataSourceImp @Inject constructor(
    private val taskDao: TaskDao,
) : TaskDataSource {

    /** Deletes */
    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun deleteCategory(category: Category) = taskDao.deleteCategory(category)


    /** Inserts */
    override suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    override suspend fun insertCategory(category: Category) = taskDao.insertCategory(category)


    /** Gets */
    override suspend fun getTask(taskId: String) = taskDao.getTask(taskId)

    override suspend fun getTaskByCreate(created: Date) = taskDao.getTaskByCreate(created)

    override suspend fun getCategoryByName(categoryName: String) =
        taskDao.getCategoryByName(categoryName)

    override fun getCategory() = taskDao.getCategory()

    override fun getCategoryWithTask() = taskDao.getCategoryWithTask()

    override fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean,
    ) = taskDao.getTasksByDateToday(
        search,
        hideCompletedTask,
    )

    override fun getTasksByDateTodayCategory(
        search: String,
        hide: Boolean,
        categoryName: String
    ) = taskDao.getTasksByDateTodayCategory(search, hide, categoryName)

    override fun getTaskByDateWeek(
        search: String,
        hide: Boolean,
        startDayWeek: Date,
        endDayWeek: Date
    ) = taskDao.getTaskByDateWeek(search, hide, startDayWeek, endDayWeek)

    override fun getTaskByDateWeekCategory(
        search: String,
        hide: Boolean,
        categoryName: String,
        startDayWeek: Date,
        endDayWeek: Date
    ) = taskDao.getTaskByDateWeekCategory(search, hide, categoryName, startDayWeek, endDayWeek)

    override fun getTaskByDateMonth(
        search: String,
        hide: Boolean,
        startDayMonth: Date,
        endDayMonth: Date
    ) = taskDao.getTaskByDateMonth(search, hide, startDayMonth, endDayMonth)

    override fun getTaskByDateMonthCategory(
        search: String,
        hide: Boolean,
        categoryName: String,
        startDayMonth: Date,
        endDayMonth: Date
    ) = taskDao.getTaskByDateMonthCategory(search, hide, categoryName, startDayMonth, endDayMonth)


    /** Updates */
    override suspend fun updateCategoryName(categoryId: Long, newCategoryName: String) =
        taskDao.updateCategoryName(categoryId, newCategoryName)

    override suspend fun updateTaskCategory(taskId: String, categoryName: String) =
        taskDao.updateTaskCategory(taskId, categoryName)

    override suspend fun updateTaskChecked(taskId: String, status: Boolean) =
        taskDao.updateTaskChecked(taskId, status)

    override suspend fun updateTaskDueDate(taskId: String, dueDate: Date) =
        taskDao.updateTaskDueDate(taskId, dueDate)

    override suspend fun updateTaskHour(taskId: String, hour: String) =
        taskDao.updateTaskHour(taskId, hour)

    override suspend fun updateTaskTitle(taskId: String, taskTitle: String) =
        taskDao.updateTaskTitle(taskId, taskTitle)

    override suspend fun updateTaskDescription(taskId: String, taskDescription: String) =
        taskDao.updateTaskDescription(taskId, taskDescription)

    override suspend fun updateTaskPriority(taskId: String, taskPriority: Int) =
        taskDao.updateTaskPriority(taskId, taskPriority)

    override suspend fun updateTaskStatus(taskId: String, taskStatus: Boolean) =
        taskDao.updateTaskStatus(taskId, taskStatus)
}
