package com.bootcamp.todoeasy.ui.fragments.category.dialogCreateCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDialogViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {

    /** Function to create the category in Room DataBase */
    fun insertCategory(category: Category) = viewModelScope.launch {
        repositoryImp.insertCategory(category)
    }

}