package com.bootcamp.todoeasy.ui.fragments.priority

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriorityViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {


    /** Function to Update the Task Priority */
    fun updateTaskPriority(taskId: String, taskPriority: Int) = viewModelScope.launch {
        repositoryImp.updateTaskPriority(taskId, taskPriority)
    }

}