package com.gfs.helper.common.ui.helper

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2

/**
 * 层叠卡片效果 ViewPager，自动轮博辅助类
 */
class CardViewPagerAutoLoopHelper(private val viewPager2: ViewPager2) {

    companion object {
        private const val INVALID_VALUE = -1
    }
    private val mScaleOffset = 160f
    private val mTranslationOffset = 10f

    private var mTempPosition = INVALID_VALUE
    private var isScrolled = false
    private var mIsInfiniteLoop = true
    private var mLoopIntervalTimeMillSeconds: Long = 0

    private var mCurrentMessage: Message? = null
    private var mLastPauseMessage: Message? = null

    private val mHandler = Handler(Looper.getMainLooper()) { msg ->
        val nextIndex = msg.what
        if (nextIndex >= 0 && nextIndex <= getFakeItemCount()) {
            viewPager2.currentItem = nextIndex
        }
        false
    }

    fun initialize(lifecycle: Lifecycle) {
        initialize(lifecycle, true, 3000)
    }

    /**
     * 初始化：配置卡片效果、并开启自动轮博
     *
     * 调用此方法前，务必为其设置 adapter
     *
     * [isAutoLoop] 是否自动轮博
     * [loopIntervalTimeMillSeconds] 自动轮博间隔时长
     */
    fun initialize(lifecycle: Lifecycle, isAutoLoop: Boolean, loopIntervalTimeMillSeconds: Long) {
        mLoopIntervalTimeMillSeconds = loopIntervalTimeMillSeconds

        addLifecycleObserver(lifecycle)
        viewPager2.apply {
            if (adapter == null) {
                throw java.lang.IllegalArgumentException("initialize failed, be sure to call [viewPager2.setAdapter()] " +
                        "before calling this method!")
            }
            setPageTransformer(getPageTransformer())
            registerOnPageChangeCallback(getOnPageChangeCallback(isAutoLoop))

            // 默认滑动到首个 item
            val itemCount = adapter?.itemCount ?: 0
            if (itemCount > 1) {
                setCurrentItem(1, false)
            }
        }
    }


    /**
     * 获取 item 真实数量，不包含头尾数量
     */
    fun getRealItemCount() : Int {
        val fakeItemCount = getFakeItemCount()
        if (fakeItemCount == 0) {
            return 0
        }
        return fakeItemCount - 2
    }

    /**
     * 获取 item 虚拟数量，包含头尾数量
     */
    fun getFakeItemCount() : Int = viewPager2.adapter?.itemCount ?: 0

    /**
     * 添加生命周期监听
     *
     * 在不可见时移除自动轮博任务；在可见时恢复
     */
    private fun addLifecycleObserver(lifecycle: Lifecycle) {
        lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onResume(owner: LifecycleOwner) {
                if (mLastPauseMessage != null) {
                    sendNextScrollPosition(viewPager2.currentItem)
                    mLastPauseMessage = null
                }
            }

            override fun onPause(owner: LifecycleOwner) {
                if (mCurrentMessage != null) {
                    mLastPauseMessage = mCurrentMessage
                    removeAwaitScrollPositionMessage()
                }
            }
        })
    }

    private fun getOnPageChangeCallback(isAutoLoop: Boolean) = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            // 是否自动轮博
            if (isAutoLoop) {
                sendNextScrollPosition(position)
            }

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
                if (mTempPosition != INVALID_VALUE && mIsInfiniteLoop) {
                    if (mTempPosition == 0) {
                        viewPager2.setCurrentItem(
                            getRealItemCount(),
                            false
                        )
                    } else if (mTempPosition == getFakeItemCount() - 1) {
                        viewPager2.setCurrentItem(1, false)
                    }
                }
            }
        }
    }

    /**
     * 发送需滚动到指定下标的延时消息
     *
     * [isScrollToNext] 是否滚动到下个下标，true: position + 1；false：position
     */
    private fun sendNextScrollPosition(currentPosition: Int, isScrollToNext: Boolean = true) {
        removeAwaitScrollPositionMessage()
        mCurrentMessage = Message().apply {
            what = if (isScrollToNext) {
                if (currentPosition == getFakeItemCount() + 1) {
                    1
                } else {
                    currentPosition + 1
                }
            } else {
                currentPosition
            }
        }
        mHandler.sendMessageDelayed(mCurrentMessage!!, mLoopIntervalTimeMillSeconds)
    }

    /**
     * 移除当前待滚动的消息
     */
    private fun removeAwaitScrollPositionMessage() {
        if (mCurrentMessage != null) {
            mHandler.removeMessages(mCurrentMessage!!.what)
            mCurrentMessage = null
        }
    }

    private fun getPageTransformer(): ViewPager2.PageTransformer = ViewPager2.PageTransformer { page, position ->
            val pageWidth: Int = page.width
            if (position < 0F) {
                page.translationX = 0F
            } else {
                val transX = -pageWidth * position + mTranslationOffset * position
                page.translationX = transX
                // 缩放比例
                val scale = (pageWidth - mScaleOffset * position) / pageWidth.toFloat()
                page.scaleX = scale
                page.scaleY = scale
                // 控制展示层级
                page.translationZ = -position
            }
        }

}