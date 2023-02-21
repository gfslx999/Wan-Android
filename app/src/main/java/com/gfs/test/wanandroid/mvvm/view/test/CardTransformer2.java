package com.gfs.test.wanandroid.mvvm.view.test;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.gfs.test.base.util.ScreenUtils;

public class CardTransformer2 implements ViewPager.PageTransformer {

    private int mOffset = 40;

    private Context mContext;
    private int spaceBetweenFirAndSecWith = 10 * 2;//第一张卡片和第二张卡片宽度差  dp单位
    private int spaceBetweenFirAndSecHeight = 10;//第一张卡片和第二张卡片高度差   dp单位

    public CardTransformer2(Context context) {
        mContext = context;
    }


    protected void onPreTransform(View page, float position) {
        final float width = page.getWidth();
        final float height = page.getHeight();

        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationX(0);
        page.setTranslationY(-height * position);

        page.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
    }


    @Override
    public void transformPage(View page, float position) {
        onPreTransform(page,position);
        if (position <= 0.0f) {
            page.setAlpha(1.0f);
            page.setTranslationY(0f);
            //控制停止滑动切换的时候，只有最上面的一张卡片可以点击
            page.setClickable(true);
        } else if (position <= 3.0f) {
            float scale = (float) (page.getWidth() - ScreenUtils.dipToPx(mContext, spaceBetweenFirAndSecWith * position)) / (float) (page.getWidth());
            //控制下面卡片的可见度
            page.setAlpha(1.0f);
            //控制停止滑动切换的时候，只有最上面的一张卡片可以点击
            page.setClickable(false);
            page.setPivotX(page.getWidth() / 2f);
            page.setPivotY(page.getHeight() / 2f);
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setTranslationY(-page.getHeight() * position + (page.getHeight() * 0.5f) * (1 - scale) + ScreenUtils.dipToPx(mContext, spaceBetweenFirAndSecHeight) * position);
        }

    }

}