package com.nice.baselibrary.base.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * 数据库操作基类
 * @author JPlus
 * @date 2019/2/16.
 */
abstract class JDBHelper<T>(context: Context, dataName: String, factory: SQLiteDatabase.CursorFactory?, dataVersion: Int) : SQLiteOpenHelper(context, dataName, factory, dataVersion) {

    override fun onCreate(db: SQLiteDatabase?) {
        onCreated(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgraded(db, oldVersion, newVersion)
    }
    abstract fun onCreated(db: SQLiteDatabase?)

    abstract  fun onUpgraded(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)

    /**
     * 添加数据
     * @param data
     * @return
     */
    abstract fun add(data:T):Boolean
    /**
     * 添加数据通过list
     * @param dataList
     * @return
     */
    abstract fun add(dataList:MutableList<T>):MutableList<Boolean>
    /**
     * 删除数据
     * @param data
     * @return
     */
    abstract fun remove(data:T):Boolean
    /**
     * 删除数据通过list
     * @param dataList
     * @return
     */
    abstract fun remove(dataList:MutableList<T>):Boolean
    /**
     * 删除所有
     * @return
     */
    abstract fun removeAll():Boolean
    /**
     * 修改数据
     * @param data
     * @return
     */
    abstract fun update(data:T):Boolean
    /**
     * 通过特定值查询数据
     * @param map
     * @return
     */
    abstract fun query(map:MutableMap<String, out String>): MutableList<T>?
    /**
     * 通过id查询数据
     * @param id
     * @return
     */
    abstract fun query(id:Int): T?
    /**
     * 分页查询
     * @param pages
     * @param limit
     * @return
     */
    abstract fun query(pages:Int, limit:Int):MutableList<T>?
    /**
     * 查询所有数据
     * @return
     */
    abstract fun queryAll():MutableList<T>?
    /**
     * 查询数据项
     * @return
     */
    abstract fun queryCount():Long

}