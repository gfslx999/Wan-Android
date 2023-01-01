package com.gfs.test.wanandroid.mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gfs.helper.common.network.BaseRepository
import com.gfs.test.wanandroid.mvvm.IndexArticlePageResource
import com.gfs.test.wanandroid.mvvm.api.ArticleApiService
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel
import kotlinx.coroutines.flow.Flow

class IndexRepository : BaseRepository() {

    private val mArticleApi by lazy { createApi<ArticleApiService>() }

    suspend fun requestIndexArticleList(pageIndex: Int, pageSize: Int = 10) = doRequest {
        mArticleApi.requestIndexArticleList(pageIndex, pageSize)
    }

    fun requestIndexArticlePagingData(pageIndex: Int, pageSize: Int = 10) : Flow<PagingData<IndexArticleModel.Data>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { IndexArticlePageResource(mArticleApi) }
        ).flow
    }

}