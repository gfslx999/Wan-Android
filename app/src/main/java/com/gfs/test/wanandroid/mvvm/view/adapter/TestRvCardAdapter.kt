package com.gfs.test.wanandroid.mvvm.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gfs.test.base.util.ToastUtil
import com.gfs.test.wanandroid.R
import com.gfs.test.wanandroid.databinding.ItemIndexArticleBinding
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel

class TestRvCardAdapter(list: List<String>) :
    BaseQuickAdapter<String, TestRvCardAdapter.VH>(list) {

    class VH(
        parent: ViewGroup,
        val binding: ItemIndexArticleBinding = ItemIndexArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {
        if (item == null) {
            return
        }
        holder.binding.let { binding ->
            binding.tvTitle.text = item

            binding.root.setOnClickListener {
                ToastUtil.showToast(context, item)
            }
        }
    }
}