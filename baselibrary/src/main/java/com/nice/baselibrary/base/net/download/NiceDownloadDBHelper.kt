package com.nice.baselibrary.base.net.download

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList


/**
 * @author JPlus
 * @date 2019/2/16.
 */
class NiceDownloadDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "download.db"
        private const val TABLE_NAME = "download"
        const val DB_VERSION = 1
        const val DOWNLOAD_SQL = "create table " + TABLE_NAME + "(" +
                "id integer primary key autoincrement," +
                "name text not null," +
                "url text not null," +
                "path text not null," +
                "date text not null," +
                "read long not null," +
                "count long not null," +
                "status text not null" + ")"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DOWNLOAD_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun queryAll(mDatabase: SQLiteDatabase?): ArrayList<NiceDownloadInfo> {
        val downloads = arrayListOf<NiceDownloadInfo>()
        // 相当于 select * from students 语句

        val cursor = mDatabase?.rawQuery("select * from download", arrayOf())

        // 移动光标获取值
        while (cursor!!.moveToNext()) {
            val downloadItem = NiceDownloadInfo(
                    cursor.getInt(cursor.getColumnIndex("id"))
                    , cursor.getString(cursor.getColumnIndex("name"))
                    , cursor.getString(cursor.getColumnIndex("url"))
                    , cursor.getString(cursor.getColumnIndex("path"))
                    , cursor.getString(cursor.getColumnIndex("date"))
                    , cursor.getLong(cursor.getColumnIndex("read"))
                    , cursor.getLong(cursor.getColumnIndex("count"))
                    , cursor.getString(cursor.getColumnIndex("status")))
            downloads.add(downloadItem)
        }
        // 关闭光标
        cursor.close()
        return downloads
    }

    fun queryById(id: Int, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        var niceDownloadItem: NiceDownloadInfo? = null
        val cursor = mDatabase?.rawQuery("select * from download where id = ?", arrayOf(id.toString()))

        // 移动光标获取值
        while (cursor!!.moveToNext()) {
            niceDownloadItem = NiceDownloadInfo(
                    cursor.getInt(cursor.getColumnIndex("id"))
                    , cursor.getString(cursor.getColumnIndex("name"))
                    , cursor.getString(cursor.getColumnIndex("url"))
                    , cursor.getString(cursor.getColumnIndex("path"))
                    , cursor.getString(cursor.getColumnIndex("date"))
                    , cursor.getLong(cursor.getColumnIndex("read"))
                    , cursor.getLong(cursor.getColumnIndex("count"))
                    , cursor.getString(cursor.getColumnIndex("status")))
        }
        // 关闭光标
        cursor.close()
        return niceDownloadItem
    }
    fun queryByName(name: String, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        var niceDownloadItem: NiceDownloadInfo? = null
        val cursor = mDatabase?.rawQuery("select * from download where name = ?", arrayOf(name))

        // 移动光标获取值
        while (cursor!!.moveToNext()) {
            niceDownloadItem = NiceDownloadInfo(
                    cursor.getInt(cursor.getColumnIndex("id"))
                    , cursor.getString(cursor.getColumnIndex("name"))
                    , cursor.getString(cursor.getColumnIndex("url"))
                    , cursor.getString(cursor.getColumnIndex("path"))
                    , cursor.getString(cursor.getColumnIndex("date"))
                    , cursor.getLong(cursor.getColumnIndex("read"))
                    , cursor.getLong(cursor.getColumnIndex("count"))
                    , cursor.getString(cursor.getColumnIndex("status")))
        }
        // 关闭光标
        cursor.close()
        return niceDownloadItem
    }
    fun queryByUrl(url: String, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        var niceDownloadItem: NiceDownloadInfo? = null
        val cursor = mDatabase?.rawQuery("select * from download where url = ?", arrayOf(url))

        // 移动光标获取值
        while (cursor!!.moveToNext()) {
            niceDownloadItem = NiceDownloadInfo(
                    cursor.getInt(cursor.getColumnIndex("id"))
                    , cursor.getString(cursor.getColumnIndex("name"))
                    , cursor.getString(cursor.getColumnIndex("url"))
                    , cursor.getString(cursor.getColumnIndex("path"))
                    , cursor.getString(cursor.getColumnIndex("date"))
                    , cursor.getLong(cursor.getColumnIndex("read"))
                    , cursor.getLong(cursor.getColumnIndex("count"))
                    , cursor.getString(cursor.getColumnIndex("status")))
        }
        // 关闭光标
        cursor.close()
        return niceDownloadItem
    }

    fun add(niceDownload: NiceDownloadInfo, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        mDatabase?.execSQL("insert into download values (null, ?,?, ?, ?, ?, ?, ?)", arrayOf(niceDownload.name, niceDownload.url, niceDownload.path, niceDownload.date, niceDownload.read, niceDownload.count, niceDownload.status))
        return queryByName(niceDownload.name, mDatabase)
    }
    fun update(niceDownload: NiceDownloadInfo, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        mDatabase?.execSQL("update download set read = ${niceDownload.read}, count = ${niceDownload.count}, status = '${niceDownload.status}' where url = '${niceDownload.url}'")
        return queryById(niceDownload.id, mDatabase)
    }
    fun remove(niceDownload: NiceDownloadInfo, mDatabase: SQLiteDatabase?): NiceDownloadInfo? {
        val id = niceDownload.id
        mDatabase?.delete("download", "id = ?", arrayOf("$id"))
        return queryById(id, mDatabase)
    }

}