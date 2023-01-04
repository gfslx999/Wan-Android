package com.gfs.test.wanandroid.mvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.gfs.helper.common.base.paging.BasePagingSource
import com.gfs.helper.common.network.BaseRepository
import com.gfs.test.base.constant.ConfigConstant
import com.gfs.test.base.util.AppUtil
import com.gfs.test.wanandroid.mvvm.api.ArticleApiService
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IndexRepository : BaseRepository() {

    private val mArticleApi by lazy { createApi<ArticleApiService>() }

    fun requestIndexArticlePagingData(pageSize: Int = ConfigConstant.DEFAULT_PAGE_SIZE) : Flow<PagingData<IndexArticleModel.Data>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { ArticlePageResource() }
        ).flow
    }

    inner class ArticlePageResource : BasePagingSource<IndexArticleModel.Data>() {
        override suspend fun onRequest(pageIndex: Int, pageSize: Int): List<IndexArticleModel.Data>? {
            return mArticleApi.requestIndexArticleList(pageIndex, pageSize).data?.dataList
        }
    }
}