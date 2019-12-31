package com.jplus.manyfunction.contract

import android.net.Uri
import com.nice.baselibrary.base.rx.NicePresenter
import com.nice.baselibrary.base.rx.NiceBaseView
import com.nice.baselibrary.base.net.download.vo.JDownloadInfo
import com.nice.baselibrary.base.net.download.listener.JDownloadCallback

/**
 * @author JPlus
 * @date 2019/2/13.
 */
interface DownloadListContract {
    interface View : NiceBaseView<Presenter> {
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

    interface Presenter: NicePresenter {
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