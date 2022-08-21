package com.bootcamp.todoeasy.data.room


import androidx.room.*
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.data.relations.CategoryWithTask
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface TaskDao {

    /** Delete */
    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteCategory(category: Category)


    /** Inserts */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)


    /** Gets */
    @Query("SELECT * FROM category")
    fun getCategory(): Flow<List<Category>>

    @Query("SELECT * FROM task WHERE idTask=:taskId")
    suspend fun getTask(taskId: String): Task

    @Query("SELECT * FROM task WHERE created =:created")
    suspend fun getTaskByCreate(created: Date): Task

    @Query("SELECT * FROM category WHERE categoryName =:categoryName")
    suspend fun getCategoryByName(categoryName: String): Category

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoryWithTask(): Flow<List<CategoryWithTask>>

    @Query("SELECT * FROM task WHERE date(date / 1000, 'unixepoch', 'localtime') = date ('now', 'localtime') AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY priority DESC ")
    fun getTasksByDateToday(search: String, hide: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date(date / 1000, 'unixepoch', 'localtime') = date ('now', 'localtime') AND categoryName =:categoryName AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY priority DESC ")
    fun getTasksByDateTodayCategory(
        search: String,
        hide: Boolean,
        categoryName: String
    ): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date/1000 BETWEEN :startDayWeek/1000 AND :endDayWeek/1000 AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY priority DESC ")
    fun getTaskByDateWeek(
        search: String,
        hide: Boolean,
        startDayWeek: Date,
        endDayWeek: Date
    ): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date/1000 BETWEEN :startDayWeek/1000 AND :endDayWeek/1000 AND categoryName =:categoryName AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY priority DESC ")
    fun getTaskByDateWeekCategory(
        search: String,
        hide: Boolean,
        categoryName: String,
        startDayWeek: Date,
        endDayWeek: Date
    ): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date/1000 BETWEEN :startDayMonth/1000 AND :endDayMonth/1000 AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY date ASC, priority DESC ")
    fun getTaskByDateMonth(
        search: String,
        hide: Boolean,
        startDayMonth: Date,
        endDayMonth: Date
    ): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE date/1000 BETWEEN :startDayMonth/1000 AND :endDayMonth/1000 AND categoryName =:categoryName AND (status != :hide OR status = 0) AND name LIKE '%' || :search || '%' ORDER BY date ASC, priority DESC ")
    fun getTaskByDateMonthCategory(
        search: String,
        hide: Boolean,
        categoryName: String,
        startDayMonth: Date,
        endDayMonth: Date
    ): Flow<List<Task>>


    /** Updates */
    @Query("UPDATE category SET categoryName =:newCategoryName WHERE idCategory =:categoryId")
    suspend fun updateCategoryName(categoryId: Long, newCategoryName: String)

    @Query("UPDATE task SET categoryName =:categoryName WHERE idTask =:taskId")
    suspend fun updateTaskCategory(taskId: String, categoryName: String)

    @Query("UPDATE task SET status =:status WHERE idTask =:taskId")
    suspend fun updateTaskChecked(taskId: String, status: Boolean)

    @Query("UPDATE task SET date =:dueDate WHERE idTask =:taskId")
    suspend fun updateTaskDueDate(taskId: String, dueDate: Date)

    @Query("UPDATE task SET hour =:hour WHERE idTask =:taskId")
    suspend fun updateTaskHour(taskId: String, hour: String)

    @Query("UPDATE task SET name =:taskTitle WHERE idTask =:taskId")
    suspend fun updateTaskTitle(taskId: String, taskTitle: String)

    @Query("UPDATE task SET description =:taskDescription WHERE idTask =:taskId")
    suspend fun updateTaskDescription(taskId: String, taskDescription: String)

    @Query("UPDATE task SET priority =:taskPriority WHERE idTask =:taskId")
    suspend fun updateTaskPriority(taskId: String, taskPriority: Int)

    @Query("UPDATE task SET status =:taskStatus WHERE idTask =:taskId")
    suspend fun updateTaskStatus(taskId: String, taskStatus: Boolean)


}