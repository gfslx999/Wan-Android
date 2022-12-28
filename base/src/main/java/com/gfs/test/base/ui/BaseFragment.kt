package com.gfs.test.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.FragmentBinding
import com.dylanc.viewbinding.base.FragmentBindingDelegate
import com.gfs.test.base.`interface`.IView
import com.gfs.test.base.util.ToastUtil

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IView, FragmentBinding<VB> by FragmentBindingDelegate() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewWithBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, view)
        initData()
    }

    override fun showToast(msg: Any?) {
        ToastUtil.showToast(requireActivity(), msg)
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