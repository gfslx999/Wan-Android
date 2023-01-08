package com.gfs.test.wanandroid

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.ActivityUtil
import com.gfs.test.base.util.StatusBarUtil
import com.gfs.test.wanandroid.databinding.ActivityMainBinding
import com.gfs.test.wanandroid.mvvm.view.IndexArticleActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        binding.btnChange.setOnClickListener {
            ActivityUtil.startActivity<IndexArticleActivity>(this)
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