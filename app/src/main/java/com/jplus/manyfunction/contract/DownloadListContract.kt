package com.jplus.manyfunction.contract

import com.nice.baselibrary.base.presenter.BasePresenter
import com.nice.baselibrary.base.BaseView
import com.nice.baselibrary.download.NiceDownloadInfo
import com.nice.baselibrary.download.NiceDownloadListener
import java.util.ArrayList

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface DownloadListContract {
    interface View: BaseView<Presenter> {
        /**
         * 展现列表
         */
        fun showData(items: ArrayList<NiceDownloadInfo>)
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
        fun addDownloads(niceDownloads: ArrayList<NiceDownloadInfo>)
        /**
         * 空列表提示
         */
        fun showEmptyList()

    }

    interface Presenter: BasePresenter {
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
        fun addDownloads(niceDownloads:ArrayList<NiceDownloadInfo>)
        /**
         * 移除下载项
         */
        fun removeDownload(position:Int)
    }
}