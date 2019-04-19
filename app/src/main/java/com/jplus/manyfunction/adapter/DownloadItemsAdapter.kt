package com.jplus.manyfunction.adapter

import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.base.ui.view.NiceTextView
import com.nice.baselibrary.download.NiceDownloadInfo

/**
 * @author JPlus
 * @date 2019/3/19.
 */
class DownloadItemsAdapter(private val niceDownloads:ArrayList<NiceDownloadInfo>):NiceAdapter<NiceDownloadInfo>(niceDownloads) {

    private var mItemClickListener:ItemClickListener<NiceDownloadInfo>?=null


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }

    override fun convert(holder: VH, item: NiceDownloadInfo, position: Int) {
        val name = holder.getView<NiceTextView>(R.id.btv_download_item_name)
        val url = holder.getView<NiceTextView>(R.id.btv_download_item_url)
        name.text = item.name
        url.text = item.url

        holder.itemView.setOnClickListener {
            mItemClickListener?.setItemClick(holder, item, position)
        }
       holder.itemView.setOnLongClickListener{
           mItemClickListener?.setItemLongClick(holder,item, position)!!
       }
    }

    override fun addItem(item: NiceDownloadInfo) {
        niceDownloads.add(0, item)
        notifyItemChanged(0)
    }

    override fun deleteItem(item: NiceDownloadInfo) {

    }

    override fun refreshItems(items: ArrayList<NiceDownloadInfo>) {

    }
    override fun getItem(position: Int): NiceDownloadInfo {
        return niceDownloads[position]
    }
    override fun setItemClickListener(itemClickListener: ItemClickListener<NiceDownloadInfo>){
        mItemClickListener = itemClickListener
    }
    override fun addItems(items: ArrayList<NiceDownloadInfo>) {

    }

    override fun deleteItem(position: Int) {
        niceDownloads.removeAt(position)
        notifyItemChanged(position)
    }

    override fun deleteItems(items: ArrayList<NiceDownloadInfo>) {

    }

}