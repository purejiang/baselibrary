package com.jplus.manyfunction.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jplus.manyfunction.R
import com.nice.baselibrary.widget.BaseCircleProgress
import com.nice.baselibrary.widget.NiceTextView
import com.nice.baselibrary.base.net.download.JDownloadInfo
import com.nice.baselibrary.base.net.download.JDownloadCallback
import com.nice.baselibrary.base.utils.LogUtils

/**
 * 下载列表适配器
 * @author JPlus
 * @date 2019/1/16.
 */
class DownloadAdapter(private val mItems:MutableList<JDownloadInfo>): RecyclerView.Adapter<DownloadAdapter.VH>() {

    private var mItemClickListener: ItemClickListener?=null


     private fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }


     fun addItem(item: JDownloadInfo) {
         mItems.add(0, item)
        notifyItemChanged(0)
    }

     fun deleteItem(item: JDownloadInfo) {

    }

     fun refreshItems(items: MutableList<JDownloadInfo>) {

    }
     fun getItem(position: Int): JDownloadInfo {
        return mItems[position]
    }
     fun setItemClickListener(itemClickListener: ItemClickListener){
        mItemClickListener = itemClickListener
    }
     fun addItems(items: MutableList<JDownloadInfo>) {

    }

     fun deleteItem(position: Int) {
        mItems.removeAt(position)
        notifyItemChanged(position)
    }

     fun deleteItems(items: MutableList<JDownloadInfo>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.get(parent, getLayout(viewType))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = mItems[position]
        val name = holder.getView<NiceTextView>(R.id.btv_download_item_name)
        val url = holder.getView<NiceTextView>(R.id.btv_download_item_url)
        LogUtils.d("item.read:${item.read}, item.count:${item.count}")
        holder.getView<BaseCircleProgress>(R.id.cpb_download_item).loading(String.format("%.1f", item.read * 100.0 / item.count).toDouble())
        name.text = item.name
        url.text = item.url

        holder.itemView.setOnClickListener {
            mItemClickListener?.setItemClick(holder, item, position)
        }
        holder.itemView.setOnLongClickListener{
            mItemClickListener?.setItemLongClick(holder,item, position)!!
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    interface ItemClickListener{
        /**
         * item的点击事件
         * @param itemView
         * @param item
         * @param position
         */
        fun setItemClick(itemView: VH, item: JDownloadInfo, position:Int)
        /**
         * item的长按事件
         * @param itemView
         * @param item
         * @param position
         * @return
         */
        fun setItemLongClick(itemView: VH, item: JDownloadInfo, position:Int):Boolean
    }



    class VH(private val mContentView: View): RecyclerView.ViewHolder(mContentView), JDownloadCallback {


        companion object {
            /**
             * 获取ViewHolder
             * @param parent
             * @param layoutId
             * @return
             */
            fun get(parent: ViewGroup?, layoutId:Int): VH {
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
        override fun update(read: Long, count: Long, done: Boolean) {
            mContentView.findViewById<BaseCircleProgress>(R.id.cpb_download_item).loading(String.format("%.1f", read * 100.0 / count).toDouble())
        }
        override fun downloadSuccess() {
            mContentView.findViewById<BaseCircleProgress>(R.id.cpb_download_item).success()
        }

        override fun downloadFailed(e: Throwable) {
            mContentView.findViewById<BaseCircleProgress>(R.id.cpb_download_item).failed()
        }
        override fun pause(read: Long, count: Long, done: Boolean) {

        }

        override fun downloadCancel() {
            mContentView.findViewById<BaseCircleProgress>(R.id.cpb_download_item).cancel()
        }
    }
}