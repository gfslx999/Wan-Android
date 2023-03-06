package com.gfs.helper.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 限制滑动方向的 ConstraintLayout
 */
class MyAstrictScrollOrientationConstraintLayout : ConstraintLayout {

    companion object {
        // 不作限制
        private const val NONE = 0
        // 限制向左滑动
        private const val LEFT = 1
        // 限制向右滑动
        private const val RIGHT = 2
    }

    private val mAstrictOrientation = RIGHT
    private var mInitialRawX = -1f
    private var mClickRawX = -1f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (mAstrictOrientation == NONE) {
            return super.dispatchTouchEvent(ev)
        }
        if (ev == null) {
            return false
        }
        val currentRawX = ev.rawX

        if (ev.action == MotionEvent.ACTION_DOWN) {
            mClickRawX = currentRawX
        }
        // 重置坐标
        if (ev.action == MotionEvent.ACTION_UP) {
            mInitialRawX = -1f
        }
        if (ev.action == MotionEvent.ACTION_MOVE) {
            val rule = when (mAstrictOrientation) {
                LEFT -> {
                    currentRawX <= mInitialRawX
                }
                RIGHT -> {
                    currentRawX >= mInitialRawX
                }
                else -> {
                    false
                }
            }
            if (mInitialRawX != -1f && rule) {
                return true
            }
            mInitialRawX = currentRawX
        }

        return super.dispatchTouchEvent(ev)
    }

}