package com.gfs.helper.common.entity

import java.util.concurrent.TimeUnit

class RetrofitConfig(builder: Builder? = null) {
    private val mDefaultTimeOutSeconds: Long = 30
    private val mDefaultTimeUnit: TimeUnit = TimeUnit.SECONDS

    internal val callTimeOut: Long
    internal val readTimeOut: Long
    internal val connectTimeOut: Long
    internal val callTimeUnit: TimeUnit
    internal val readTimeUnit: TimeUnit
    internal val connectTimeUnit: TimeUnit

    init {
        callTimeOut = builder?.callTimeOut ?: mDefaultTimeOutSeconds
        readTimeOut = builder?.readTimeOut ?: mDefaultTimeOutSeconds
        connectTimeOut = builder?.connectTimeOut ?: mDefaultTimeOutSeconds

        callTimeUnit = builder?.callTimeUnit ?: mDefaultTimeUnit
        readTimeUnit = builder?.readTimeUnit ?: mDefaultTimeUnit
        connectTimeUnit = builder?.connectTimeUnit ?: mDefaultTimeUnit
    }

    data class Builder (
        internal var callTimeOut: Long? = null,
        internal var callTimeUnit: TimeUnit? = null,
        internal var readTimeOut: Long? = null,
        internal var readTimeUnit: TimeUnit? = null,
        internal var connectTimeOut: Long? = null,
        internal var connectTimeUnit: TimeUnit? = null,
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

        fun build() : RetrofitConfig {
            return RetrofitConfig(this)
        }
    }
}