package com.gfs.helper.common.expand

import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setDefaultOverScrollMode() {
    if (Build.VERSION.SDK_INT < VERSION_CODES.S) {
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}