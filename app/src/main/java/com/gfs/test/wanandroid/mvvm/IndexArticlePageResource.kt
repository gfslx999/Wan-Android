package com.gfs.test.wanandroid.mvvm

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gfs.test.wanandroid.mvvm.api.ArticleApiService
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel

class IndexArticlePageResource(private val articleApi: ArticleApiService) : PagingSource<Int, IndexArticleModel.Data>() {
    override fun getRefreshKey(state: PagingState<Int, IndexArticleModel.Data>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IndexArticleModel.Data> {
        return try {
            val pageIndex = params.key ?: 0
            val pageSize = params.loadSize

            val responseEntity = articleApi.requestIndexArticleList(pageIndex, pageSize)
            val items = responseEntity.data?.dataList ?: emptyList()
            val prevKey = if (pageIndex > 0) pageIndex - 1 else null
            val nextKey = if (items.isEmpty()) null else pageIndex + 1
            LoadResult.Page(items, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}