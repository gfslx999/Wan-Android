package com.gfs.test.wanandroid.mvvm.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.gfs.helper.common.ui.helper.CardViewPagerAutoLoopHelper
import com.gfs.helper.common.ui.layoutmanager.CardStackLayoutManager
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.LogUtil
import com.gfs.test.base.util.ScreenUtils
import com.gfs.test.wanandroid.callback.SlideCardStackCallBack
import com.gfs.test.wanandroid.databinding.ActivityTestBinding
import com.gfs.test.wanandroid.mvvm.view.adapter.HomeKeyWorkAdapter
import com.gfs.test.wanandroid.mvvm.view.adapter.TestRvCardAdapter
import com.youth.banner.Banner

class TestActivity : BaseActivity<ActivityTestBinding>() {

    private val mStringList = listOf(
        "标题1111111111111111111111111",
        "标题2222222222222222222222222",
        "标题3333333333333333333333333"
    )
    private lateinit var mHomeKeyWorkAdapter: HomeKeyWorkAdapter

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        mHomeKeyWorkAdapter = HomeKeyWorkAdapter(this, mStringList)

//        initBanner(homeKeyWorkAdapter)
        initViewPager2()
//        initRvCards()
    }

    private fun initRvCards() {
        binding.rvCards.visibility = View.VISIBLE
        var list = listOf(
            "撒大方撒非法所得vv是大是大非",
            "请问道歉啊我的个人文本吧",
            "我日哦给你热i个哦你那个热日哦",
        )
        list = list.reversed()
        val testRvCardAdapter = TestRvCardAdapter(list)

        binding.rvCards.apply {
//            layoutManager = LinearLayoutManager(this@TestActivity)
            layoutManager =
                CardStackLayoutManager()
            adapter = testRvCardAdapter
        }

        testRvCardAdapter.setOnItemClickListener { adapter, view, position ->
            LogUtil.logI("position: $position")
            showToast(list.get(position))
        }
        val slideCardStackCallBack = SlideCardStackCallBack(testRvCardAdapter)
        val itemTouchHelper = ItemTouchHelper(slideCardStackCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rvCards)
    }

    private fun initViewPager2() {
        binding.clViewPager2Parent.visibility = View.VISIBLE
        binding.banner.visibility = View.GONE

        binding.viewPagerTwo.adapter = mHomeKeyWorkAdapter
        binding.viewPagerTwo.offscreenPageLimit = 3

        val pagerAutoLoopHelper = CardViewPagerAutoLoopHelper(binding.viewPagerTwo)
        pagerAutoLoopHelper.initialize(lifecycle)
    }

    override fun initData() {
    }
}