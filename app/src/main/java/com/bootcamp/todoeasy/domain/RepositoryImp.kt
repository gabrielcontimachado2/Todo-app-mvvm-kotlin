package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.room.TaskDataSourceImp

import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val taskDataSourceImp: TaskDataSourceImp,
) : Repository {

    override suspend fun insertTask(task: Task) = taskDataSourceImp.insertTask(task)

    override suspend fun insertCategory(category: Category) =
        taskDataSourceImp.insertCategory(category)

    override fun getCategory() = taskDataSourceImp.getCategory()

    override suspend fun deleteTask(task: Task) = taskDataSourceImp.deleteTask(task)

    override fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean
    ) = taskDataSourceImp.getTasksByDateToday(search, hideCompletedTask)


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



