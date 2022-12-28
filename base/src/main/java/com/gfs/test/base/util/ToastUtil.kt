package com.gfs.test.base.util

import android.content.Context
import android.widget.Toast


object ToastUtil {

    private var mToast: Toast? = null

    /**
     * 弹出吐司提示
     *
     * [msg] 要弹出的内容
     * [duration] 弹出时长
     * [isClearLast] 是否清除上个弹出的内容
     */
    fun showToast(
        context: Context?,
        msg: Any?,
        duration: Int = Toast.LENGTH_SHORT,
        isClearLast: Boolean = true
    ) {
        if (!AppUtil.checkIsInMainThread()) {
            LogUtil.logE("Toast failed, current thread is not in main thread now!")
            return
        }
        if (isClearLast && mToast != null) {
            mToast!!.cancel()
        }
        mToast = Toast.makeText(context, "$msg", duration)
        mToast!!.show()
    }

}