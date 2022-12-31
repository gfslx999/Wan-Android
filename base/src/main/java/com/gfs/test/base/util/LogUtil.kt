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
        logSplitLine()
        Log.i("$TAG-logI", "$msg")
        logSplitLine()
    }

    fun logE(msg: Any?) {
        if (!isEnableLog) {
            return
        }

        logSplitLine()
        Log.e(TAG, "logE: $msg")
        logSplitLine()
    }

    fun logD(msg: Any?) {
        if (!isEnableLog) {
            return
        }
        logSplitLine()
        Log.d(TAG, "logD: $msg")
        logSplitLine()
    }

    private fun logSplitLine() {
        Log.i(TAG,"==========================================")
    }

}