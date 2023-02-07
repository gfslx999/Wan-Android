package com.gfs.test.wanandroid.mvvm.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.gfs.test.base.constant.EventConstant
import com.gfs.test.base.model.ThemeConfigModel
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.SourceUtil
import com.gfs.test.base.util.ThemeUtil
import com.gfs.test.wanandroid.databinding.ActivitySelectAppThemeBinding
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.random.Random
import kotlin.random.nextInt

class SelectAppThemeActivity : BaseActivity<ActivitySelectAppThemeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val themeType = intent?.getIntExtra("themeType", 0)
        if (themeType == 0) {
            setTheme(com.gfs.test.base.R.style.CustomTheme_Sales)
        } else {
            setTheme(com.gfs.test.base.R.style.CustomTheme_Manager)
        }
        super.onCreate(savedInstanceState)
    }

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        val drawableFromAttribute = SourceUtil.getResourceId(this, com.gfs.test.base.R.attr.naviHomeIcon)
        binding.ivImage.setImageResource(drawableFromAttribute)
//        var temp = 0
//        binding.btnChangeAppTheme.setOnClickListener {
//            if (temp % 2 == 0) {
//                binding.ivImage.setImageResource(drawableFromAttribute)
//            } else {
//
//            }
//        }
    }

    override fun initData() {

    }
}