package com.gfs.helper.common.network

import com.gfs.helper.common.entity.BaseResponseEntity
import com.gfs.test.base.constant.HttpCodeConstant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {

    protected inline fun <reified T> createApi(baseUrl: String? = null) =
        RetrofitManager.createApi(T::class.java, baseUrl)

    /**
     * 处理请求响应结果
     *
     * [isCheckResponseCode] 是否检查响应code，默认检查；若 code 不为成功，将抛出异常
     */
    protected suspend fun <T> doRequest(
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
        isCheckResponseCode: Boolean = true,
        handleResponse: suspend () -> BaseResponseEntity<T>
    ) : T? = withContext(coroutineDispatcher) {
        val response = handleResponse()
        if (isCheckResponseCode && response.errorCode != HttpCodeConstant.CODE_SUCCESS) {
            throw RuntimeException(response.errorMsg)
        }
        return@withContext response.data
    }

}