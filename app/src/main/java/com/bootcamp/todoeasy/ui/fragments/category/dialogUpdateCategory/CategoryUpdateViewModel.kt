package com.bootcamp.todoeasy.ui.fragments.category.dialogUpdateCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryUpdateViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
): ViewModel(){

    /** Function to update the Category Name in the Task */
    fun updateTaskCategory(taskId: String, categoryName: String) {
        viewModelScope.launch {
            repositoryImp.updateTaskCategory(taskId, categoryName)
        }
    }

    /** Function to Create new Category */
    fun insertCategory(category: Category) = viewModelScope.launch {
        repositoryImp.insertCategory(category)
    }

}