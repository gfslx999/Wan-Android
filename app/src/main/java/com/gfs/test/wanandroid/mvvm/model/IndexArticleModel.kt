package com.gfs.test.wanandroid.mvvm.model

import com.gfs.helper.common.base.paging.BasePagingData
import com.google.gson.annotations.SerializedName

/**
 * 首页-文章列表item实体类
 */
data class IndexArticleModel(
    val curPage: Int?,
    @SerializedName("datas")
    val dataList: List<Data>?,
    val offset: Int?,
    val over: Boolean?,
    val pageCount: Int?,
    val size: Int?,
    val total: Int?
) {
    data class Data(
        val adminAdd: Boolean?,
        val apkLink: String?,
        val audit: Int?,
        val author: String?,
        val canEdit: Boolean?,
        val chapterId: Int?,
        val chapterName: String?,
        val collect: Boolean?,
        val courseId: Int?,
        val desc: String?,
        val descMd: String?,
        val envelopePic: String?,
        val fresh: Boolean?,
        val host: String?,
        val id: Int?,
        val isAdminAdd: Boolean?,
        val link: String?,
        val niceDate: String?,
        val niceShareDate: String?,
        val origin: String?,
        val prefix: String?,
        val projectLink: String?,
        val publishTime: Long?,
        val realSuperChapterId: Int?,
        val selfVisible: Int?,
        val shareDate: Long?,
        val shareUser: String?,
        val superChapterId: Int?,
        val superChapterName: String?,
        val tags: List<Any>?,
        val title: String?,
        val type: Int?,
        val userId: Int?,
        val visible: Int?,
        val zan: Int?
    ) : BasePagingData() {
        override fun soleId(): String  = id.toString()
    }
}