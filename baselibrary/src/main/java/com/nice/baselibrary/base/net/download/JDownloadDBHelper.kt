package com.nice.baselibrary.base.net.download

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nice.baselibrary.base.utils.LogUtils
import java.lang.StringBuilder


/**
 * @author JPlus
 * @date 2019/2/16.
 */
class JDownloadDBHelper(context: Context, private val table_name: String = "download", db_name: String = "download.db", db_version: Int = 1) : SQLiteOpenHelper(context, db_name, null, db_version) {


    override fun onCreate(db: SQLiteDatabase?) {
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

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun query(mutableMap: MutableMap<String, String>?): MutableList<JDownloadInfo> {
        val downloadList: MutableList<JDownloadInfo> = mutableListOf()
        val cursor: Cursor?
        if (mutableMap == null) {
            // 查询所有
//            cursor = this.writableDatabase.query(table_name, null, null, null, null, null, null)
            cursor = this.writableDatabase.rawQuery("select * from $table_name", arrayOf())
        } else {
            // 针对key和value查询
            val sb = StringBuilder()
            for (items in mutableMap){
                sb.append("and ${items.key} = ?")
            }
            sb.delete(0, 3)
//            cursor = this.writableDatabase.query(table_name, null, null, mutableMap.values.toTypedArray(), null, null, null)
            cursor = this.writableDatabase?.rawQuery("select * from $table_name where $sb", mutableMap.values.toTypedArray())
        }
        // 移动光标获取值
        cursor?.let {
            while (it.moveToNext()) {
                downloadList.add(JDownloadInfo(
                        cursor.getInt(cursor.getColumnIndex("id"))
                        , cursor.getString(cursor.getColumnIndex("name"))
                        , cursor.getString(cursor.getColumnIndex("url"))
                        , cursor.getString(cursor.getColumnIndex("path"))
                        , cursor.getString(cursor.getColumnIndex("start_time"))
                        , cursor.getString(cursor.getColumnIndex("end_time"))
                        , cursor.getLong(cursor.getColumnIndex("read"))
                        , cursor.getLong(cursor.getColumnIndex("count"))
                        , cursor.getString(cursor.getColumnIndex("status"))))
            }
        }
        // 关闭光标
        cursor?.close()
        LogUtils.d(downloadList.toString())
        return downloadList
    }

    fun add(jDownload: JDownloadInfo): MutableList<JDownloadInfo> {
        this.writableDatabase?.execSQL("insert into $table_name values (null, ?,?, ?, ?, ?, ?, ?, ?)", arrayOf(jDownload.name, jDownload.url, jDownload.path, jDownload.start_time, jDownload.end_time, jDownload.read, jDownload.count, jDownload.status))
        return query(mutableMapOf(Pair("name", jDownload.name)))
    }

    fun update(jDownload: JDownloadInfo): MutableList<JDownloadInfo> {
        val sql = "update $table_name set  name = '${jDownload.name}', url = '${jDownload.url}', path = '${jDownload.path}', end_time = '${jDownload.end_time}', read = ${jDownload.read}, count = ${jDownload.count}, status = '${jDownload.status}' where id = ${jDownload.id}"
        LogUtils.d(sql)
        this.writableDatabase?.execSQL(sql)
        return query(mutableMapOf(Pair("id", "${jDownload.id}")))
    }

    fun remove(jDownload: JDownloadInfo): Boolean {
        this.writableDatabase?.delete(table_name, "id = ?", arrayOf("${jDownload.id}"))
        return query(mutableMapOf(Pair("id", "${jDownload.id}"))).size == 0
    }

}