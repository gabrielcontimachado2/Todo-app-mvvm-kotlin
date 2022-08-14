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

    fun updateStatus(taskId: String, taskStatus: Boolean) = viewModelScope.launch {
        repositoryImp.updateTaskStatus(taskId, taskStatus)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repositoryImp.deleteTask(task)
    }

    fun getTaskByCreate(taskCreate: Date) {
        viewModelScope.launch {
            _task.value = repositoryImp.getTaskByCreate(taskCreate)
        }
    }

    fun updateCategory(taskId: String, categoryName: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskCategory(taskId, categoryName)
        }
    }

    fun updateTitle(taskId: String, taskTitle: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskTitle(taskId, taskTitle)
        }
    }

    fun updateDescription(taskId: String, taskDescription: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskDescription(taskId, taskDescription)
        }
    }


    fun updateDueDate(taskId: String, dueDate: Long) {
        viewModelScope.launch {
            repositoryImp.updateTaskDueDate(taskId, Date(dueDate))
        }
    }

    fun updateHour(taskId: String, hour: String): Boolean {
        return viewModelScope.launch {
            repositoryImp.updateTaskHour(taskId, hour)
        }.isCompleted
    }

    fun updateTask(taskId: String) {
        viewModelScope.launch {
            _task.value = repositoryImp.getTask(taskId)
        }
    }


}