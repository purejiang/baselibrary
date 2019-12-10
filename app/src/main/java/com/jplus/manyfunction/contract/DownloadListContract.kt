package com.jplus.manyfunction.contract

import android.net.Uri
import com.nice.baselibrary.base.rx.NicePresenter
import com.nice.baselibrary.base.rx.NiceBaseView
import com.nice.baselibrary.base.net.download.NiceDownloadInfo
import com.nice.baselibrary.base.net.download.NiceDownloadListener

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface DownloadListContract {
    interface ViewNice : NiceBaseView<Presenter> {
        /**
         * 展现列表
         */
        fun showData(items: MutableList<NiceDownloadInfo>)
        /**
         * 添加下载项
         */
        fun addDownload(item: NiceDownloadInfo)
        /**
         * 移除下载项
         */
        fun removeDownload(position: Int)
        /**
         * 添加下载项
         */
        fun addDownloads(niceDownloads: MutableList<NiceDownloadInfo>)
        /**
         * 空列表提示
         */
        fun showEmptyList()

    }

    interface Presenter: NicePresenter {
        /**
         * 添加新下载项
         * @param url
         * @param dirPath
         * @return
         */
        fun addDownload(url: String, dirPath:String): Uri
        /**
         * 开启下载
         */
        fun startDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener)
        /**
         * 添加新下载项
         */
        fun addDownloads(niceDownloads:MutableList<NiceDownloadInfo>)
        /**
         * 移除下载项
         */
        fun removeDownload(position:Int)
    }
}