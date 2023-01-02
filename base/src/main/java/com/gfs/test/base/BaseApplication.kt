package com.gfs.test.base

import android.app.Application
import com.gfs.test.base.util.MMKVUtil

open class BaseApplication : Application() {

    companion object {
        private lateinit var mInstance: BaseApplication

        fun getInstance() : BaseApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()

        MMKVUtil.initialize(this)
        mInstance = this
    }

}