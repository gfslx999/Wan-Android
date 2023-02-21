package com.gfs.helper.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.gfs.test.base.util.LogUtil

class MyConstraintLayout : ConstraintLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mInitialRawX = -1f
    private var mClickRawX = -1f

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
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
            if (mInitialRawX != -1f && currentRawX >= mInitialRawX) {
                return true
            }
            mInitialRawX = currentRawX
        }

        return super.dispatchTouchEvent(ev)
    }

}