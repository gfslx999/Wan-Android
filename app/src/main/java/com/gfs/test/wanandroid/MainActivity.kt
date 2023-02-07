package com.gfs.test.wanandroid

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gfs.helper.common.expand.tStartActivity
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.wanandroid.databinding.ActivityMainBinding
import com.gfs.test.wanandroid.mvvm.view.SelectAppThemeActivity
import com.gfs.test.wanandroid.mvvm.view.TestActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        var themeType = 0
        binding.btnChange.setOnClickListener {
//            ActivityUtil.startActivity<IndexArticleActivity>(this)
            themeType = if (themeType == 0) {
                1
            } else {
                0
            }
        }
        binding.btnInto.setOnClickListener {
            tStartActivity<TestActivity>()
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