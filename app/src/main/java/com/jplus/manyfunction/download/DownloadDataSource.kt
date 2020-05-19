package com.jplus.manyfunction.download

import android.content.Context
import com.nice.baselibrary.base.source.DataSource
import com.nice.baselibrary.base.entity.vo.DownloadInfo



/**
 * 下载数据源
 * @author JPlus
 * @date 2019/3/7.
 */
class DownloadDataSource(private val mDownloadDbHelper: DownloadDbHelper) : DataSource<DownloadInfo> {

    override fun addData(data: DownloadInfo, callBack: (result: Boolean) -> Unit) {
       callBack(mDownloadDbHelper.add(data))
    }

    override fun addDataList(dataList: MutableList<DownloadInfo>, callBack: (result: Boolean) -> Unit) {
        var result = true
        dataList.forEach { result = result&&mDownloadDbHelper.add(it) }
        callBack(result)
    }

    override fun removeData(data: DownloadInfo, callBack: (result: Boolean) -> Unit) {
        callBack(mDownloadDbHelper.remove(data))
    }

    override fun removeDataList(dataList: MutableList<DownloadInfo>, callBack: (result: Boolean) -> Unit) {
        callBack(mDownloadDbHelper.remove(dataList))
    }

    override fun removeAllData(callBack: (result: Boolean) -> Unit) {
        callBack(mDownloadDbHelper.removeAll())
    }

    override fun modifyData(data: DownloadInfo, callBack: (result: Boolean) -> Unit) {
        callBack(mDownloadDbHelper.update(data))
    }

    override fun getData(map: MutableMap<String, out String>, callback: DataSource.LoadDataListCallback<DownloadInfo>) {
        callback.onDataLoaded(mDownloadDbHelper.query(map))
    }

    override fun getData(id: Int, callback: DataSource.LoadDataCallback<DownloadInfo>) {
        callback.onDataLoaded(mDownloadDbHelper.query(id))
    }

    override fun getDataList(pages: Int, limit: Int, callback: DataSource.LoadDataListCallback<DownloadInfo>) {
        callback.onDataLoaded(mDownloadDbHelper.query(pages, limit))
    }

    override fun getAllData(callback: DataSource.LoadDataListCallback<DownloadInfo>) {
        callback.onDataLoaded(mDownloadDbHelper.queryAll())
    }

}