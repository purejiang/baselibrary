package com.jplus.manyfunction.adapter

import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.jplus.manyfunction.MyApplication
import com.jplus.manyfunction.R
import com.jplus.manyfunction.download.JDownloadManager
import com.nice.baselibrary.base.net.download.common.JDownloadState
import com.nice.baselibrary.base.net.download.listener.JDownloadCallback
import com.nice.baselibrary.base.net.download.vo.JDownloadInfo
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.StringUtils
import com.nice.baselibrary.widget.BaseCircleProgress
import com.nice.baselibrary.widget.JTextView

/**
 * 下载列表适配器
 * @author JPlus
 * @date 2019/1/16.
 */
class DownloadAdapter(private var mItems: MutableList<JDownloadInfo>,private val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<DownloadAdapter.VH>() {
    private var mItemBindListener:ItemBindListener?=null
    private var mIsShowCheck = false
    private var mCheck = false

    private fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }

    fun addItem(item: JDownloadInfo) {
        Log.d("pipa", "addItem$item")
        mItems.add(item)
        notifyItemInserted(mItems.size-1)
    }

    fun setItemBindListener(itemBindListener:ItemBindListener){
        mItemBindListener = itemBindListener
    }

    fun deleteItems(infos: MutableList<JDownloadInfo>) {
        for(info in infos){
            mItems.remove(info)
        }
        notifyDataSetChanged()
    }

    fun showCheck(boolean: Boolean){
        mIsShowCheck = boolean
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.get(parent, getLayout(viewType))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = mItems[position]
        Log.e("pipa", "item---:$item")
            holder.getView<CheckBox>(R.id.cb_download_check).visibility = if (mIsShowCheck) {
                View.VISIBLE
            } else {
                View.GONE
            }
        holder.getView<JTextView>(R.id.btv_download_item_name).text = item.name

        mItemBindListener?.onBindListener(holder, item, position)?.let{
            if(it){
                holder.getView<BaseCircleProgress>(R.id.bcp_download_item).loading(String.format("%.1f", item.read * 100.0 / item.count).toDouble())
                holder.getView<JTextView>(R.id.btv_download_item_ratio).text = "${StringUtils.parseByteSize(item.read)}/${StringUtils.parseByteSize(item.count)}"
            }
        }

        holder.itemView.setOnClickListener {
            mItemClickListener.setItemClick(holder, item, position)
        }
        holder.itemView.setOnLongClickListener{
            mItemClickListener.setItemLongClick(holder, item, position)
        }
        holder.getView<CheckBox>(R.id.cb_download_check).setOnCheckedChangeListener { buttonView, isChecked ->
            mItemClickListener.setItemCheck(holder, item, position, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface ItemBindListener {
        /**
         * item的Bind
         * @param itemView
         * @param item
         * @param position
         * @return 是否刷新进度
         */
        fun onBindListener(itemView: VH, item: JDownloadInfo, position: Int):Boolean
    }
    interface ItemClickListener {
        /**
         * item的点击事件
         * @param itemView
         * @param item
         * @param position
         */
        fun setItemClick(itemView: VH, item: JDownloadInfo, position: Int)
        /**
         * item的选择事件
         * @param itemView
         * @param item
         * @param position
         * @param checked
         */
        fun setItemCheck(itemView: VH, item: JDownloadInfo, position: Int, checked: Boolean)
        /**
         * item的长按事件
         * @param itemView
         * @param item
         * @param position
         * @return
         */
        fun setItemLongClick(itemView: VH, item: JDownloadInfo, position:Int):Boolean
    }


    class VH(private val mContentView: View) : RecyclerView.ViewHolder(mContentView) {
        companion object {
            /**
             * 获取ViewHolder
             * @param parent
             * @param layoutId
             * @return
             */
            fun get(parent: ViewGroup?, layoutId: Int): VH {
                return VH(LayoutInflater.from(parent?.context).inflate(layoutId, parent, false))
            }
        }

        //储存itemView的控件
        private var mSpareArray: SparseArray<View>? = null

        init {
            mSpareArray = SparseArray()
        }

        /**
         * 通过id获取view
         * @param id
         * @return
         */
        fun <T : View> getView(id: Int): T {
            var view = mSpareArray?.get(id)
            if (view == null) {
                view = mContentView.findViewById(id)
                mSpareArray?.put(id, view)
            }
            return view as T
        }



    }
}