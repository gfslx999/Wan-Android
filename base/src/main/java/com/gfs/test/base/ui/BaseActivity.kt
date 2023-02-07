package com.gfs.test.base.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate
import com.gfs.test.base.`interface`.IView
import com.gfs.test.base.constant.EventConstant
import com.gfs.test.base.model.ThemeConfigModel
import com.gfs.test.base.util.MMKVUtil
import com.gfs.test.base.util.StatusBarUtil
import com.gfs.test.base.util.ThemeUtil
import com.gfs.test.base.util.ToastUtil
import com.jeremyliao.liveeventbus.LiveEventBus

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IView, ActivityBinding<VB> by ActivityBindingDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val themeType = MMKVUtil.getInt("themeType", 0)
        if (themeType == 0) {
            setTheme(com.gfs.test.base.R.style.CustomTheme_Sales)
        } else {
            setTheme(com.gfs.test.base.R.style.CustomTheme_Manager)
        }
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()

        initView(savedInstanceState)
        initData()

//        changeAppThemeColor(ThemeUtil.getAppThemeConfig(this), false)
//        // 监听更改主题色事件
//        LiveEventBus.get<ThemeConfigModel>(EventConstant.CHANGE_APP_THEME_COLOR_EVENT)
//            .observe(this) {
//                changeAppThemeColor(it)
//            }
    }

    protected open fun changeAppThemeColor(themeModel: ThemeConfigModel, isSave: Boolean = true) {
        supportActionBar?.let {
            it.setBackgroundDrawable(ColorDrawable(themeModel.themeColor))
        }
        StatusBarUtil.setStatusBarColor(this, themeModel.themeColor, themeModel.superStyleIsDark)
        if (!isSave) {
            return
        }
        ThemeUtil.saveAppThemeConfig(themeModel)
    }

    override fun showToast(msg: Any?) {
        ToastUtil.showToast(this, msg)
    }

    override fun showLoading(loadingContent: String) {
        // todo
    }

    override fun updateLoadingContent(content: String) {
        // todo
    }

    override fun hideLoading() {
        // todo
    }

}