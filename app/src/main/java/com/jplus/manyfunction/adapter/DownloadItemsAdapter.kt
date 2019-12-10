package com.jplus.manyfunction.adapter

import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.widget.NiceTextView
import com.nice.baselibrary.base.net.download.NiceDownloadInfo

/**
 * @author JPlus
 * @date 2019/3/19.
 */
class DownloadItemsAdapter(private val niceDownloads:ArrayList<NiceDownloadInfo>):NiceAdapter<NiceDownloadInfo>(niceDownloads) {


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }

    override fun convert(holder: VH, item: NiceDownloadInfo, position: Int, payloads: MutableList<Any>?) {
        val name = holder.getView<NiceTextView>(R.id.btv_download_item_name)
        val url = holder.getView<NiceTextView>(R.id.btv_download_item_url)
        name.text = item.name
        url.text = item.url

    }

    override fun addItem(item: NiceDownloadInfo, position: Int) {
        niceDownloads.add(0, item)
        notifyItemChanged(0)
    }

    override fun deleteItem(item: NiceDownloadInfo) {

    }


    override fun getItem(position: Int): NiceDownloadInfo {
        return niceDownloads[position]
    }



    override fun deleteItem(position: Int) {
        niceDownloads.removeAt(position)
        notifyItemChanged(position)
    }

    fun deleteItems(items: MutableList<NiceDownloadInfo>) {

    }

}