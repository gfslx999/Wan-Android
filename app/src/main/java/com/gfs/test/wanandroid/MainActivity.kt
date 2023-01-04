package com.gfs.test.wanandroid

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.gfs.helper.common.entity.RetrofitConfig
import com.gfs.helper.common.constant.LoggingInterceptorLevel
import com.gfs.helper.common.network.RetrofitManager
import com.gfs.helper.common.ui.view.CustomProgress
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.ActivityUtil
import com.gfs.test.base.util.StatusBarUtil
import com.gfs.test.wanandroid.databinding.ActivityMainBinding
import com.gfs.test.wanandroid.mvvm.view.IndexArticleActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?, view: View?) {
//        StatusBarUtil.setStatusBarToImmersiveLayout(this)

        // second way
        window.statusBarColor = Color.TRANSPARENT
//        binding.root.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding.btnChange) { view, insets ->
//            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
//            layoutParams.topMargin = insets.systemWindowInsetTop
//            insets
//        }

        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val retrofitConfig = RetrofitConfig.Builder()
            .addCommonHeader("MainActivity-test1", "123")
            .addCommonHeader("MainActivity-test2", "456")
            .loggingInterceptorLevel(LoggingInterceptorLevel.BODY)
            .build()
        RetrofitManager.setRetrofitConfig(retrofitConfig)

        //todo 如何封装一个通用的 navigationBar，并可使状态栏透明

        var mProgress = 0f
        binding.btnChange.setOnClickListener {
            mProgress += 10f
            binding.semicircleProgress.updateProgress(mProgress, true)
//            ActivityUtil.startActivity<IndexArticleActivity>(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.gfs.test.base.R.menu.base_menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.gfs.test.base.R.id.action_search) {
            showToast("搜索")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
    }
}