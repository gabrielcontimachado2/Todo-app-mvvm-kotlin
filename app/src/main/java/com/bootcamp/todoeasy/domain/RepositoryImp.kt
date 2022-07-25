package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.room.TaskDataSourceImp
import com.bootcamp.todoeasy.util.Constants
import kotlinx.coroutines.flow.*

import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val taskDataSourceImp: TaskDataSourceImp,
) : Repository {

    override suspend fun insertTask(task: Task) = taskDataSourceImp.insertTask(task)

    override suspend fun insertCategory(category: Category) =
        taskDataSourceImp.insertCategory(category)

    override fun getCategory() = taskDataSourceImp.getCategory()

    override fun getTask(
        search: String,
        hide: Boolean,
        day: String,
    ) = when (day) {
        Constants.TODAY -> taskDataSourceImp.getTasksByDateToday(search, hide)
        Constants.WEEKLY -> TODO()
        Constants.MONTH -> TODO()
        else -> {
            flow {
                emptyList<List<Task>>()
            }
        }
    }


}
