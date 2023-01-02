package com.gfs.helper.common.base.paging

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.core.util.size
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * 封装 PagingAdapter 相关逻辑
 *
 * 支持设置itemView单击、长按，及子View单击、长按事件
 */
abstract class BasePagingAdapter<T : BasePagingData , VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T> = DefaultComparator()
) : PagingDataAdapter<T, VH>(diffCallback) {

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mOnItemChildViewClickListener: OnItemChildViewClickListener<T>? = null

    private val mChildViewClickArray by lazy {
        SparseArray<OnItemChildViewClickListener<T>>(3)
    }
    private val mChildViewLongClickArray by lazy {
        SparseArray<OnItemChildViewLongClickListener<T>>(3)
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent.context, parent, viewType).apply {
            onBindingItemClickListener(this)
        }
    }

    /**
     * 绑定 item 点击时间
     */
    protected open fun onBindingItemClickListener(viewHolder: VH) {
        val itemView = viewHolder.itemView
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

        for (i in 0 until mChildViewClickArray.size()) {
            // 根据下标获取到对应的key，也就是viewId
            val viewId = mChildViewClickArray.keyAt(i)
            val childView = itemView.findViewById<View?>(viewId)
            childView?.setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                onItemChildViewClick(it, position)
            }
        }

        for (i in 0 until mChildViewLongClickArray.size()) {
            // 根据下标获取到对应的key，也就是viewId
            val viewId = mChildViewLongClickArray.keyAt(i)
            val childView = itemView.findViewById<View?>(viewId)
            childView?.setOnLongClickListener {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                onItemChildViewLongClick(it, position)
            }
        }

    }

    /**
     * item 点击回调，如需要，可重写此逻辑
     */
    protected open fun onItemClick(view: View, position: Int) {
        mOnItemClickListener?.invoke(view, position, peek(position))
    }

    /**
     * item 长按回调，如需要，可重写此逻辑
     */
    protected open fun onItemLongClick(view: View, position: Int) : Boolean {
        return mOnItemLongClickListener?.invoke(view, position, peek(position)) ?: false
    }

    /**
     * childView 点击回调
     */
    protected open fun onItemChildViewClick(view: View, position: Int) {
        mChildViewClickArray.get(view.id)?.invoke(view, position, peek(position))
    }

    /**
     * childView 点击回调
     */
    protected open fun onItemChildViewLongClick(view: View, position: Int) : Boolean {
        return mChildViewLongClickArray.get(view.id)?.invoke(view, position, peek(position)) ?: false
    }

    /**
     * 创建 ViewHolder
     */
    protected abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int,) : VH

    /**
     * 设置 itemView 点击事件
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        mOnItemClickListener = onItemClickListener
    }

    /**
     * 设置 itemView 长按事件
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    /**
     * 添加子View的点击事件
     */
    fun addOnItemChildViewClickListener(childViewId: Int, onItemChildViewClickListener: OnItemChildViewClickListener<T>) {
        mChildViewClickArray.put(childViewId, onItemChildViewClickListener)
    }

    /**
     * 添加子View的长按事件
     */
    fun addOnItemChildViewLongClickListener(childViewId: Int, onItemChildViewLongClickListener: OnItemChildViewLongClickListener<T>) {
        mChildViewLongClickArray.put(childViewId, onItemChildViewLongClickListener)
    }
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

typealias OnItemClickListener <T> = (view: View, position: Int, itemData: T?) -> Unit

typealias OnItemLongClickListener <T> = (view: View, position: Int, itemData: T?) -> Boolean?

typealias OnItemChildViewClickListener <T> = (childView: View, position: Int, itemData: T?) -> Unit

typealias OnItemChildViewLongClickListener <T> = (childView: View, position: Int, itemData: T?) -> Boolean?
