package com.gfs.test.wanandroid.mvvm.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gfs.helper.common.base.paging.BasePagingAdapter
import com.gfs.test.wanandroid.R
import com.gfs.test.wanandroid.databinding.ItemIndexArticleBinding
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel

class IndexArticleAdapter : BasePagingAdapter<IndexArticleModel.Data, IndexArticleAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ItemIndexArticleBinding = ItemIndexArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val itemData = getItem(position) ?: return
        holder.binding.let { binding ->
            binding.tvTitle.text = itemData.title ?: ""
            binding.tvAuthor.text = itemData.author ?: itemData.shareUser ?: ""
            if (itemData.isCollect == true) {
                binding.ivStarCollect.setImageResource(R.mipmap.icon_grey_star_filled)
            } else {
                binding.ivStarCollect.setImageResource(R.mipmap.icon_grey_star_not_fill)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}