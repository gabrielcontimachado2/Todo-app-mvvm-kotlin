package com.bootcamp.todoeasy.ui.fragments.today


import androidx.lifecycle.*
import com.bootcamp.todoeasy.data.di.IoDispatcher
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var searchTask = MutableStateFlow("")
    var hideCompletedTask = MutableStateFlow(false)
    var taskDay = MutableStateFlow("")
    var categoryRequest = MutableStateFlow("")
    val category = repositoryImp.getCategory()

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _taskFlow = combine(
        searchTask,
        hideCompletedTask,
        taskDay,
    ) { searchTask, hideCompletedTask, taskDay ->
        Triple(searchTask, hideCompletedTask, taskDay)
    }.flatMapLatest { (searchTask, hideCompletedTask, taskDay) ->
        if (categoryRequest.value.isNotEmpty()) {
            repositoryImp.getTask(searchTask, hideCompletedTask, taskDay).mapLatest { taskList ->
                taskList.filter { task ->
                    task.categoryName == categoryRequest.value
                }
            }
        } else {
            repositoryImp.getTask(searchTask, hideCompletedTask, taskDay)
        }
    }.flowOn(ioDispatcher).conflate()


    val taskFlow = _taskFlow

}




