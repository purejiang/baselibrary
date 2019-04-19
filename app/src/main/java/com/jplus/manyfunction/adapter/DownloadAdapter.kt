package com.jplus.manyfunction.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jplus.manyfunction.R
import com.nice.baselibrary.base.ui.view.NiceCircleProgress
import com.nice.baselibrary.base.ui.view.NiceTextView
import com.nice.baselibrary.download.NiceDownloadInfo
import com.nice.baselibrary.download.NiceDownloadListener

/**
 * 下载列表适配器
 * @author JPlus
 * @date 2019/1/16.
 */
class DownloadAdapter(private val mItems:MutableList<NiceDownloadInfo>): RecyclerView.Adapter<DownloadAdapter.VH>() {

    private var mItemClickListener: DownloadAdapter.ItemClickListener?=null


     private fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }


     fun addItem(item: NiceDownloadInfo) {
         mItems.add(0, item)
        notifyItemChanged(0)
    }

     fun deleteItem(item: NiceDownloadInfo) {

    }

     fun refreshItems(items: MutableList<NiceDownloadInfo>) {

    }
     fun getItem(position: Int): NiceDownloadInfo {
        return mItems[position]
    }
     fun setItemClickListener(itemClickListener: DownloadAdapter.ItemClickListener){
        mItemClickListener = itemClickListener
    }
     fun addItems(items: MutableList<NiceDownloadInfo>) {

    }

     fun deleteItem(position: Int) {
        mItems.removeAt(position)
        notifyItemChanged(position)
    }

     fun deleteItems(items: MutableList<NiceDownloadInfo>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        return VH.get(parent, getLayout(viewType))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = mItems[position]
        val name = holder.getView<NiceTextView>(R.id.btv_download_item_name)
        val url = holder.getView<NiceTextView>(R.id.btv_download_item_url)
        holder.getView<NiceCircleProgress>(R.id.cpb_download_item).loading(String.format("%.1f", item.read * 100.0 / item.count).toDouble())
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
        fun setItemClick(itemView: DownloadAdapter.VH, item: NiceDownloadInfo, position:Int)
        /**
         * item的长按事件
         * @param itemView
         * @param item
         * @param position
         * @return
         */
        fun setItemLongClick(itemView: DownloadAdapter.VH, item: NiceDownloadInfo, position:Int):Boolean
    }



    class VH(private val mContentView: View): RecyclerView.ViewHolder(mContentView), NiceDownloadListener {


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
            mContentView.findViewById<NiceCircleProgress>(R.id.cpb_download_item).loading(String.format("%.1f", read * 100.0 / count).toDouble())
        }
        override fun downloadSuccess() {
            mContentView.findViewById<NiceCircleProgress>(R.id.cpb_download_item).success()
        }

        override fun downloadFailed(e: Throwable) {
            mContentView.findViewById<NiceCircleProgress>(R.id.cpb_download_item).failed()
        }

        override fun downloadCancel() {
            mContentView.findViewById<NiceCircleProgress>(R.id.cpb_download_item).cancel()
        }
    }
}