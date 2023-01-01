package com.gfs.test.wanandroid.mvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfs.helper.common.expand.createViewModel
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.LogUtil
import com.gfs.test.wanandroid.R
import com.gfs.test.wanandroid.databinding.ActivityIndexArticleAdapterBinding
import com.gfs.test.wanandroid.mvvm.view.adapter.IndexArticleAdapter
import com.gfs.test.wanandroid.mvvm.viewmodel.IndexViewModel
import kotlinx.coroutines.flow.collect

class IndexArticleActivity : BaseActivity<ActivityIndexArticleAdapterBinding>() {

    private val mViewModel by lazy { createViewModel<IndexViewModel>() }
    private val mArticleAdapter = IndexArticleAdapter()

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(this@IndexArticleActivity)
            adapter = mArticleAdapter
        }

        lifecycleScope.launchWhenCreated {
            mViewModel.requestIndexArticlePagingData().collect {
                mArticleAdapter.submitData(it)
            }
        }
        mArticleAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.rvArticles.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.rvArticles.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    binding.progressCircular.visibility = View.GONE
                    LogUtil.logE("message: ${error.error.message}")
                    showToast("请求失败：" + error.error.message)
                }
            }
        }
    }

    override fun initData() {
    }
}