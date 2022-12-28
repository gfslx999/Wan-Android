package com.gfs.test.base.util

import android.os.Looper

object AppUtil {

    /**
     * 校验当前是否在主线程
     */
    fun checkIsInMainThread() : Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

}