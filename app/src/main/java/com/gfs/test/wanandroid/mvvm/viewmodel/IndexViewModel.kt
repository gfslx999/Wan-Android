package com.gfs.test.wanandroid.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gfs.helper.common.expand.launch
import com.gfs.test.wanandroid.mvvm.IndexArticlePageResource
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel
import com.gfs.test.wanandroid.mvvm.repository.IndexRepository
import kotlinx.coroutines.flow.Flow

class IndexViewModel : ViewModel() {

    private val mRepository by lazy { IndexRepository() }

    fun requestIndexArticlePagingData() : Flow<PagingData<IndexArticleModel.Data>> {
        return mRepository.requestIndexArticlePagingData(0).cachedIn(viewModelScope)
    }

}