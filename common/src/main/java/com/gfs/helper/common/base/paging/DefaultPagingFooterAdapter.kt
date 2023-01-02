package com.gfs.helper.common.base.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gfs.helper.common.databinding.CommonDefaultPagingFooterBinding

/**
 * Paging 默认尾部局
 *
 * 自带失败后重试、Loading状态
 */
class DefaultPagingFooterAdapter(private val onRetry: () -> Unit) : LoadStateAdapter<DefaultPagingFooterAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: CommonDefaultPagingFooterBinding = CommonDefaultPagingFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, loadState: LoadState) {
        val binding = holder.binding
        when (loadState) {
            is LoadState.Loading -> {
                binding.commonBtnRetry.visibility = View.GONE
                binding.commonProgressBar.visibility = View.VISIBLE
            }
            is LoadState.NotLoading -> {
                binding.commonBtnRetry.visibility = View.GONE
                binding.commonProgressBar.visibility = View.GONE
            }
            is LoadState.Error -> {
                binding.commonBtnRetry.visibility = View.VISIBLE
                binding.commonProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VH {
        val vh = VH(parent)
        vh.binding.commonBtnRetry.setOnClickListener {
            onRetry.invoke()
        }
        return vh
    }


}