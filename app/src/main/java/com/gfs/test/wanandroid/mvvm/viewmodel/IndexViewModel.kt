package com.gfs.test.wanandroid.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel
import com.gfs.test.wanandroid.mvvm.repository.IndexRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class IndexViewModel : ViewModel() {

    private val mRepository by lazy { IndexRepository() }

    fun requestIndexArticlePagingData() : Flow<PagingData<IndexArticleModel.Data>> {
        return mRepository.requestIndexArticlePagingData()
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)

    }

}