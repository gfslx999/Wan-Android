package com.gfs.test.base.util

import android.app.UiModeManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.gfs.test.base.R
import com.gfs.test.base.constant.CacheConstant
import com.gfs.test.base.model.ThemeConfigModel

object ThemeUtil {

    private var mCurrentCacheThemeColor: Int = -1

    /**
     * 更新主题配置
     */
    fun saveAppThemeConfig(themeConfigModel: ThemeConfigModel) {
        if (mCurrentCacheThemeColor == themeConfigModel.themeColor) {
            return
        }
        MMKVUtil.save(CacheConstant.APP_THEME_COLOR, themeConfigModel.themeColor)
        MMKVUtil.save(CacheConstant.APP_THEME_SUPER_STYLE_IS_DARK, themeConfigModel.superStyleIsDark)
        mCurrentCacheThemeColor = themeConfigModel.themeColor
    }

    /**
     * 获取当前主题配置
     */
    fun getAppThemeConfig(context: Context) : ThemeConfigModel {
        // 判断当前是否处于深色模式，处于深色模式时，直接返回深色模式主题色
        if (isNightModeNow(context)) {
            return ThemeConfigModel(ContextCompat.getColor(context, R.color.night_primary_color))
        }
        val cacheThemeColor = getCurrentCacheThemeColor()
        if (cacheThemeColor != -1) {
            val isDark = MMKVUtil.getBoolean(CacheConstant.APP_THEME_SUPER_STYLE_IS_DARK)
            return ThemeConfigModel(cacheThemeColor, isDark)
        }
        return ThemeConfigModel(ContextCompat.getColor(context, R.color.primary_color))
    }

    /**
     * 判断当前是否为深色模式
     */
    fun isNightModeNow(context: Context) : Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager

        // 判断当前是否处于深色模式，处于深色模式时，直接返回深色模式主题色
        return uiModeManager?.nightMode == UiModeManager.MODE_NIGHT_YES
    }

    private fun getCurrentCacheThemeColor() : Int {
        if (mCurrentCacheThemeColor != -1) {
            return mCurrentCacheThemeColor
        }
        mCurrentCacheThemeColor = MMKVUtil.getInt(CacheConstant.APP_THEME_COLOR, -1)
        return mCurrentCacheThemeColor
    }

}