package com.nice.baselibrary.base.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 适配器基类
 * @author JPlus
 * @date 2019/1/16.
 */
abstract class NiceAdapter<T>(private val mItems:MutableList<T>): RecyclerView.Adapter<NiceAdapter.VH>() {
    private var mItemClickListener:ItemClickListener?=null

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
    abstract fun convert(holder:VH, item:T, position: Int, payloads: MutableList<Any>?)
    /**
     * 添加item
     * @param item
     */
    open  fun addItem(item:T, position:Int){
        mItems.add(position, item)
        notifyItemInserted(position)
    }

    /**
     * 删除item
     * @param item
     */
    open  fun deleteItem(item:T){

    }

    /**
     * 删除指定位置的item
     * @param position
     */
    open  fun deleteItem(position: Int){
        mItems.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 获取指定位置的item
     * @param position
     */
    open  fun getItem(position:Int):T{
        return mItems[position]
    }


    /**
     * 设置条目的点击监听
     * @param itemClickListener
     */
     open fun setItemClickListener(itemClickListener: ItemClickListener?){
        this.mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.get(parent, getLayout(viewType))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        convert(holder, mItems[position], position, payloads)

        holder.itemView.setOnClickListener {
            mItemClickListener!!.setItemClick(holder, position)
        }
        holder.itemView.setOnLongClickListener{
            mItemClickListener!!.setItemLongClick(holder, position)
        }

    }
    override fun onBindViewHolder(holder: VH, position: Int) {

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface ItemClickListener{
        /**
         * item的点击事件
         * @param holder
         * @param position
         */
        fun setItemClick(holder:VH, position:Int)
        /**
         * item的长按事件
         * @param holder
         * @param position
         * @return
         */
        fun setItemLongClick(holder:VH, position:Int):Boolean
    }



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