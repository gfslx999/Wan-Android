package com.gfs.test.base.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object GlideUtil {

    private var mCommonErrorResourceId = -1

    fun configErrorPlaceHolder(resourceId: Int) : GlideUtil {
        mCommonErrorResourceId = resourceId
        return this
    }

//    private fun internalLoad(
//        context: Context?,
//        mode: Any?,
//        imageView: ImageView?,
//        radius: Int = 0
//    ) {
//        if (context == null || mode == null || imageView == null) {
//            return
//        }
//        var load = Glide.with(context).load(mode)
//        if (radius > 0) {
//            val bitmapTransform = RequestOptions.bitmapTransform(RoundedCorners(radius))
//            val transform = bitmapTransform.transform(CenterCrop(), RoundedCorners(radius))
//            load = load.apply(transform)
//        }
//        load.error()
//
//        load.into(imageView)
//    }

}