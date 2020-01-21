package com.jplus.manyfunction.presenter

import android.content.Context
import android.util.Log
import com.jplus.manyfunction.contract.DownloadListContract
import com.jplus.manyfunction.download.JDownloadDataSource
import com.jplus.manyfunction.download.JDownloadManager
import com.nice.baselibrary.base.net.download.JDownloadState
import com.nice.baselibrary.base.net.download.listener.JDownloadCallback
import com.nice.baselibrary.base.net.download.vo.JDownloadInfo
import com.nice.baselibrary.base.utils.LogUtils
import com.nice.baselibrary.base.utils.StringUtils
import java.io.File


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListPresenter(context: Context, private val mView: DownloadListContract.View) : DownloadListContract.Presenter {


    private val mDataSource: JDownloadDataSource by lazy {
        JDownloadDataSource(context)
    }

    init {
        mView.setPresenter(this)
    }

    override fun subscribe() {
        val infoList = mDataSource.getAllData()
        infoList?.let {
            LogUtils.d("getAllData:it:$it")
            if (it.size == 0) {
                mView.showData(it)
            } else {
                mView.showData(it)
            }
        }

    }

    override fun isInQueue(id: Int): Boolean {
        return JDownloadManager.isInQueue(id)
    }

    override fun unSubscribe() {

    }

    override fun addDownload(url: String, dirPath: String) {
        val name = StringUtils.parseUrlName(url)
        mDataSource.getData(mutableMapOf(Pair("url", url), Pair("name", name))).let {
            if (it.size == 0) {
                val filePath = dirPath + File.separator + name
                val download = JDownloadInfo(0, name, url, filePath, "${System.currentTimeMillis()}", "", 0L, 0L, JDownloadState.DOWNLOAD_READY)
                mDataSource.addData(download)?.let { info ->
                    mView.addDownload(info)
                }
            } else {
                mView.downloadIsExist("$name 已在下载列表中")
            }
        }
    }

    override fun getStatus(id: Int): String {
        return mDataSource.getData(id)?.status ?: JDownloadState.DOWNLOAD_UNKNOWN
    }


    override fun controlDownload(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback) {
        when (getStatus(jDownloadInfo.id)) {
            JDownloadState.DOWNLOAD_ING -> JDownloadManager.pauseDownload(jDownloadInfo.id)
            JDownloadState.DOWNLOAD_PAUSE -> JDownloadManager.reStartDownload(jDownloadInfo.id, jDownloadCallback, mDataSource)
            JDownloadState.DOWNLOAD_READY -> JDownloadManager.addNewDownload(jDownloadInfo, jDownloadCallback, mDataSource, true)
        }

    }

    override fun reBindListener(jDownloadInfo: JDownloadInfo, jDownloadCallback: JDownloadCallback) {
        JDownloadManager.reBindListener(jDownloadInfo.id, jDownloadCallback, mDataSource)
    }
    override fun isInDataBase(id: Int): Boolean {
        return mDataSource.getData(id) != null
    }


    override fun addDownloads(jDownloads: MutableList<JDownloadInfo>) {
        mDataSource.addDataList(jDownloads)
        mView.addDownloads(jDownloads)
    }

    override fun removeDownloads(jDownloads: MutableList<JDownloadInfo>) {
        Log.d("pipa", "removeDownloads:$jDownloads")
        if (mDataSource.removeDataList(jDownloads)) {
            Log.d("pipa", "removeDownloads:True")
        }
        JDownloadManager.deleteDownloads(jDownloads)
        mView.removeDownloads(jDownloads)
    }
}