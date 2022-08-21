package com.bootcamp.todoeasy.ui.fragments.category.dialogEditCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {


    /** Function to Edit the categoryName in Room Database with his Id */
    fun editCategory(categoryId: Long, newCategoryName: String) {
        viewModelScope.launch { repositoryImp.updateCategoryName(categoryId, newCategoryName) }
    }

}