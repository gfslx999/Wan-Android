package com.gfs.test.base.util

import android.util.Log
import com.gfs.test.base.BuildConfig

object LogUtil {

    private const val TAG = "LogUtil"
    private var isEnableLog = BuildConfig.DEBUG

    /**
     * 设置是否允许 log 打印
     *
     * 默认仅在 debug 模式下允许打印
     */
    fun setEnableLog(enableLog: Boolean) {
        isEnableLog = enableLog
    }

    fun logI(msg: Any?) {
        if (!isEnableLog) {
            return
        }
        Log.i(TAG, "logI: $msg")
    }

    fun logE(msg: Any?) {
        if (!isEnableLog) {
            return
        }
        Log.e(TAG, "logE: $msg")
    }

    fun logD(msg: Any?) {
        if (!isEnableLog) {
            return
        }
        Log.d(TAG, "logD: $msg")
    }

}