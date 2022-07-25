package com.bootcamp.todoeasy.data.room


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import javax.inject.Inject

class TaskDataSourceImp @Inject constructor(
    private val taskDao: TaskDao,
) : TaskDataSource {

    override suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    override suspend fun insertCategory(category: Category) = taskDao.insertCategory(category)

    override fun getTasksByDateToday(search: String, hide: Boolean) = taskDao.getTasksByDateToday(search, hide)

    override fun getCategory() = taskDao.getCategory()



}
