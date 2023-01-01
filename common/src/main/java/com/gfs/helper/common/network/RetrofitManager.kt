package com.gfs.helper.common.network

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
    private val mOkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .build()
    }

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

    private fun buildRetrofit(baseUrl: String) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

}