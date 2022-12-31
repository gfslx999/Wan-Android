package com.gfs.test.wanandroid

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.gfs.test.base.constant.EventConstant
import com.gfs.test.base.model.ThemeConfigModel
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.wanandroid.databinding.ActivitySelectAppThemeBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.random.Random
import kotlin.random.nextInt

class SelectAppThemeActivity : BaseActivity<ActivitySelectAppThemeBinding>() {
    override fun initView(savedInstanceState: Bundle?, view: View?) {
        binding.btnChangeAppTheme.setOnClickListener {
            val nextInt = Random.nextInt(0, 10)

            when {
                nextInt % 2 == 0 -> {
                    LiveEventBus.get<ThemeConfigModel>(EventConstant.CHANGE_APP_THEME_COLOR_EVENT)
                        .post(ThemeConfigModel(Color.RED))
                }
                nextInt % 3 == 0 -> {
                    LiveEventBus.get<ThemeConfigModel>(EventConstant.CHANGE_APP_THEME_COLOR_EVENT)
                        .post(ThemeConfigModel(Color.BLUE))
                }
                else -> {
                    LiveEventBus.get<ThemeConfigModel>(EventConstant.CHANGE_APP_THEME_COLOR_EVENT)
                        .post(ThemeConfigModel(Color.YELLOW, true))
                }
            }
        }
    }

    override fun initData() {

    }
}