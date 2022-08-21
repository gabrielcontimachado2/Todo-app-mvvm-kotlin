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

    /** Function to Create a task in Room database */
    fun insertTask(task: Task ) = viewModelScope.launch {
        repositoryImp.insertTask(task)
    }


    init {
        _category = repositoryImp.getCategory().asLiveData() as MutableLiveData<List<Category>>
    }



}