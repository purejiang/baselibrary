package com.jplus.manyfunction.contract

import com.nice.baselibrary.base.mvp.BasePresenter
import com.nice.baselibrary.base.mvp.BaseView
import com.nice.baselibrary.base.entity.vo.DownloadInfo
import com.jplus.manyfunction.download.DownloadCallback

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface DownloadListContract {
    interface View : BaseView<Presenter> {
        /**
         * 展现列表
         */
        fun showData(items: MutableList<DownloadInfo>)
        /**
         * 添加下载项
         */
        fun addDownload(item: DownloadInfo)
        /**
         * 移除下载项
         */
        fun removeDownloads(items: MutableList<DownloadInfo>)
        /**
         * 添加下载项
         */
        fun addDownloads(downloads: MutableList<DownloadInfo>)
        /**
         * 已存在提示
         */
        fun downloadIsExist(message:String)
        /**
         * 空列表提示
         */
        fun showEmptyList()

    }

    interface Presenter: BasePresenter {
        /**
         * 获取下载状态
         * @param id
         * @return String
         */
//        fun getStatus(id:Int):String

        /**
         * 添加新下载项
         * @param url
         * @param dirPath
         * @return
         */
        fun addDownload(url: String, dirPath:String)

        /**
         * 添加新下载项
         */
        fun addDownloads(downloads:MutableList<DownloadInfo>)

        /**
         * 删除下载项
         */
        fun removeDownloads(downloads: MutableList<DownloadInfo>)

        /**
         * 暂停下载
         */
        fun controlDownload(downloadInfo: DownloadInfo, downloadCallback: DownloadCallback)
        /**
         * 重新绑定
         */
        fun reBindListener(downloadInfo: DownloadInfo, downloadCallback: DownloadCallback)

        /**
         * 是否队列中
         */
        fun isInQueue(id: Int):Boolean
//        /**
//         * 是否存在数据库
//         */
//        fun isInDataBase(id: Int):Boolean


    }
}