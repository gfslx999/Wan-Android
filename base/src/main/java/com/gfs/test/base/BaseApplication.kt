package com.gfs.test.base

import android.app.Application
import com.gfs.test.base.util.MMKVUtil

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MMKVUtil.initialize(this)
    }

}