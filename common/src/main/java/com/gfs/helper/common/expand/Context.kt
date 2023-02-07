package com.gfs.helper.common.expand

import android.content.Context
import android.content.Intent

inline fun <reified T> Context.tStartActivity() {
    startActivity(Intent(this, T::class.java))
}