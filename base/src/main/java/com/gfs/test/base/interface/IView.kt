package com.gfs.test.base.`interface`

import android.os.Bundle
import android.view.View

internal interface IView {

    fun initView(savedInstanceState: Bundle?, view: View? = null)

    fun initData()

    fun showToast(msg: Any?)

    fun showLoading(loadingContent: String = "Loading...")

    fun updateLoadingContent(content: String)

    fun hideLoading()

}