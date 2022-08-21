package com.bootcamp.todoeasy.ui.activitys.detailCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.data.relations.CategoryWithTask
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCategoryViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp,
) : ViewModel() {

    /** UiState for Detail Category Activity */
    private var _uiStateCategory: MutableStateFlow<CategoryUiState> =
        MutableStateFlow(CategoryUiState())
    val uiStateCategory: StateFlow<CategoryUiState>
        get() = _uiStateCategory.asStateFlow()


    init {
        getCategory()
    }


    /** Get the Categories in Room and set the uiState */
    fun getCategory() {
        viewModelScope.launch {

            repositoryImp.getCategoryWithTask()
                .collect { listCategoryWithTask ->
                    _uiStateCategory.update { categoryUiState ->
                        categoryUiState.copy(categoryWithTask = listCategoryWithTask)
                    }
                }
        }
    }

    /** Delete the Category But Before Get the Category in Room by your Name */
    fun deleteCategory(category: Category) {

        viewModelScope.launch {
            repositoryImp.deleteCategory(category)
        }
    }

}

data class CategoryUiState(
    var categoryWithTask: List<CategoryWithTask> = listOf()
)