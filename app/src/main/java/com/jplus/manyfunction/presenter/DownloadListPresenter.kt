package com.jplus.manyfunction.presenter

import android.content.Context
import android.util.Log
import com.jplus.manyfunction.contract.DownloadListContract
import com.jplus.manyfunction.download.DownloadCallback
import com.jplus.manyfunction.download.DownloadDataSource
import com.jplus.manyfunction.download.DownloadDbHelper
import com.jplus.manyfunction.download.JDownloadManager
import com.nice.baselibrary.base.source.DataSource
import com.nice.baselibrary.base.entity.vo.DownloadInfo
import com.nice.baselibrary.base.download.DownloadState
import com.nice.baselibrary.base.utils.parseUrlName
import java.io.File


/**
 * @author JPlus
 * @date 2019/2/13.
 */
class DownloadListPresenter(context: Context, private val mView: DownloadListContract.View) : DownloadListContract.Presenter {

    private val mDataSource: DownloadDataSource by lazy {
        DownloadDataSource(DownloadDbHelper(context))
    }

    init {
        mView.setPresenter(this)
    }

    override fun subscribe() {
        mDataSource.getAllData(object : DataSource.LoadDataListCallback<DownloadInfo> {
            override fun onDataLoaded(dataList: MutableList<DownloadInfo>) {
                Log.d("pipa", "getAllData====${dataList.size}")
                mView.showData(dataList)
            }

            override fun onDataNotAvailable(throwable: Throwable) {

            }
        })


    }

    override fun isInQueue(id: Int): Boolean {
        return JDownloadManager.isInQueue(id)
    }

    override fun unSubscribe() {

    }

    override fun addDownload(url: String, dirPath: String) {
        val name = parseUrlName(url)
        mDataSource.getData(mutableMapOf(Pair("url", url), Pair("name", name)), object : DataSource.LoadDataListCallback<DownloadInfo> {
            override fun onDataLoaded(dataList: MutableList<DownloadInfo>) {
                    if (dataList.size == 0) {
                        val filePath = dirPath + File.separator + name
                        val download = DownloadInfo(0, name, url, filePath, "${System.currentTimeMillis()}", "", 0L, 0L, DownloadState.DOWNLOAD_READY)
                        mDataSource.addData(download) { result->
                            if(result) Log.d("pipa", "aaaa")
                        }
                        mDataSource.getData(mutableMapOf(Pair("url", download.url), Pair("name", download.name)), object : DataSource.LoadDataListCallback<DownloadInfo> {
                            override fun onDataLoaded(dataList: MutableList<DownloadInfo>) {
                                mView.addDownload(dataList[0])
                            }

                            override fun onDataNotAvailable(throwable: Throwable) {

                            }
                        })
                    } else {
                        mView.downloadIsExist("$name 已在下载列表中")
                    }
                }

            override fun onDataNotAvailable(throwable: Throwable) {

            }
        })
    }


    override fun controlDownload(downloadInfo: DownloadInfo, downloadCallback: DownloadCallback) {
        mDataSource.getData(downloadInfo.id, object : DataSource.LoadDataCallback<DownloadInfo> {
            override fun onDataLoaded(data: DownloadInfo?) {
                when (data?.status) {
                    DownloadState.DOWNLOAD_ING -> JDownloadManager.pauseDownload(downloadInfo.id)
                    DownloadState.DOWNLOAD_PAUSE -> JDownloadManager.reStartDownload(downloadInfo.id, downloadCallback, mDataSource)
                    DownloadState.DOWNLOAD_READY -> JDownloadManager.addNewDownload(downloadInfo, downloadCallback, mDataSource, true)
                }

            }

            override fun onDataNotAvailable(throwable: Throwable) {

            }
        })

    }

    override fun reBindListener(downloadInfo: DownloadInfo, downloadCallback: DownloadCallback) {
        Log.d("pipa", "onBindListener====$downloadInfo")

        JDownloadManager.reBindListener(downloadInfo.id, downloadCallback, mDataSource)
    }

//    override fun isInDataBase(id: Int): Boolean {
//        return mDataSource.getData(id) != null
//    }


    override fun addDownloads(downloads: MutableList<DownloadInfo>) {
        mDataSource.addDataList(downloads) {
            if (it) mView.addDownloads(downloads)
        }
    }

    override fun removeDownloads(downloads: MutableList<DownloadInfo>) {
        Log.d("pipa", "removeDownloads:$downloads")
        mDataSource.removeDataList(downloads){
            if (it) {
                JDownloadManager.deleteDownloads(downloads)
                mView.removeDownloads(downloads)
            }
        }
    }
}