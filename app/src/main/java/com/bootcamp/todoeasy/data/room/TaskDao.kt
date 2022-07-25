package com.bootcamp.todoeasy.data.room


import androidx.room.*
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM task WHERE date(date / 1000, 'unixepoch') = date ('now') AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY priority DESC ")
    fun getTasksByDateToday(search: String, hide: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM category")
    fun getCategory(): Flow<List<Category>>


}