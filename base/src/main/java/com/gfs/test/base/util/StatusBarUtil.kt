package com.gfs.test.base.util

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt

/**
 * 状态栏工具类
 */
object StatusBarUtil {

    /**
     * 设置状态栏颜色
     *
     * [isBlackIcon] 状态栏图标、文本是否为黑色
     */
    fun setStatusBarColor(activity: Activity?, @ColorInt statusBarColor: Int, isBlackIcon: Boolean = false) {
        activity?.apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (isBlackIcon) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = statusBarColor
        }
    }

    /**
     * 设置状态栏为 沉浸式布局（即将状态栏设为透明，并且背景色会依据布局中的背景色）
     */
    fun setStatusBarToImmersiveLayout(activity: Activity?, isBlackIcon: Boolean = false) {
        activity?.apply {
            window.decorView.systemUiVisibility = if (isBlackIcon) {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
            window.statusBarColor = Color.TRANSPARENT
        }
    }

}