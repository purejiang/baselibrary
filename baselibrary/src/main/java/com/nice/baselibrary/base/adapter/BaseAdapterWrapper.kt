package com.nice.baselibrary.base.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * 通用适配器装饰器
 * @author JPlus
 * @date 2020/4/13.
 */
class BaseAdapterWrapper<T>(private val mAdapter: BaseAdapter<T>,private val mListener:CreatedListener) : RecyclerView.Adapter<BaseAdapter.VH>() {
    internal enum class ItemType {
        HEADER,
        FOOTER,
        NORMAL
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ItemType.HEADER.ordinal
            mAdapter.itemCount + 1 -> ItemType.FOOTER.ordinal
            else -> ItemType.NORMAL.ordinal
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.VH {
        return when (viewType) {
            ItemType.HEADER.ordinal -> BaseAdapter.VH(mListener.setHeadView())
            ItemType.FOOTER.ordinal -> BaseAdapter.VH(mListener.setFootView())
            else -> mAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return mAdapter.itemCount + 2
    }

    override fun onBindViewHolder(holder: BaseAdapter.VH, position: Int) {
        when (position) {
            0 -> return
            mAdapter.itemCount + 1 -> return
            else -> mAdapter.onBindViewHolder(holder, position - 1)
        }
    }
    interface CreatedListener{
        /**
         * 配置头部View
         */
        fun setHeadView():View

        /**
         * 配置结尾处的View
         */
        fun setFootView():View
    }

}