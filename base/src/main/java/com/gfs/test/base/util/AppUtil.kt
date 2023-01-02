package com.gfs.test.base.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Looper
import com.gfs.test.base.BaseApplication

object AppUtil {

    /**
     * 校验当前是否在主线程
     */
    fun checkIsInMainThread() : Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    fun logCheckIsInMainThread() {
        LogUtil.logI("checkIsInMainThread: ${checkIsInMainThread()}")
    }

    /**
     * 校验当前网络状态
     *
     * @param inExceptionResultValue 在异常情况下返回的 boolean 值
     */
    @JvmOverloads
    fun checkNetWorkIsConnect(inExceptionResultValue: Boolean = false): Boolean {
        try {
            val connectivityManager = BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (connectivityManager != null) {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return activeNetworkInfo.state == NetworkInfo.State.CONNECTED
                }
            } else {
                return inExceptionResultValue
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return inExceptionResultValue
        }
        return false
    }

}