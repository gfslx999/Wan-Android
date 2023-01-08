package com.gfs.test.base.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.gfs.test.base.R

/**
 * 状态栏工具类
 */
object StatusBarUtil {

    const val DEFAULT_STATUS_BAR_ALPHA = 112
    private val FAKE_STATUS_BAR_VIEW_ID: Int = R.id.statusbarutil_fake_status_bar_view
    private val FAKE_TRANSLUCENT_VIEW_ID: Int = R.id.statusbarutil_translucent_view
    private const val TAG_KEY_HAVE_SET_OFFSET = -123


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
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param activity       需要设置的activity
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTransparentForImageView(activity: Activity, needOffsetView: View? = null) {
        setTranslucentForImageView(activity, 0, needOffsetView)
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     * @param needOffsetView 需要向下偏移的 View
     */
    fun setTranslucentForImageView(
        activity: Activity,
        @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int,
        needOffsetView: View?
    ) {
        setTransparentForWindow(activity)
        addTranslucentView(activity, statusBarAlpha)
        if (needOffsetView != null) {
            val haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET)
            if (haveSetOffset != null && haveSetOffset as Boolean) {
                return
            }
            val layoutParams: ViewGroup.MarginLayoutParams =
                needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                layoutParams.rightMargin, layoutParams.bottomMargin
            )
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true)
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 设置透明
     */
    private fun setTransparentForWindow(activity: Activity) {
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private fun addTranslucentView(
        activity: Activity,
        @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int
    ) {
        val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.visibility == View.GONE) {
                fakeTranslucentView.visibility = View.VISIBLE
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private fun createTranslucentStatusBarView(activity: Activity, alpha: Int): View? {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
        statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
        return statusBarView
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }


}