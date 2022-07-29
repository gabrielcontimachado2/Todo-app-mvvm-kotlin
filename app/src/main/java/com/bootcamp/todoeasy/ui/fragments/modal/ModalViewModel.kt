package com.bootcamp.todoeasy.ui.fragments.modal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.models.Task
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModalViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {

    /** Create a task and his category in room db */
    fun insertTask(task: Task, category: Category) = viewModelScope.launch {
        repositoryImp.insertTask(task)
        repositoryImp.insertCategory(category)
    }

}