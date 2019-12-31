package com.jplus.manyfunction.download

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.nice.baselibrary.base.net.download.db.JDBHelper
import com.nice.baselibrary.base.net.download.vo.JDownloadInfo
import com.nice.baselibrary.base.utils.LogUtils


/**
 * 下载数据源
 * @author JPlus
 * @date 2019/3/7.
 */
class DownloadDbHelper(context: Context, private val table_name: String = "download", db_name: String = "download.db", db_version: Int = 1) : JDBHelper<JDownloadInfo>(context, db_name, null, db_version) {

    override fun onCreated(db: SQLiteDatabase?) {
        val downloadSql = "create table " + table_name + "(" +
                "id integer primary key autoincrement," +
                "name text not null," +
                "url text not null," +
                "path text not null," +
                "start_time text not null," +
                "end_time text not null," +
                "read long not null," +
                "count long not null," +
                "status text not null" + ")"
        db?.execSQL(downloadSql)
    }

    override fun onUpgraded(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun add(data: JDownloadInfo): JDownloadInfo? {
        val db = this.writableDatabase
        LogUtils.d("add:$data")
        db.insert(table_name, null, download2Value(data))
        val sql = "select * from $table_name where name = ? and url = ?"
        return queryDataBase(sql, arrayOf(data.name, data.url))[0]
    }

    @Throws(SQLException::class)
    fun addExec(data: JDownloadInfo): JDownloadInfo? {
        LogUtils.d("addExec:$data")
        val sql = "insert into $table_name values (null, '${data.name}', '${data.url}', '${data.path}', '${data.start_time}', '${data.end_time}', ${data.read}, ${data.count}, '${data.status}')"
        this.writableDatabase?.execSQL(sql)
        return null
    }

    override fun add(dataList: MutableList<JDownloadInfo>): MutableList<JDownloadInfo>? {
        LogUtils.d("adds:$dataList")
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (data in dataList) {
                db.insert(table_name, null, download2Value(data))
            }
            db.setTransactionSuccessful()
            return null
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    @Throws(SQLException::class)
    fun addExec(jDownloads: MutableList<JDownloadInfo>) {
        LogUtils.d("addExec:$jDownloads")
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            for (jDownload in jDownloads) {
                val sql = "insert into $table_name values (null, '${jDownload.name}', '${jDownload.url}', '${jDownload.path}', '${jDownload.start_time}', '${jDownload.end_time}', ${jDownload.read}, ${jDownload.count}, '${jDownload.status}')"
                LogUtils.d(sql)
                this.writableDatabase?.execSQL(sql)
            }
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    override fun remove(data: JDownloadInfo): Boolean {
        LogUtils.d("remove:$data")
        val db = this.writableDatabase
        val result = db.delete(table_name, "id = ?", arrayOf("${data.id}")) == 1
        db.close()
        return result
    }

    override fun remove(dataList: MutableList<JDownloadInfo>): Boolean {
        LogUtils.d("remove:$dataList")
        val db = this.writableDatabase
        db.beginTransaction()
        var result = true
        try {
            for (data in dataList) {
                result = result and (db.delete(table_name, "id = ?", arrayOf("${data.id}")) == 1)
            }
            db.setTransactionSuccessful()
            return result
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    override fun removeAll(): Boolean {
        LogUtils.d("removeAll")
        val db = this.writableDatabase
        db.delete(table_name, null, null)
        db.close()
        return true
    }

    override fun update(data: JDownloadInfo): JDownloadInfo? {
        val db = this.writableDatabase
        val sql = "update $table_name set  name = '${data.name}', url = '${data.url}', path = '${data.path}', end_time = '${data.end_time}', read = ${data.read}, count = ${data.count}, status = '${data.status}' where id = ${data.id}"
        LogUtils.d(sql)
        db.execSQL(sql)
        db.close()
        return null
    }

    override fun query(map: MutableMap<String, out String>): MutableList<JDownloadInfo> {
        LogUtils.d("query:$map")
        // 针对key和value查询
        val sb = StringBuilder()
        for (items in map) {
            sb.append("and ${items.key} = ?")
        }
        sb.delete(0, 3)
        // cursor = this.writableDatabase.query(table_name, null, null, mutableMap.values.toTypedArray(), null, null, null)
        return queryDataBase("select * from $table_name where $sb", map.values.toTypedArray())
    }

    override fun query(id: Int): JDownloadInfo? {
        val sql = "select * from $table_name where id = ?"
        return queryDataBase(sql, arrayOf(id.toString()))[0]
    }

    override fun query(pages: Int, limit: Int): MutableList<JDownloadInfo> {
        val startPoint = pages * limit
        val sql = "select * from $table_name limit $limit offset $startPoint"
        return queryDataBase(sql, arrayOf())
    }

    override fun queryAll(): MutableList<JDownloadInfo>? {
        // 查询所有
        // cursor = this.writableDatabase.query(table_name, null, null, null, null, null, null)
        return queryDataBase("select * from $table_name", arrayOf())
    }

    override fun queryCount(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun queryDataBase(sql: String, array: Array<out String>? = null): MutableList<JDownloadInfo> {
        LogUtils.d("queryDataBase:$sql")
        val downloadList: MutableList<JDownloadInfo> = mutableListOf()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(sql, array)
        // 移动游标获取值
        cursor.let {
            while (it.moveToNext()) {
                downloadList.add(JDownloadInfo(
                        it.getInt(it.getColumnIndex("id"))
                        , it.getString(it.getColumnIndex("name"))
                        , it.getString(it.getColumnIndex("url"))
                        , it.getString(it.getColumnIndex("path"))
                        , it.getString(it.getColumnIndex("start_time"))
                        , it.getString(it.getColumnIndex("end_time"))
                        , it.getLong(it.getColumnIndex("read"))
                        , it.getLong(it.getColumnIndex("count"))
                        , it.getString(it.getColumnIndex("status"))))
            }
        }
        cursor.close()
        db.close()
        return downloadList
    }

    private fun download2Value(data: JDownloadInfo): ContentValues = ContentValues().apply {
        put("name", data.name)
        put("url", data.url)
        put("path", data.path)
        put("start_time", data.start_time)
        put("end_time", data.end_time)
        put("read", data.read)
        put("count", data.count)
        put("status", data.status)
    }
}