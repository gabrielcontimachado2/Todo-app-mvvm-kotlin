package com.bootcamp.todoeasy.ui.fragments.today


import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.lifecycle.*
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {

    /** MutableStates Flow for use in the Combine Method with Triple() and Get the last value when one or more change his value */
    private val hideCompletedTask: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var categoryName: MutableStateFlow<String> = MutableStateFlow("")
    private var searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    /** MutableLiveData to set the chip group in the Main Activity */
    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category


    /** Ui State Flow for the Month Fragment */
    private var _uiStateMonth: MutableStateFlow<TasksUiStateMonth> =
        MutableStateFlow(TasksUiStateMonth())
    val uiStateMonth: StateFlow<TasksUiStateMonth> = _uiStateMonth.asStateFlow()

    /** Ui State Flow for the Week Fragment */
    private var _uiStateWeek: MutableStateFlow<TasksUiStateWeek> =
        MutableStateFlow(TasksUiStateWeek())
    val uiStateWeek: StateFlow<TasksUiStateWeek> = _uiStateWeek.asStateFlow()

    /** Ui State Flow for the Today Fragment */
    private var _uiStateToday: MutableStateFlow<TasksUiStateToday> =
        MutableStateFlow(TasksUiStateToday())
    val uiStateToday: StateFlow<TasksUiStateToday> = _uiStateToday.asStateFlow()

    /** Ui State Flow for the Main Activity */
    private var _uiStateMain: MutableStateFlow<TaskUiStateMain> =
        MutableStateFlow(TaskUiStateMain())
    val uiStateMain: StateFlow<TaskUiStateMain> = _uiStateMain.asStateFlow()


    /** Collect the data in room for list of tasks and categories when the ViewModel Start  */
    init {

        getTaskToday()
        getTaskWeek()
        getTaskMonth()
        getCategoryList()

    }

    /** Function for Get the list Categories with the Values Emitted in Room DB */
    private fun getCategoryList() {
        _category = repositoryImp.getCategory().asLiveData() as MutableLiveData<List<Category>>
    }

    /** Function for Combine the Three StateFlow and Return the "Transformation" with Experimental "FlatMapLatest" and Collect and Set the Ui State For Today */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTaskToday() {

        viewModelScope.launch {
            // Combine Three State Flow
            combine(
                searchQuery,
                categoryName,
                hideCompletedTask
            ) { searchQuery, categorySelected, hideCompletedTask ->
                Triple(searchQuery, categorySelected, hideCompletedTask)
            }.flatMapLatest { (searchQuery, categorySelected, hideCompletedTask) ->
                // When the Category Selected not have a Value get Normal List of Task
                if (categorySelected == "") {
                    repositoryImp.getTasksByDateToday(searchQuery, hideCompletedTask)
                } else {
                    repositoryImp.getTasksByDateTodayCategory(
                        searchQuery,
                        hideCompletedTask,
                        categorySelected
                    )
                }
            }.distinctUntilChanged().collect { taskList ->
                _uiStateToday.update { uiState ->
                    uiState.copy(tasksToday = taskList)
                }
            }
        }

    }

    /** Function for Combine the Three StateFlow and Return the "Transformation" with Experimental "FlatMapLatest" and Collect and Set the Ui State For Week */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTaskWeek() {

        //Start Calendar with Locale Instance
        val calendar = Calendar.getInstance(ULocale.forLocale(Locale.getDefault()))

        //Set the Start of the Week
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startWeek = calendar.time

        //Set the End of the Week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        val endWeek = calendar.time

        viewModelScope.launch {
            combine(
                searchQuery,
                categoryName,
                hideCompletedTask
            ) { searchQuery, categorySelected, hideCompletedTask ->
                Triple(searchQuery, categorySelected, hideCompletedTask)
            }.flatMapLatest { (searchQuery, categorySelected, hideCompletedTask) ->
                if (categorySelected == "") {

                    repositoryImp.getTaskByDateWeek(
                        searchQuery,
                        hideCompletedTask,
                        startWeek,
                        endWeek
                    )

                } else {

                    repositoryImp.getTaskByDateWeekCategory(
                        searchQuery,
                        hideCompletedTask,
                        categorySelected,
                        startWeek,
                        endWeek
                    )

                }
            }.distinctUntilChanged().collect { taskList ->
                _uiStateWeek.update { uiState ->
                    uiState.copy(tasksWeek = taskList)
                }

            }
        }


    }

    /** Function for Combine the Three StateFlow and Return the "Transformation" with Experimental "FlatMapLatest" and Collect and Set the Ui State For Month */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTaskMonth() = viewModelScope.launch {
        val calendar = Calendar.getInstance(ULocale.forLocale(Locale.getDefault()))

        //Set the Start of the Month to be Use in the Query in the Room
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startMonth = calendar.time

        //Set the End of the Month to be Use in the Query in the Room
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DATE, -1)
        val endMonth = calendar.time

        viewModelScope.launch {
            combine(
                searchQuery,
                categoryName,
                hideCompletedTask
            ) { searchQuery, categorySelected, hideCompletedTask ->
                Triple(searchQuery, categorySelected, hideCompletedTask)
            }.flatMapLatest { (searchQuery, categorySelected, hideCompletedTask) ->
                if (categorySelected == "") {

                    repositoryImp.getTaskByDateMonth(
                        searchQuery,
                        hideCompletedTask,
                        startMonth,
                        endMonth
                    )

                } else {

                    repositoryImp.getTaskByDateMonthCategory(
                        searchQuery,
                        hideCompletedTask,
                        categorySelected,
                        startMonth,
                        endMonth
                    )

                }
            }.distinctUntilChanged().collect { taskList ->
                _uiStateMonth.update { uiState ->
                    uiState.copy(taskMonth = taskList)
                }
            }
        }

    }


    /** Function for Delete the task choice by user */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repositoryImp.deleteTask(task)
        }
    }

    /** Function for Set the value for filter category*/
    fun setCategoryFilter(categorySelected: String) {
        categoryName.value = categorySelected
    }

    /** Function for Set the HideCompleted Value in State Flow and in the Main data class Ui State */
    fun setHide(hideCompleted: Boolean) {

        //Data class Main
        _uiStateMain.update {
            it.copy(hideCompleted = hideCompleted)
        }

        //MutableStateFlow
        hideCompletedTask.value = hideCompleted
    }

    /** Function for Set the value when the query changed his value */
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    /** Function for Create the Task and his Category When was Deleted and user Want undone his action in Fragments (TODAY, WEEK, MONTH) */
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repositoryImp.insertTask(task)
        }
    }

    /** Function for update the Task when checked change his value */
    fun updateTaskByChecked(taskId: String, checked: Boolean) = viewModelScope.launch {
        repositoryImp.updateTaskChecked(taskId, checked)
    }

}

/** Data class Ui State for each Fragment */
data class TasksUiStateToday(
    var tasksToday: List<Task> = listOf(),
    var message: String = "",
)

data class TasksUiStateWeek(
    var tasksWeek: List<Task> = listOf(),
    var message: String = "",
)

data class TasksUiStateMonth(
    var taskMonth: List<Task> = listOf(),
    var message: String = "",
)

data class TaskUiStateMain(
    var hideCompleted: Boolean = false
)
