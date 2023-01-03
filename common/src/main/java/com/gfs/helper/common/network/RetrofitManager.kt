package com.gfs.helper.common.network

import com.gfs.helper.common.entity.RetrofitConfig
import com.gfs.test.base.constant.UrlConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit 管理类
 */
object RetrofitManager {

    private val mCacheRetrofitMap = mutableMapOf<String, Retrofit>()
    private val mCommonHeadersMap: HashMap<String, String> by lazy { HashMap() }
    private var mOkHttpClient: OkHttpClient? = null

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
     * 添加公共请求头
     *
     * 注意：调用此方法后必须调用 [takeEffectConfig] 方法，对应信息才会生效
     */
    fun addCommonHeaders(key: String, value: String) : RetrofitManager {
        if (key.isEmpty()) {
            return this
        }
        mCommonHeadersMap[key] = value
        return this
    }

    /**
     * 使配置生效
     */
    fun takeEffectConfig() {
        if (mOkHttpClient != null) {
            mOkHttpClient = null
        }
        if (mCacheRetrofitMap.isNotEmpty()) {
            mCacheRetrofitMap.clear()
        }
        createOkHttpClient(mCommonHeadersMap)
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

    private var mRetrofitConfig: RetrofitConfig? = null

    private fun createOkHttpClient(commonHeadersMap: HashMap<String, String>? = null) {
        if (mOkHttpClient != null) {
            return
        }
        if (mRetrofitConfig == null) {
            mRetrofitConfig = RetrofitConfig()
        }
        //todo 添加公共请求头配置逻辑
        mRetrofitConfig!!.let { retrofitConfig ->
            mOkHttpClient =  OkHttpClient.Builder()
                .callTimeout(retrofitConfig.callTimeOut, retrofitConfig.callTimeUnit)
                .readTimeout(retrofitConfig.readTimeOut, retrofitConfig.readTimeUnit)
                .connectTimeout(retrofitConfig.connectTimeOut, retrofitConfig.connectTimeUnit)
                .addNetworkInterceptor(LoggingInterceptor(commonHeadersMap))
                .build()
        }
    }

}