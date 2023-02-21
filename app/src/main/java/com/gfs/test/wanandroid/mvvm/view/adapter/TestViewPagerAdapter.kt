package com.gfs.test.wanandroid.mvvm.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gfs.test.wanandroid.R

class TestViewPagerAdapter(private val mContext: Context, private val mDataList: List<String>) : BaseAdapter() {

    override fun getCount(): Int = mDataList.size

    override fun getItem(position: Int): String = mDataList.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val newConvertView: View?

        val viewHolder = if (convertView == null) {
            newConvertView = LayoutInflater.from(mContext).inflate(R.layout.item_banner_test, null)

            val newViewHolder = ViewHolder()
            newViewHolder.mTextView = newConvertView!!.findViewById(R.id.tv_title) as TextView
            newConvertView.tag = newViewHolder
            newViewHolder
        } else {
            newConvertView = convertView

            convertView.tag as ViewHolder
        }

        viewHolder.mTextView?.text = getItem(position)
        return newConvertView
    }

    internal class ViewHolder {
        var mTextView: TextView? = null
    }
}