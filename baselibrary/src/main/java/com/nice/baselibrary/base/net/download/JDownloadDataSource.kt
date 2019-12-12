package com.nice.baselibrary.base.net.download

import android.content.Context

/**
 * 下载数据源
 * @author JPlus
 * @date 2019/3/7.
 */
class JDownloadDataSource(context: Context) {

    private val mDownloadDataHelper: JDownloadDBHelper by lazy {
        JDownloadDBHelper(context)
    }
    private val mDownloadList:MutableList<JDownloadInfo> by lazy{
        mDownloadDataHelper.query(null)
    }

    fun addData(item: JDownloadInfo) : JDownloadInfo? {
        mDownloadDataHelper.add(item).let {
            mDownloadList.add(item)
        }
        return item
    }

    fun addDataList(items: MutableList<JDownloadInfo>) {
        items.filter { mDownloadDataHelper.add(it).size != 0 }
                .forEach { mDownloadList.add(it) }
    }

    fun removeData(item: JDownloadInfo): Boolean {
        return if (mDownloadDataHelper.remove(item)){
            mDownloadList.remove(item)
            true
        }else{
            false
        }
    }

    fun removeDatas(items: MutableList<JDownloadInfo>) {
        items.filter { mDownloadDataHelper.remove(it)}
                .forEach { mDownloadList.remove(it) }
    }

    fun deleteAll() {

    }

    fun modifyData(jDownloadInfo: JDownloadInfo) {
        mDownloadDataHelper.update(jDownloadInfo)
    }

    fun refreshData(): MutableList<JDownloadInfo>? {

        return null
    }

    fun getData(mutableMap: MutableMap<String, String>): MutableList<JDownloadInfo> {
        return mDownloadDataHelper.query(mutableMap)
    }

    fun getAllData(): MutableList<JDownloadInfo>? {
        return mDownloadDataHelper.query(null)
    }
}