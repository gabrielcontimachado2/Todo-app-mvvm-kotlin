package com.bootcamp.todoeasy.ui.activitys.detailTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {


    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category

    private var _task: MutableLiveData<Task> = MutableLiveData()
    val task: LiveData<Task>
        get() = _task


    init {
        viewModelScope.launch {
            repositoryImp.getCategory().collectLatest {
                _category.postValue(it)
            }
        }
    }


    /** Function for delete the task */
    fun deleteTask(task: Task) = viewModelScope.launch {
        repositoryImp.deleteTask(task)
    }

    /** Function to update the Status in Task */
    fun updateStatus(taskId: String, taskStatus: Boolean) = viewModelScope.launch {
        repositoryImp.updateTaskStatus(taskId, taskStatus)
    }

    /** Function to update the Category in Task */
    fun updateCategory(taskId: String, categoryName: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskCategory(taskId, categoryName)
        }
    }

    /** Function to update the title in Task */
    fun updateTitle(taskId: String, taskTitle: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskTitle(taskId, taskTitle)
        }
    }

    /** Function to update the Description in Task */
    fun updateDescription(taskId: String, taskDescription: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskDescription(taskId, taskDescription)
        }
    }

    /** Function to update the Due Date in Task */
    fun updateDueDate(taskId: String, dueDate: Long) {
        viewModelScope.launch {
            repositoryImp.updateTaskDueDate(taskId, Date(dueDate))
        }
    }

    /** Function to update the Hour in Task */
    fun updateHour(taskId: String, hour: String): Boolean {
        return viewModelScope.launch {
            repositoryImp.updateTaskHour(taskId, hour)
        }.isCompleted
    }

    /** Function to update the Task LiveData  */
    fun updateTask(taskId: String) {
        viewModelScope.launch {
            _task.value = repositoryImp.getTask(taskId)
        }
    }


}