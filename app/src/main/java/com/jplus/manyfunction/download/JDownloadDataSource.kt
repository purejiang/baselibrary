package com.jplus.manyfunction.download

import android.content.Context
import com.nice.baselibrary.base.db.JDataSource
import com.nice.baselibrary.base.entity.vo.JDownloadInfo



/**
 * 下载数据源
 * @author JPlus
 * @date 2019/3/7.
 */
class JDownloadDataSource(context: Context) : JDataSource<JDownloadInfo>() {
    private val mDownloadDataHelper: DownloadDbHelper by lazy {
        DownloadDbHelper(context)
    }

    override fun getDataList(pages: Int, limit: Int): MutableList<JDownloadInfo>? {
        return mDownloadDataHelper.query(pages, limit)
    }

    override fun addData(data: JDownloadInfo): Boolean {
       return mDownloadDataHelper.add(data)
    }

    override fun addDataList(dataList: MutableList<JDownloadInfo>): Boolean {
        var result = true
        dataList.forEach { result = result&&mDownloadDataHelper.add(it) }
        return result
    }

    override fun removeDataList(dataList: MutableList<JDownloadInfo>): Boolean {
        return mDownloadDataHelper.remove(dataList)
    }

    override fun modifyData(data: JDownloadInfo): Boolean {
        return mDownloadDataHelper.update(data)
    }

    override fun removeData(data: JDownloadInfo): Boolean {
        return mDownloadDataHelper.remove(data)
    }

    override fun removeAllData(): Boolean {
        return mDownloadDataHelper.removeAll()
    }

    override fun getData(map:MutableMap<String, out String>): MutableList<JDownloadInfo> {
        return mDownloadDataHelper.query(map)
    }
    override fun getData(id:Int): JDownloadInfo? {
        return mDownloadDataHelper.query(id)
    }
    override fun getAllData(): MutableList<JDownloadInfo>? {
        return mDownloadDataHelper.queryAll()
    }


}