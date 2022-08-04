package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import kotlinx.coroutines.flow.Flow


interface Repository {

    suspend fun insertTask(task: Task)

    suspend fun insertCategory(category: Category)

    fun getTasksByDateToday(
        search: String,
        hideCompletedTask: Boolean
    ): Flow<List<Task>>

    fun getCategory(): Flow<List<Category>>

    suspend fun deleteTask(task: Task)



}
