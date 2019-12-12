package com.jplus.manyfunction.presenter

import android.net.Uri
import com.jplus.manyfunction.contract.DownloadListContract
import com.nice.baselibrary.base.net.download.*
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.StringUtils
import com.nice.baselibrary.base.utils.getDateTimeByMillis
import java.io.File
import java.util.*


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListPresenter(private val mView: DownloadListContract.View?, private val mDataSource: JDownloadDataSource) : DownloadListContract.Presenter {
    private val mDownloadsManager: JDownloadManager by lazy {
        JDownloadManager()
    }

    init {
        mView?.setPresenter(this)
    }

    override fun subscribe() {
        mDataSource.getAllData()?.let{
            mView?.showData(it)
        }
    }

    override fun unSubscribe() {

    }

    override fun addDownload(url: String, dirPath: String): Uri {
        val downloadList = mDataSource.getData(mutableMapOf(Pair("url", url)))
        val name = StringUtils.parseUrlName(url)
        if (downloadList.size == 0) {
            LogUtils.d("addDownload:url:$url, path:$dirPath")
            val download = JDownloadInfo(0, name, url, dirPath + File.separator + name, Date(System.currentTimeMillis()).getDateTimeByMillis(false), "",0, 0, JDownloadState.DOWNLOAD_READY)
            mDataSource.addData(download)
            mView?.addDownload(download)
        }
        return Uri.parse(dirPath + File.separator + name)
    }


    override fun startDownload(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback) {
        //判断下载状态
        when {
            //准备下载->下载
            jDownloadInfo.status == JDownloadState.DOWNLOAD_READY -> {
                jDownloadInfo.status = JDownloadState.DOWNLOAD_ING
                mDataSource.modifyData(jDownloadInfo)
                mDownloadsManager.startNewDownload(jDownloadInfo, jDownloadCallback, mDataSource)
            }
            //下载->暂停
            jDownloadInfo.status == JDownloadState.DOWNLOAD_ING -> {
                jDownloadInfo.status = JDownloadState.DOWNLOAD_PAUSE
                mDataSource.modifyData(jDownloadInfo)
                mDownloadsManager.pauseDownload(jDownloadInfo)
            }
            //暂停->继续下载
            jDownloadInfo.status == JDownloadState.DOWNLOAD_PAUSE -> {
                jDownloadInfo.status = JDownloadState.DOWNLOAD_ING
                mDataSource.modifyData(jDownloadInfo)
                mDownloadsManager.reStartDownload(jDownloadInfo, jDownloadCallback, mDataSource)
            }
        }
    }

    override fun addDownloads(jDownloads: MutableList<JDownloadInfo>) {
        mDataSource.addDataList(jDownloads)
        mView?.addDownloads(jDownloads)
    }

    override fun removeDownload(position: Int) {
        mDataSource.getAllData()?.get(position)?.let{
            mDataSource.removeData(it)
        }
        mView?.removeDownload(position)
    }
}