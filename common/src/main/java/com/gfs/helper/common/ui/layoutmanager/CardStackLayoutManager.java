package com.gfs.helper.common.ui.layoutmanager;


import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 层叠卡片效果 - LayoutManager
 */
public class CardStackLayoutManager extends RecyclerView.LayoutManager {

    // 最开始显示叠加个数
    public static final int MAX_SHOW_COUNT = 4;

    // item 平移Y轴距
    public static final float TRANSLATION_Y = 5f;

    // 缩放的大小
    public static final float SCALE = 0.13f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        // 缓存 ViewHolder
        detachAndScrapAttachedViews(recycler);

        // 最下面图片下标
        int bottomPosition = 0;
        // 获取所有图片
        int itemCount = getItemCount();

        //如果所有图片 > 显示的图片
        if (itemCount > MAX_SHOW_COUNT) {
            // 获取到从第几张开始
            bottomPosition = itemCount - MAX_SHOW_COUNT;
        }

        for (int i = bottomPosition; i < itemCount; i++) {
            // 获取当前view宽高
            View view = recycler.getViewForPosition(i);

            addView(view);

            // 测量
            measureChildWithMargins(view, 0, 0);

            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

            // LinearLayoutManager#layoutChunk#layoutDecoratedWithMargins
            // 绘制布局
            layoutDecoratedWithMargins(view, widthSpace / 2,
                    heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));

            /*
             * 作者:android 超级兵
             * 创建时间: 11/3/21 4:25 PM
             * TODO itemCount - 1  = 最后一个元素
                    最后一个元素 - i = 倒数的元素
                 例如
                 7-1 = 6
                 7-2 = 5
                 7-3 = 4
                 7-4 = 3
                 。。。
             */
            int level = itemCount - 1 - i;

            if (level > 0) {
                int translationValue = toDip(view.getContext(), TRANSLATION_Y);

                // 如果不是最后一个才缩放
                if (level < MAX_SHOW_COUNT - 1) {
                    // 平移
                    view.setTranslationX(translationValue * level);
                    // 缩放
                    view.setScaleY(1 - SCALE * level);
                } else {
                    // 最下面的View 和前一个View布局一样(level - 1)
                    view.setTranslationX(translationValue * (level - 1));
                    view.setScaleY(1 - SCALE * (level - 1));
                }
            }
        }
    }

    private int toDip(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
}