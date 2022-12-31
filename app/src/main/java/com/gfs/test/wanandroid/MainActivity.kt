package com.gfs.test.wanandroid

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gfs.test.base.constant.EventConstant
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.ActivityUtil
import com.gfs.test.wanandroid.databinding.ActivityMainBinding
import com.jeremyliao.liveeventbus.LiveEventBus

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        binding.toolBar.apply {
            title = "WanAndroid"
            setSupportActionBar(binding.toolBar)
        }

        binding.btnChange.setOnClickListener {
            ActivityUtil.startActivity<SelectAppThemeActivity>(this)
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