package com.bootcamp.todoeasy.ui.activitys.detailCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.todoeasy.data.di.IoDispatcher
import com.bootcamp.todoeasy.data.models.Category
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCategoryViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp,
) : ViewModel() {

    private var _category: MutableLiveData<List<Category>> = MutableLiveData()
    val category: LiveData<List<Category>>
        get() = _category

    private val categorySelected: MutableLiveData<Category> = MutableLiveData()

    init {
        viewModelScope.launch() {
            repositoryImp.getCategory().collect { categoryList ->
                _category.postValue(categoryList)
            }
        }
    }

    /** Delete the Category But Before Get the Category in Room by your Name */
    fun deleteCategory(categorySelected: String) = viewModelScope.launch {

        val category: Category = getCategoryByName(categorySelected)

        repositoryImp.deleteCategory(category)

    }

    private suspend fun getCategoryByName(categoryName: String) =
        repositoryImp.getCategoryByName(categoryName)


}