package com.nice.baselibrary.base.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 适配器基类
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class NiceAdapter<T>(private val mItems:ArrayList<T>): RecyclerView.Adapter<NiceAdapter.VH>() {
    /**
     * 获取Layout
     * @param viewType
     */
    abstract fun getLayout(viewType:Int):Int
    /**
     * 提供绑定视图及item
     * @param holder
     * @param item
     * @param position
     */
    abstract fun convert(holder:VH, item:T, position: Int)
    /**
     * 添加删除item
     * @param item
     */
    abstract  fun addItem(item:T)
    /**
     * 添加列表中所有item
     * @param items
     */
    abstract  fun addItems(items:ArrayList<T>)
    /**
     * 删除item
     * @param item
     */
    abstract  fun deleteItem(item:T)

    /**
     * 删除指定位置的item
     * @param position
     */
    abstract  fun deleteItem(position: Int)

    /**
     * 删除列表中所有item
     * @param items
     */
    abstract  fun deleteItems(items:ArrayList<T>)
    /**
     * 获取指定位置的item
     * @param position
     */
    abstract  fun getItem(position:Int):T
    /**
     * 刷新所有item
     * @param items
     */
    abstract  fun refreshItems(items:ArrayList<T>)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH.get(parent, getLayout(viewType))
    }

    override fun onBindViewHolder(holder: VH?, position: Int, payloads: MutableList<Any>?) {
        super.onBindViewHolder(holder, position, payloads)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        convert(holder, mItems[position], position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface ItemClickListener<in T>{
        /**
         * item的点击事件
         * @param holder
         * @param item
         * @param position
         */
        fun setItemClick(holder:VH, item:T, position:Int)
        /**
         * item的长按事件
         * @param holder
         * @param item
         * @param position
         * @return
         */
        fun setItemLongClick(holder:VH, item:T, position:Int):Boolean
    }

    abstract fun setItemClickListener(itemClickListener: ItemClickListener<T>)


    class VH(private val mContentView: View): RecyclerView.ViewHolder(mContentView) {
        companion object {
            /**
             * 获取ViewHolder
             * @param parent
             * @param layoutId
             * @return
             */
            fun get(parent: ViewGroup?, layoutId:Int):VH{
                return VH(LayoutInflater.from(parent?.context).inflate(layoutId, parent, false))
            }
        }
        //储存itemView的控件
        private var mSpareArray:SparseArray<View>?=null

        init{
            mSpareArray = SparseArray()
        }

        /**
         * 通过id获取view
         * @param id
         * @return
         */
        fun <T:View>getView(id:Int):T{
            var view = mSpareArray?.get(id)
            if(view==null){
                view = mContentView.findViewById(id)
                mSpareArray?.put(id, view)
            }
            return view as T
        }
    }
}