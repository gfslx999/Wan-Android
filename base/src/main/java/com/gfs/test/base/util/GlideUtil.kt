package com.gfs.test.base.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Glide 工具类
 */
object GlideUtil {

    private var mCommonErrorResourceId = 0
    private var mCommonPlaceholderResourceId = 0

    /**
     * 全局配置加载异常时的资源 id
     */
    fun configCommonErrorResourceId(resourceId: Int): GlideUtil {
        mCommonErrorResourceId = resourceId
        return this
    }

    /**
     * 全局配置加载时的占位图资源 id
     */
    fun configCommonPlaceholderResourceId(resourceId: Int): GlideUtil {
        mCommonPlaceholderResourceId = resourceId
        return this
    }

    /**
     * 加载一张普通图片
     */
    @JvmOverloads
    fun load(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0
    ) {
        internalLoad(
            context,
            mode,
            imageView,
            errorResourceId = errorResourceId,
            placeholderResourceId = placeholderResourceId
        )
    }

    /**
     * 加载一张带圆角的照片
     */
    @JvmOverloads
    fun loadByRoundCorner(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        cornerRadius: Int,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0
    ) {
        internalLoad(
            context, mode, imageView, cornerRadius = cornerRadius,
            errorResourceId = errorResourceId,
            placeholderResourceId = placeholderResourceId
        )
    }

    /**
     * 加载一张圆形图片
     */
    @JvmOverloads
    fun loadByCircle(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0
    ) {
        internalLoad(
            context, mode, imageView, isCircleCrop = true,
            errorResourceId = errorResourceId,
            placeholderResourceId = placeholderResourceId
        )
    }

    /**
     * 加载一张 GIF 图片
     */
    @JvmOverloads
    fun loadByGif(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0
    ) {
        internalLoad(
            context, mode, imageView, isGifImage = true,
            errorResourceId = errorResourceId,
            placeholderResourceId = placeholderResourceId
        )
    }

    /**
     * 公开全部参数，可自由指定
     *
     * [cornerRadius] 圆角度数
     * [errorResourceId] 加载失败情况下的资源id
     * [placeholderResourceId] 加载中的占位资源 id
     * [isCircleCrop] 是否加载为圆形
     * [isGifImage] 是否为 gif 图片
     */
    fun overallLoad(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        cornerRadius: Int = 0,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0,
        isCircleCrop: Boolean = false,
        isGifImage: Boolean = false
    ) {
        internalLoad(
            context,
            mode,
            imageView,
            cornerRadius,
            errorResourceId,
            placeholderResourceId,
            isCircleCrop,
            isGifImage
        )
    }

    /**
     * 内部加载实现
     */
    private fun internalLoad(
        context: Context?,
        mode: Any?,
        imageView: ImageView?,
        cornerRadius: Int = 0,
        errorResourceId: Int = 0,
        placeholderResourceId: Int = 0,
        isCircleCrop: Boolean = false,
        isGifImage: Boolean = false
    ) {
        if (context == null || mode == null || imageView == null) {
            return
        }

        // 是否为 gif 图片
        var load = if (isGifImage) {
            Glide.with(context).asGif().load(mode)
        } else {
            Glide.with(context).load(mode)
        }

        // 是否裁剪为圆形
        if (isCircleCrop) {
            load = load.circleCrop()
        } else if (cornerRadius > 0) {
            // 是否裁剪圆角
            val bitmapTransform = RequestOptions.bitmapTransform(RoundedCorners(cornerRadius))
            val transform = bitmapTransform.transform(CenterCrop(), RoundedCorners(cornerRadius))
            load = load.apply(transform)
        }

        if (errorResourceId != 0) {
            load = load.error(errorResourceId)
        } else if (mCommonErrorResourceId != 0) {
            load = load.error(mCommonErrorResourceId)
        }

        if (placeholderResourceId != 0) {
            load = load.placeholder(placeholderResourceId)
        } else if (mCommonPlaceholderResourceId != 0) {
            load = load.placeholder(mCommonPlaceholderResourceId)
        }

        load.into(imageView)
    }

}