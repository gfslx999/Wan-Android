package com.gfs.test.wanandroid.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gfs.helper.common.base.paging.DefaultPagingFooterAdapter
import com.gfs.helper.common.expand.createViewModel
import com.gfs.helper.common.expand.setDefaultOverScrollMode
import com.gfs.test.base.ui.BaseActivity
import com.gfs.test.base.util.LogUtil
import com.gfs.test.wanandroid.R
import com.gfs.test.wanandroid.databinding.ActivityIndexArticleAdapterBinding
import com.gfs.test.wanandroid.mvvm.view.adapter.IndexArticleAdapter
import com.gfs.test.wanandroid.mvvm.viewmodel.IndexViewModel

class IndexArticleActivity : BaseActivity<ActivityIndexArticleAdapterBinding>() {

    private val mViewModel by lazy { createViewModel<IndexViewModel>() }
    private val mArticleAdapter = IndexArticleAdapter()

    override fun initView(savedInstanceState: Bundle?, view: View?) {
        initRv()
        initSwipeRefreshLayout()
        initOnClick()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            mArticleAdapter.refresh()
        }
    }

    private fun initRv() {
        binding.rvArticles.apply {
            layoutManager = LinearLayoutManager(this@IndexArticleActivity)
            adapter = mArticleAdapter.withLoadStateFooter(DefaultPagingFooterAdapter {
                mArticleAdapter.retry()
            })
            setDefaultOverScrollMode()
        }
    }

    private fun initOnClick() {
        mArticleAdapter.setOnItemClickListener { _, index, itemData ->
            showToast("item 点击事件：${itemData?.title}")
        }
        mArticleAdapter.addOnItemChildViewClickListener(R.id.iv_star_collect) { _, position, itemData ->
            if (itemData == null) {
                return@addOnItemChildViewClickListener
            }
            itemData.isCollect = if (itemData.isCollect != null) !itemData.isCollect!! else true
            mArticleAdapter.notifyItemChanged(position)
        }
    }

    override fun initData() {
        lifecycleScope.launchWhenCreated {
            mViewModel.requestIndexArticlePagingData().collect {
                mArticleAdapter.submitData(it)
            }
        }

        mArticleAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.progressCircular.visibility = if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.rvArticles.visibility = View.VISIBLE
                        View.GONE
                    } else {
                        binding.rvArticles.visibility = View.GONE
                        View.VISIBLE
                    }
                }
                is LoadState.NotLoading -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.rvArticles.visibility = View.VISIBLE
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
                is LoadState.Error -> {
                    val error = it.refresh as LoadState.Error
                    binding.progressCircular.visibility = View.GONE
                    showToast("请求失败：" + error.error.message)
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }
}