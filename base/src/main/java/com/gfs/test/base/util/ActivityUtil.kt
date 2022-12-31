package com.gfs.test.base.util

import android.app.Activity
import android.content.Context
import android.content.Intent

object ActivityUtil {

    inline fun <reified T : Activity> startActivity(context: Context) {
        val intent = Intent(context, T::class.java)
        context.startActivity(intent)
    }

}