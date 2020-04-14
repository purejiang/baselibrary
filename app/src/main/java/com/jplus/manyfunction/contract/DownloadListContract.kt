package com.jplus.manyfunction.contract

import com.nice.baselibrary.base.mvp.BasePresenter
import com.nice.baselibrary.base.mvp.BaseView
import com.nice.baselibrary.base.entity.vo.JDownloadInfo
import com.nice.baselibrary.base.net.download.JDownloadCallback

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface DownloadListContract {
    interface View : BaseView<Presenter> {
        /**
         * 展现列表
         */
        fun showData(items: MutableList<JDownloadInfo>)
        /**
         * 添加下载项
         */
        fun addDownload(item: JDownloadInfo)
        /**
         * 移除下载项
         */
        fun removeDownloads(items: MutableList<JDownloadInfo>)
        /**
         * 添加下载项
         */
        fun addDownloads(jDownloads: MutableList<JDownloadInfo>)
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
        fun getStatus(id:Int):String

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
        fun addDownloads(jDownloads:MutableList<JDownloadInfo>)

        /**
         * 删除下载项
         */
        fun removeDownloads(jDownloads: MutableList<JDownloadInfo>)

        /**
         * 暂停下载
         */
        fun controlDownload(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback)
        /**
         * 重新绑定
         */
        fun reBindListener(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback)

        /**
         * 是否队列中
         */
        fun isInQueue(id: Int):Boolean
        /**
         * 是否存在数据库
         */
        fun isInDataBase(id: Int):Boolean


    }
}