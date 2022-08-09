package com.bootcamp.todoeasy.ui.fragments.modal

import android.content.Context
import androidx.lifecycle.*
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import com.bootcamp.todoeasy.util.alarm.SetAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModalViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {

    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category

    /** Create a task and his category in room db */
    fun insertTask(task: Task, category: Category) = viewModelScope.launch {
        repositoryImp.insertCategory(category)
        repositoryImp.insertTask(task)
    }


    /** Create a category in room db */
    fun insertCategory(category: Category) = viewModelScope.launch {
        repositoryImp.insertCategory(category)
    }

    init {
        _category = repositoryImp.getCategory().asLiveData() as MutableLiveData<List<Category>>
    }

    fun setAlarm(
        context: Context,
        task: Task,
        alarmHour: Int,
        alarmMinute: Int,
        date: Long,
        createdTime: Long,
        interval: String
    ) {
        val setAlarm = SetAlarm(context)

        setAlarm.setAlarmTask(task, alarmHour, alarmMinute, date, createdTime, interval = "")
    }

}