package com.jplus.manyfunction.presenter

import android.content.Context
import com.jplus.manyfunction.contract.DownloadListContract

import com.nice.baselibrary.base.utils.StringUtils
import com.nice.baselibrary.download.*
import java.util.*


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListPresenter : DownloadListContract.Presenter {


    private var mView: DownloadListContract.View? = null
    private var mDataSourceNice: NiceDownloadDataSource? = null
    private var mDownloadsManagerNice: NiceDownloadManager? = null

    constructor(context: Context, view: DownloadListContract.View?, dataSourceNice: NiceDownloadDataSource) {
        mView = view
        mDataSourceNice = dataSourceNice
        mView?.setPresenter(this)
        mDownloadsManagerNice = NiceDownloadManager.getInstance()
        mDownloadsManagerNice?.init(context)
    }

    override fun subscribe() {
        mView?.showData(mDataSourceNice?.getAllData()!!)
    }

    override fun unSubscribe() {

    }

    override fun addDownload(url: String) {
        var download = mDataSourceNice?.getData(url)
        if (download == null) {
            download = NiceDownloadInfo(0, StringUtils.parseUrlName(url), url, StringUtils.getDateTime(), 0, 0, NiceDownloadManager.DownloadStatus.DOWNLOAD_START)
            mDataSourceNice?.addData(download)
            mView?.addDownload(download)
        }
    }


    override fun startDownload(niceDownloadInfo: NiceDownloadInfo, niceDownloadListener: NiceDownloadListener) {
        //判断下载状态
        when {
            //准备下载->下载
            niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_START -> {
                niceDownloadInfo.status = NiceDownloadManager.DownloadStatus.DOWNLOAD_ING
                mDataSourceNice?.modifyData(niceDownloadInfo)
                mDownloadsManagerNice?.startNewDownload(niceDownloadInfo, niceDownloadListener, mDataSourceNice!!)
            }
            //暂停->继续下载
            niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_PAUSE -> {
                niceDownloadInfo.status = NiceDownloadManager.DownloadStatus.DOWNLOAD_ING
                mDataSourceNice?.modifyData(niceDownloadInfo)
                mDownloadsManagerNice?.reStartDownload(niceDownloadInfo, niceDownloadListener, mDataSourceNice!!)
            }
            //下载->暂停
            niceDownloadInfo.status == NiceDownloadManager.DownloadStatus.DOWNLOAD_ING -> {
                niceDownloadInfo.status = NiceDownloadManager.DownloadStatus.DOWNLOAD_PAUSE
                mDataSourceNice?.modifyData(niceDownloadInfo)
                mDownloadsManagerNice?.pauseDownload(niceDownloadInfo)
            }
        }

    }

    override fun addDownloads(niceDownloads: ArrayList<NiceDownloadInfo>) {
        mDataSourceNice?.addDatas(niceDownloads)
        mView?.addDownloads(niceDownloads)
    }


    override fun removeDownload(position: Int) {
        mDataSourceNice?.removeData(mDataSourceNice?.getAllData()?.get(position)!!)
        mView?.removeDownload(position)
    }


}