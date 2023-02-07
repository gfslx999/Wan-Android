package com.gfs.test.base.util

import android.content.Context
import com.gfs.test.base.BaseApplication

object SourceUtil {

    /**
     * 获取资源id
     */
    fun getResourceId(context: Context, attribute: Int) : Int {
        val attributes = context.obtainStyledAttributes(intArrayOf(attribute))
        val resourceId = attributes.getResourceId(0, 0)
        attributes.recycle()
        return resourceId
    }

    /**
     * 获取颜色值
     */
    fun getColor(context: Context, attribute: Int) : Int {
        val attributes = context.obtainStyledAttributes(intArrayOf(attribute))
        val color = attributes.getColor(0, 0)
        attributes.recycle()
        return color
    }

}