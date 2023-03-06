package com.gfs.helper.common.expand

import android.util.Log

fun <T> List<T>.printAllItems(tag: String = "printAllItems") {
    if (isNullOrEmpty()) {
        Log.e("printAllItems", "$tag: is null or empty!")
        return
    }
    Log.i("printAllItems", "$tag start: ====================================")
    for (t in this) {
        Log.i("printAllItems", "$tag: $t")
    }
    Log.i("printAllItems", "$tag end: ====================================")
}