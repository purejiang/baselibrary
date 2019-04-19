package com.jplus.manyfunction.contract

import com.nice.baselibrary.base.rx.NicePresenter
import com.nice.baselibrary.base.ui.NiceBaseView
import com.nice.baselibrary.download.NiceDownloadInfo
import com.nice.baselibrary.download.NiceDownloadListener

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
         */
        fun addDownload(url: String)
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