package com.gfs.helper.common.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gfs.test.base.constant.ConfigConstant
import com.gfs.test.base.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BasePagingSource<T : Any>(private val initialPageIndex: Int = ConfigConstant.INITIAL_PAGE_INDEX) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val pageIndex = params.key ?: initialPageIndex
            val pageSize = params.loadSize

            // 切换到子线程去执行
            val items = withContext(Dispatchers.IO) {
                onRequest(pageIndex, pageSize) ?: emptyList()
            }

            val prevKey = if (pageIndex > 0) pageIndex - 1 else null
            val nextKey = if (items.isEmpty()) null else pageIndex + 1

            LoadResult.Page(items, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    abstract suspend fun onRequest(pageIndex: Int, pageSize: Int) : List<T>?
}