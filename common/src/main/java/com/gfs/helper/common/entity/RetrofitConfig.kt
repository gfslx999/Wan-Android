package com.gfs.helper.common.entity

import com.gfs.helper.common.constant.LoggingInterceptorLevel
import java.util.concurrent.TimeUnit

/**
 * Retrofit 相关配置类
 *
 * 通过 RetrofitConfig.Builder() 来构造此对象
 */
class RetrofitConfig internal constructor(builder: Builder? = null) {
    private val mDefaultTimeOutSeconds: Long = 30
    private val mDefaultTimeUnit: TimeUnit = TimeUnit.SECONDS

    internal val callTimeOut: Long
    internal val readTimeOut: Long
    internal val connectTimeOut: Long
    internal val callTimeUnit: TimeUnit
    internal val readTimeUnit: TimeUnit
    internal val connectTimeUnit: TimeUnit
    internal var commonHeadersMap: HashMap<String, String>?
    internal val loggingInterceptorLevel: LoggingInterceptorLevel?

    init {
        callTimeOut = builder?.callTimeOut ?: mDefaultTimeOutSeconds
        readTimeOut = builder?.readTimeOut ?: mDefaultTimeOutSeconds
        connectTimeOut = builder?.connectTimeOut ?: mDefaultTimeOutSeconds

        callTimeUnit = builder?.callTimeUnit ?: mDefaultTimeUnit
        readTimeUnit = builder?.readTimeUnit ?: mDefaultTimeUnit
        connectTimeUnit = builder?.connectTimeUnit ?: mDefaultTimeUnit
        commonHeadersMap = builder?.commonHeadersMap
        loggingInterceptorLevel = builder?.loggingInterceptorLevel
    }

    data class Builder (
        internal var callTimeOut: Long? = null,
        internal var callTimeUnit: TimeUnit? = null,
        internal var readTimeOut: Long? = null,
        internal var readTimeUnit: TimeUnit? = null,
        internal var connectTimeOut: Long? = null,
        internal var connectTimeUnit: TimeUnit? = null,
        internal var commonHeadersMap: HashMap<String, String>? = null,
        internal var loggingInterceptorLevel: LoggingInterceptorLevel? = null
    ) {
        fun callTimeOut(callTimeOut: Long, timeUnit: TimeUnit) : Builder {
            this.callTimeOut = callTimeOut
            this.callTimeUnit = timeUnit
            return this
        }

        fun readTimeOut(readTimeOut: Long, timeUnit: TimeUnit) : Builder {
            this.readTimeOut = readTimeOut
            this.readTimeUnit = timeUnit
            return this
        }

        fun connectTimeOut(connectTimeOut: Long, timeUnit: TimeUnit) : Builder {
            this.connectTimeOut = connectTimeOut
            this.connectTimeUnit = timeUnit
            return this
        }

        /**
         * 添加公共请求头，适用于添加不可变的公共请求头信息
         */
        fun addCommonHeader(key: String, value: String) : Builder {
            if (key.isEmpty()) {
                return this
            }
            if (commonHeadersMap == null) {
                commonHeadersMap = hashMapOf()
            }
            commonHeadersMap!![key] = value
            return this
        }

        /**
         * 网络请求拦截器-日志级别
         */
        fun loggingInterceptorLevel(level: LoggingInterceptorLevel) : Builder {
            this.loggingInterceptorLevel = level
            return this
        }

        fun build() : RetrofitConfig {
            return RetrofitConfig(this)
        }
    }
}