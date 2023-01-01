package com.gfs.test.wanandroid.mvvm.api

import com.gfs.helper.common.entity.BaseResponseEntity
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 文章相关接口
 */
interface ArticleApiService {

    // 请求首页文章列表
    @GET("article/list/{pageIndex}/json")
    suspend fun requestIndexArticleList(
        @Path("pageIndex") pageIndex: Int, @Query("page_size") pageSize: Int
    ): BaseResponseEntity<IndexArticleModel>

}