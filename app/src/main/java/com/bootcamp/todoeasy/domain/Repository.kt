package com.bootcamp.todoeasy.domain


import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


interface Repository {


    suspend fun insertTask(task: Task)

    suspend fun insertCategory(category: Category)

    fun getTask(search: String, hide: Boolean, day: String): Flow<List<Task>>

    fun getCategory(): Flow<List<Category>>
}
