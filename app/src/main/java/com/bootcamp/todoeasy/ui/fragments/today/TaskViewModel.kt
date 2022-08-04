package com.bootcamp.todoeasy.ui.fragments.today


import androidx.lifecycle.*
import com.bootcamp.todoeasy.data.di.IoDispatcher
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val categoryRequest: MutableLiveData<String> = MutableLiveData("")
    private val searchTask: MutableLiveData<String> = MutableLiveData("")
    private val hideCompletedTask: MutableLiveData<Boolean> = MutableLiveData(false)
    private var taskUpdate: MutableList<Task> = mutableListOf()

    private var _task: MutableLiveData<List<Task>> = MutableLiveData()
    val task: LiveData<List<Task>>
        get() = _task

    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category


    /** Collect the data in room for list of tasks and category's */
    init {
        viewModelScope.launch {
            repositoryImp.getTasksByDateToday(
                searchTask.value.toString(),
                hideCompletedTask.value!!
            ).collectLatest { taskList ->
                _task.postValue(taskList)
            }
        }

        _category = repositoryImp.getCategory().asLiveData() as MutableLiveData<List<Category>>
    }

    /** Delete the task choice by user */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repositoryImp.deleteTask(task)
        }
    }

    /** Set the value for filter category*/
    fun setCategoryFilter(categorySelected: String) {
        categoryRequest.value = categorySelected
    }

    /** Set the value when the query changed his value */
    fun updateSearchQuery(queryChanged: String) {
        searchTask.value = queryChanged
    }

    /** Filter the list of tasks with the category selected in main activity in chip buttons */
    fun updateTaskWithCategory() {

    }

    /** Create a task and his category in room db */
    fun insertTask(task: Task, category: Category?) = viewModelScope.launch {
        repositoryImp.insertTask(task)
        if (category != null) {
            repositoryImp.insertCategory(category)
        }
    }


//fun updateFiltersByCategoryOrAndSearch() {
//    mutableListFilterCategory.clear()
//    viewModelScope.launch {
//        repositoryImp.getTasksByDateToday(
//            searchTask.value.toString(),
//            hideCompletedTask.value!!
//        ).asLiveData().switchMap { taskList ->


//    }
//    _task.postValue(mutableListFilterCategory)
//}


}
/**  talvez usar */
//var searchTask = MutableStateFlow("")
//var hideCompletedTask = MutableStateFlow(false)
//var taskDay = MutableStateFlow("")
//var categoryRequest = MutableStateFlow("")
//val category = repositoryImp.getCategory()
//private var _taskFlow: MutableLiveData<List<Task>> = MutableLiveData()
//val taskFlow: LiveData<List<Task>> = _taskFlow


//init {
//    viewModelScope.launch {
//        repositoryImp.getTasksByDateToday(
//            searchTask.value,
//            hideCompletedTask.value
//        ).collectLatest { taskList ->
//            _taskFlow.value = taskList
//        }
//    }
//}

//@OptIn(ExperimentalCoroutinesApi::class)
//fun updateFiltersByCategoryOrAndSearch() {

//    combine(
//        searchTask,
//        hideCompletedTask,
//        categoryRequest
//    ) { searchTask, hideCompletedTask, categoryRequest ->
//        Triple(
//            searchTask,
//            hideCompletedTask,
//            categoryRequest
//        )
//    }.flatMapLatest { (searchTask, hideCompleted, categoryRequest) ->
//        if (categoryRequest.isNotEmpty()) {
//            repositoryImp.getTasksByDateToday(searchTask, hideCompleted)
//                .mapLatest { taskList ->
//                    taskList.filter { task ->
//                        task.categoryName == categoryRequest
//                    }
//                }
//        } else {
//            repositoryImp.getTasksByDateToday(searchTask, hideCompleted)
//        }
//    }.onEach { taskList ->
//        _taskFlow.postValue(taskList)
//    }.flowOn(ioDispatcher).conflate()

//}


//@OptIn(ExperimentalCoroutinesApi::class)
//_taskFlow = combine(
//searchTask,
//hideCompletedTask,
//taskDay,
//)
//{
//    searchTask, hideCompletedTask, taskDay ->
//    Triple(searchTask, hideCompletedTask, taskDay)
//}.flatMapLatest
//{
//    (searchTask, hideCompletedTask, taskDay) ->
//    if (categoryRequest.value.isNotEmpty()) {
//        repositoryImp.getTask(searchTask, hideCompletedTask, taskDay).mapLatest { taskList ->
//            taskList.filter { task ->
//                task.categoryName == categoryRequest.value
//            }
//        }
//    } else {
//        repositoryImp.getTask(searchTask, hideCompletedTask, taskDay)
//    }
//}.flowOn(ioDispatcher).conflate()






