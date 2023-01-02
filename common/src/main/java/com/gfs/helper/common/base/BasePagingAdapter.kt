package com.gfs.helper.common.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gfs.test.base.util.LogUtil

/**
 * 封装 PagingAdapter 相关逻辑
 *
 * 支持设置itemView单击、长按，及子View单击、长按事件
 */
abstract class BasePagingAdapter<T : BasePagingData , VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T> = DefaultComparator()
) : PagingDataAdapter<T, VH>(diffCallback) {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildViewClickListener: OnItemChildViewClickListener? = null

    private val mBindChildViewIdList = mutableListOf<Int>()
//    private var mOnItemChildClickListener:

//    override fun onBindViewHolder(holder: VH, position: Int) {
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent.context, parent, viewType).apply {
            onBindingItemClickListener(this)
        }
    }

    /**
     * 绑定 item 点击时间
     */
    protected open fun onBindingItemClickListener(viewHolder: VH) {
        val itemView = viewHolder.itemView
        LogUtil.logI("onBindingItemClickListener mOnItemClickListener: $mOnItemClickListener")
        mOnItemClickListener?.let {
            itemView.setOnClickListener { view ->
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                onItemClick(view, position)
            }
        }
        mOnItemLongClickListener?.let {
            itemView.setOnLongClickListener { view ->
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                onItemLongClick(view, position)
            }
        }
        mOnItemChildViewClickListener?.let { listener ->
            if (mBindChildViewIdList.isEmpty()) {
                return@let
            }

            for (childViewId in mBindChildViewIdList) {
                val childView = itemView.findViewById<View>(childViewId)
                childView.setOnClickListener {
                    val position = viewHolder.bindingAdapterPosition
                    if (position == RecyclerView.NO_POSITION) {
                        return@setOnClickListener
                    }
                    listener.invoke(it.id, position)
                }
            }
        }

    }

    /**
     * item 点击回调，如需要，可重写此逻辑
     */
    protected open fun onItemClick(view: View, position: Int) {
        mOnItemClickListener?.invoke(view, position)
    }

    /**
     * item 长按回调，如需要，可重写此逻辑
     */
    protected open fun onItemLongClick(view: View, position: Int) : Boolean {
        return mOnItemLongClickListener?.invoke(view, position) ?: false
    }

    /**
     * 创建 ViewHolder
     */
    protected abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int,) : VH

    /**
     * 设置 itemView 点击事件
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    /**
     * 设置 itemView 长按事件
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    /**
     * 设置 itemView 的 子View 点击事件
     */
    fun setOnChildItemClickListener(onItemChildViewClickListener: OnItemChildViewClickListener) {
        mOnItemChildViewClickListener = onItemChildViewClickListener
    }

    fun bindChildItemClick(childViewId: Int?) {
        if (childViewId == null) {
            return
        }
        if (mBindChildViewIdList.contains(childViewId)) {
            return
        }
        mBindChildViewIdList.add(childViewId)
    }

    // 用于 paging 进行比较的默认实现类
    internal class DefaultComparator<T : BasePagingData> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.soleId() == newItem.soleId()
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }

}

typealias OnItemClickListener = (view: View, position: Int) -> Unit

typealias OnItemLongClickListener = (view: View, position: Int) -> Boolean?

typealias OnItemChildViewClickListener = (childViewId: Int, position: Int) -> Unit
