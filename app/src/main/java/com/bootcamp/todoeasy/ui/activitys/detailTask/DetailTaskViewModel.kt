package com.bootcamp.todoeasy.ui.activitys.detailTask

import androidx.lifecycle.ViewModel
import com.bootcamp.todoeasy.domain.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val repositoryImp: RepositoryImp
) : ViewModel() {



}