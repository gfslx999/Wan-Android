package com.gfs.test.wanandroid.callback

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gfs.helper.common.ui.layoutmanager.CardStackLayoutManager
import com.gfs.test.base.util.LogUtil
import com.gfs.test.base.util.ScreenUtils
import com.gfs.test.wanandroid.mvvm.view.adapter.TestRvCardAdapter
import kotlin.math.sqrt

class SlideCardStackCallBack (private val adapter: TestRvCardAdapter) :
    ItemTouchHelper.SimpleCallback(0, 15) {

    /*
     public static final int UP = 1;  // 1

    public static final int DOWN = 1 << 1; // 2

    public static final int LEFT = 1 << 2; //4

    public static final int RIGHT = 1 << 3; // 8
 */
// 1 上滑动
// 3 上下滑动
// 7 上下左滑动
// 15   上下左右滑动
// dragDirs 拖拽
// swipeDirs 滑动

    /*
     * 拖拽使用,不管
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean = let {
        false
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 仅支持向左侧滑动
        val dragFlags = ItemTouchHelper.LEFT
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /*
     * 动画结束后的处理
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direct: Int) {
        // 当前滑动的View
        val layoutPosition = viewHolder.layoutPosition
        val mutableList = adapter.items.toMutableList()

        // 删除当前滑动的元素
        val removeAt = mutableList.removeAt(layoutPosition)
        // 添加到集合第0个位置 造成循环滑动的效果
        mutableList.add(0, removeAt)
        adapter.submitList(mutableList)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val maxDistance = recyclerView.width / 2

        //sqrt 开根号
        val sqrt = sqrt((dX * dX + dY * dY).toDouble())

        // 放大系数
        var d = sqrt / maxDistance

        if (d > 1) {
            d = 1.0
        }

        val childCount = recyclerView.childCount
        // 循环所有数据
        repeat(childCount) {
            val view = recyclerView.getChildAt(it)

            val valueDip = ScreenUtils.dipToPx(view.context, CardStackLayoutManager.TRANSLATION_Y)

            val level = childCount - 1 - it
            if (level > 0) {
                if (level < 3) {
                    view.translationX =
                        (valueDip * level - d * valueDip).toFloat()

                    val scaleValue = 1 - CardStackLayoutManager.SCALE * level + d * CardStackLayoutManager.SCALE
//                    view.scaleX = scaleValue.toFloat()
                    view.scaleY = scaleValue.toFloat()
                }
            }
        }

    }

    /*
     * 设置回弹距离
     */
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.3f
    }

    /*
     * 设置回弹时间
     */
//    override fun getAnimationDuration(
//        recyclerView: RecyclerView,
//        animationType: Int,
//        animateDx: Float,
//        animateDy: Float
//    ): Long {
//        return 1000
//    }
}