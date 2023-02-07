package com.gfs.test.wanandroid.mvvm.view.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class SingleAddPageTransformer implements ViewPager2.PageTransformer {
    private float mOffset = 0;
    private String TAG = this.getClass().getSimpleName();
    private float scall = 1;
    private static final float CENTER_PAGE_SCALE = 0.8f;
    private int offscreenPageLimit;//vp 缓存个数
    private boolean addToRight = true;

    public SingleAddPageTransformer() {
        this(3, 100f, false);
    }

    public SingleAddPageTransformer(int offscreenPageLimit, float mOffset, boolean addToRight) {
        this.mOffset = mOffset;
        this.offscreenPageLimit = offscreenPageLimit;
        this.addToRight = addToRight;
    }

    private int pagerWidth;
    private float horizontalOffsetBase;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (pagerWidth == 0)
            pagerWidth = page.getWidth();//vp width
        if (horizontalOffsetBase == 0)
            horizontalOffsetBase = (pagerWidth - pagerWidth * CENTER_PAGE_SCALE) / 2 / offscreenPageLimit + mOffset;

        if (addToRight) {
            if (position >= offscreenPageLimit || position <= -1) {//向左边滑去的
//            logUtil.d_2(TAG, "向左边滑去了");
                page.setVisibility(View.GONE);
            } else {
                page.setVisibility(View.VISIBLE);
            }

            //setTranslation
            if (position >= 0) {
//            page.setTranslationX((-pagerWidth * position) + horizontalOffsetBase * position);
                page.setTranslationX((horizontalOffsetBase - pagerWidth) * position);
            } else {
                page.setTranslationX(0);
            }

            //setScale
            if (position == 0) {
                page.setScaleX(CENTER_PAGE_SCALE);
                page.setScaleY(CENTER_PAGE_SCALE);
            } else {
                float scaleFactor = Math.min(CENTER_PAGE_SCALE - position * 0.1f, CENTER_PAGE_SCALE);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }

        } else {
            if ( position >= 1) {
                page.setVisibility(View.GONE);
            } else {
                page.setVisibility(View.VISIBLE);
            }

            //setTranslation
            if (position < 0) {
//            page.setTranslationX((-pagerWidth * position) + horizontalOffsetBase * position);
                page.setTranslationX((horizontalOffsetBase - pagerWidth) * position);
                page.setTranslationZ(0);
            } else {
                page.setTranslationX(0);
                page.setTranslationZ(-position);
            }

            //setScale
            if (position == 0) {
                page.setScaleX(CENTER_PAGE_SCALE);
                page.setScaleY(CENTER_PAGE_SCALE);
            } else {
                float scaleFactor = Math.min(CENTER_PAGE_SCALE - Math.abs(position) * 0.1f, CENTER_PAGE_SCALE);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }
        }
    }
}
