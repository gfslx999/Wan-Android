package com.gfs.test.wanandroid.mvvm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gfs.test.wanandroid.databinding.ItemIndexArticleBinding
import com.gfs.test.wanandroid.mvvm.model.IndexArticleModel

class IndexArticleAdapter : PagingDataAdapter<IndexArticleModel.Data, IndexArticleAdapter.VH>(Comparator) {


    class VH(
        parent: ViewGroup,
        val binding: ItemIndexArticleBinding = ItemIndexArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<IndexArticleModel.Data>() {
        override fun areItemsTheSame(
            oldItem: IndexArticleModel.Data,
            newItem: IndexArticleModel.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: IndexArticleModel.Data,
            newItem: IndexArticleModel.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val itemData = getItem(position) ?: return
        holder.binding.let { binding ->
            binding.tvTitle.text = itemData.title ?: ""
            binding.tvAuthor.text = itemData.author ?: itemData.shareUser ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}