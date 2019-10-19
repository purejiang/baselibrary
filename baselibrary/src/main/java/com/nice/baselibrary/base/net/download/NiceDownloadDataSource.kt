package com.nice.baselibrary.base.net.download

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

/**
 * @author JPlus
 * @date 2019/3/7.
 */
class NiceDownloadDataSource(context: Context) {
    private var mNiceDownloadList: MutableList<NiceDownloadInfo> = ArrayList()
    private var mDownloadDataHelper: NiceDownloadDBHelper? = null
    private var mDatabase: SQLiteDatabase? = null

    init {
        mDownloadDataHelper = NiceDownloadDBHelper(context)
        mDatabase = mDownloadDataHelper?.writableDatabase
        mNiceDownloadList = mDownloadDataHelper!!.queryAll(mDatabase)
    }

    fun addData(item: NiceDownloadInfo) : NiceDownloadInfo? {
        mDownloadDataHelper?.add(item, mDatabase)?.let {
            mNiceDownloadList.add(item)
        }
        return item
    }

    fun addDataList(items: MutableList<NiceDownloadInfo>) {
        items.filter { mDownloadDataHelper?.add(it, mDatabase) != null }
                .forEach { mNiceDownloadList.add(it) }

    }

    fun removeData(item: NiceDownloadInfo): NiceDownloadInfo? {
        mDownloadDataHelper?.remove(item, mDatabase)?.let {
            mNiceDownloadList.remove(item)
            return item
        }
        return null
    }

    fun removeDatas(items: MutableList<NiceDownloadInfo>) {
        items.filter { mDownloadDataHelper?.remove(it, mDatabase) != null }
                .forEach { mNiceDownloadList.remove(it) }
    }

    fun deleteAll() {

    }

    fun modifyData(niceDownloadInfo: NiceDownloadInfo) {
        mDownloadDataHelper?.update(niceDownloadInfo, mDatabase)
    }

    fun refreshData(): MutableList<NiceDownloadInfo>? {
        return null
    }

    fun getData(url: String): NiceDownloadInfo? {
        return mDownloadDataHelper?.queryByUrl(url, mDatabase)
    }
    fun getDataByUrl(url: String): NiceDownloadInfo? {

        return mDownloadDataHelper?.queryByUrl(url, mDatabase)
    }

    fun getAllData(): MutableList<NiceDownloadInfo>? {
        return mDownloadDataHelper?.queryAll(mDatabase)
    }
}