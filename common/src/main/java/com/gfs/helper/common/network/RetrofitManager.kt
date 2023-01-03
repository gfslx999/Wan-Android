package com.gfs.helper.common.network

import com.gfs.helper.common.entity.RetrofitConfig
import com.gfs.test.base.constant.UrlConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit 管理类
 */
object RetrofitManager {

    private val mCacheRetrofitMap = mutableMapOf<String, Retrofit>()
    private var mOkHttpClient: OkHttpClient? = null
    private var mRetrofitConfig: RetrofitConfig? = null

    /**
     * 调用 retrofit 创建对应的 API Service
     *
     * [clazz] 目标 apiService 的类型
     * [baseUrl] 指定域名，为空时将使用默认域名
     */
    fun <T> createApi(clazz: Class<T>, baseUrl: String? = null) : T {
        val finallyBaseUrl = if (baseUrl.isNullOrEmpty()) {
            UrlConstant.BASE_URL
        } else {
            baseUrl
        }
        return try {
            if (!mCacheRetrofitMap.containsKey(finallyBaseUrl)) {
                throw NullPointerException("not contains")
            }
            val retrofit = mCacheRetrofitMap[finallyBaseUrl]
                ?: throw NullPointerException("null")

            retrofit.create(clazz)
        } catch (e: NullPointerException) {
            val retrofit = buildRetrofit(finallyBaseUrl)
            mCacheRetrofitMap[finallyBaseUrl] = retrofit
            retrofit.create(clazz)
        }
    }

    /**
     * 设置 Retrofit 相关配置信息
     */
    fun setRetrofitConfig(retrofitConfig: RetrofitConfig) {
        mRetrofitConfig = retrofitConfig
        resetCacheInfo()
    }

    /**
     * 动态添加公共请求头
     *
     * 适用于动态添加可变请求头，例如：token、userId、userCode...
     */
    fun addCommonHeaders(key: String, value: String) {
        if (key.isEmpty()) {
            return
        }

        if (mRetrofitConfig == null) {
            mRetrofitConfig = RetrofitConfig.Builder()
                .addCommonHeader(key, value)
                .build()
        } else {
            mRetrofitConfig!!.let {
                if (it.commonHeadersMap == null) {
                    it.commonHeadersMap = hashMapOf()
                }
                it.commonHeadersMap!![key] = value
            }
        }
        resetCacheInfo()
    }

    private fun resetCacheInfo() {
        if (mOkHttpClient != null) {
            mOkHttpClient = null
        }
        if (mCacheRetrofitMap.isNotEmpty()) {
            mCacheRetrofitMap.clear()
        }
    }

    private fun buildRetrofit(baseUrl: String) : Retrofit {
        if (mOkHttpClient == null) {
            createOkHttpClient()
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient!!)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createOkHttpClient() {
        if (mOkHttpClient != null) {
            return
        }
        if (mRetrofitConfig == null) {
            mRetrofitConfig = RetrofitConfig()
        }
        mRetrofitConfig!!.let { retrofitConfig ->
            mOkHttpClient =  OkHttpClient.Builder()
                .callTimeout(retrofitConfig.callTimeOut, retrofitConfig.callTimeUnit)
                .readTimeout(retrofitConfig.readTimeOut, retrofitConfig.readTimeUnit)
                .connectTimeout(retrofitConfig.connectTimeOut, retrofitConfig.connectTimeUnit)
                .addNetworkInterceptor(LoggingInterceptor(retrofitConfig.commonHeadersMap, retrofitConfig.loggingInterceptorLevel))
                .build()
        }
    }

}