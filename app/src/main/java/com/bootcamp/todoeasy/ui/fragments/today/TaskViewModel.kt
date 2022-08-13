package com.bootcamp.todoeasy.ui.fragments.today


import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.icu.util.ULocale
import androidx.lifecycle.*
import com.bootcamp.todoeasy.data.di.IoDispatcher
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val categoryRequest: MutableLiveData<String> = MutableLiveData("")
    val searchTask: MutableLiveData<String> = MutableLiveData("")
    private val hideCompletedTask: MutableLiveData<Boolean> = MutableLiveData(false)


    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category


    /** Task Today */
    private var _taskToday: MutableLiveData<List<Task>> = MutableLiveData()
    val taskToday: LiveData<List<Task>> = Transformations.switchMap(categoryRequest) { it ->
        if (it != "") {
            repositoryImp.getTasksByDateTodayCategory(
                searchTask.value.toString(),
                hideCompletedTask.value!!,
                it
            ).asLiveData()
        } else {
            repositoryImp.getTasksByDateToday(
                searchTask.value.toString(),
                hideCompletedTask.value!!
            ).asLiveData()
        }
    }

    /** Task Weekly */
    private var _taskWeekly: MutableLiveData<List<Task>> = MutableLiveData()
    val taskWeekly: LiveData<List<Task>>
        get() = _taskWeekly

    /** Task Month */
    private var _taskMonth: MutableLiveData<List<Task>> = MutableLiveData()
    val taskMonth: LiveData<List<Task>>
        get() = _taskMonth


    /** Collect the data in room for list of tasks and category's */
    init {


        getTaskWeek()
        getTaskMonth()

        _category = repositoryImp.getCategory().asLiveData() as MutableLiveData<List<Category>>
    }


    private fun getTaskToday() = viewModelScope.launch {


    }

    fun task(): List<Task> {

        val listFilter = mutableListOf<Task>()

        viewModelScope.launch {
            repositoryImp.getTasksByDateTodayCategory(
                searchTask.value.toString(),
                hideCompletedTask.value!!,
                categoryRequest.value.toString()
            ).collectLatest {
                listFilter.addAll(it)
            }
        }

        return listFilter.toList()
    }

    private fun getTaskWeek() = viewModelScope.launch {
        val calendar = Calendar.getInstance(ULocale.forLocale(Locale.getDefault()))


        calendar.firstDayOfWeek = Calendar.MONDAY

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startWeek = calendar.time

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val endWeek = calendar.time

        repositoryImp.getTaskByDateWeek(
            searchTask.value.toString(),
            hideCompletedTask.value!!,
            startWeek,
            endWeek
        ).collect {
            _taskWeekly.postValue(it)
        }

    }

    private fun getTaskMonth() = viewModelScope.launch {
        val calendar = Calendar.getInstance(ULocale.forLocale(Locale.getDefault()))


        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startMonth = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DATE, -1)
        val endMonth = calendar.time

        repositoryImp.getTaskByDateMonth(
            searchTask.value.toString(),
            hideCompletedTask.value!!,
            startMonth,
            endMonth
        ).collect {
            _taskMonth.postValue(it)
        }

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
    fun updateTaskWithCategory() = viewModelScope.launch {

        val listFilter: MutableList<Task> = mutableListOf()

        if (categoryRequest.value.toString() != null) {
            repositoryImp.getTasksByDateTodayCategory(
                searchTask.value.toString(),
                hideCompletedTask.value!!,
                categoryRequest.value.toString()
            ).collect {
                listFilter.addAll(it)
            }
        } else {
            repositoryImp.getTasksByDateToday(
                searchTask.value.toString(),
                hideCompletedTask.value!!,
            ).collect {
                listFilter.addAll(it)
            }
        }

        _taskToday.postValue(listFilter)
    }

    /** Create a task and his category in room db */
    fun insertTask(task: Task, category: Category?) = viewModelScope.launch {
        repositoryImp.insertTask(task)
        if (category != null) {
            repositoryImp.insertCategory(category)
        }
    }

    fun updateTaskByChecked(taskId: String, checked: Boolean) = viewModelScope.launch {
        repositoryImp.updateTaskChecked(taskId, checked)
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






