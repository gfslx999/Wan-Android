package com.gfs.test.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ActivityBinding
import com.dylanc.viewbinding.base.ActivityBindingDelegate
import com.gfs.test.base.`interface`.IView
import com.gfs.test.base.util.ToastUtil

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IView, ActivityBinding<VB> by ActivityBindingDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()
        initView(savedInstanceState)
        initData()
    }

    override fun showToast(msg: Any?) {
        ToastUtil.showToast(this, msg)
    }

    override fun showLoading(loadingContent: String) {
        // todo
    }

    override fun updateLoadingContent(content: String) {
        // todo
    }

    override fun hideLoading() {
        // todo
    }

}