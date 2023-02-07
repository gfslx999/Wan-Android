package com.gfs.test.wanandroid.mvvm.view

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.LogUtil
import com.gfs.test.base.util.ScreenUtils
import com.gfs.test.wanandroid.databinding.ActivityTestBinding
import com.gfs.test.wanandroid.mvvm.view.adapter.HomeKeyWorkAdapter
import com.youth.banner.Banner

class TestActivity : BaseActivity<ActivityTestBinding>() {
    private val mScaleOffset = 100f
    private val mTranslationOffset = 100f

    private var mMaxScaleValue: Float? = 0.949f

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        val stringList = listOf(
            "标题1111111111111111111111111",
            "标题2222222222222222222222222",
            "标题3333333333333333333333333"
        )
        val homeKeyWorkAdapter = HomeKeyWorkAdapter(this, stringList)

//        initBanner(homeKeyWorkAdapter)
        initViewPager2(homeKeyWorkAdapter)
    }

    private fun getPageTransformer() : PageTransformer = PageTransformer { page, position ->
        val pageWidth: Int = page.width
        LogUtil.logI("position: $position, page: $page")
        if (position < 0F) {
            page.translationX = 0F
        } else {
            val transX = -pageWidth * position + mTranslationOffset * position
//            val transX = -pageWidth * position
            page.translationX = transX
            // 缩放比例
            var scale = (pageWidth - mScaleOffset * position) / pageWidth.toFloat()
            if (scale > mMaxScaleValue!!) {
                scale = mMaxScaleValue!!
            }
            LogUtil.logI("scale: $scale")
            page.scaleX = scale
            page.scaleY = scale
            // 控制展示层级
            page.translationZ = -position
        }
    }

    private var mTempPosition = Banner.INVALID_VALUE
    private var isScrolled = false
    private var mIsInfiniteLoop = true

    private fun initViewPager2(homeKeyWorkAdapter: HomeKeyWorkAdapter) {
        binding.clViewPager2Parent.visibility = View.VISIBLE
        binding.banner.visibility = View.GONE

        binding.viewPagerTwo.adapter = homeKeyWorkAdapter
        binding.viewPagerTwo.offscreenPageLimit = 3
        binding.viewPagerTwo.setPageTransformer(getPageTransformer())
        binding.viewPagerTwo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (isScrolled) {
                    mTempPosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //手势滑动中,代码执行滑动中
                if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                    isScrolled = true
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //滑动闲置或滑动结束
                    isScrolled = false
                    if (mTempPosition != Banner.INVALID_VALUE && mIsInfiniteLoop) {
                        if (mTempPosition == 0) {
                            binding.viewPagerTwo.setCurrentItem(homeKeyWorkAdapter.realCount, false)
                        } else if (mTempPosition == homeKeyWorkAdapter.itemCount - 1) {
                            binding.viewPagerTwo.setCurrentItem(1, false)
                        }
                    }
                }
            }
        })

        homeKeyWorkAdapter.setOnBannerListener { data, position ->
            showToast(data)
        }
    }

    private fun initBanner(homeKeyWorkAdapter: HomeKeyWorkAdapter) {
        binding.clViewPager2Parent.visibility = View.GONE
        binding.banner.visibility = View.VISIBLE

        binding.banner.viewPager2.offscreenPageLimit = 3
        binding.banner.setAdapter(homeKeyWorkAdapter)
            .setLoopTime(2000)
            .setBannerRound2(ScreenUtils.dipToPx(this, 6f))
            .setPageTransformer(getPageTransformer())
            .addBannerLifecycleObserver(this)
    }

    override fun initData() {
    }
}