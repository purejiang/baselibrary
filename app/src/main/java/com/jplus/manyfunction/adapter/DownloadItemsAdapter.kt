package com.jplus.manyfunction.adapter

import com.jplus.manyfunction.R
import com.nice.baselibrary.base.adapter.NiceAdapter
import com.nice.baselibrary.widget.NiceTextView
import com.nice.baselibrary.base.net.download.JDownloadInfo

/**
 * @author JPlus
 * @date 2019/3/19.
 */
class DownloadItemsAdapter(private val jDownloads:ArrayList<JDownloadInfo>):NiceAdapter<JDownloadInfo>(jDownloads) {


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getLayout(viewType: Int): Int {
        return R.layout.view_download_item
    }

    override fun convert(holder: VH, item: JDownloadInfo, position: Int, payloads: MutableList<Any>?) {
        val name = holder.getView<NiceTextView>(R.id.btv_download_item_name)
        val url = holder.getView<NiceTextView>(R.id.btv_download_item_url)
        name.text = item.name
        url.text = item.url

    }

    override fun addItem(item: JDownloadInfo, position: Int) {
        jDownloads.add(0, item)
        notifyItemChanged(0)
    }

    override fun deleteItem(item: JDownloadInfo) {

    }


    override fun getItem(position: Int): JDownloadInfo {
        return jDownloads[position]
    }



    override fun deleteItem(position: Int) {
        jDownloads.removeAt(position)
        notifyItemChanged(position)
    }

    fun deleteItems(items: MutableList<JDownloadInfo>) {

    }

}