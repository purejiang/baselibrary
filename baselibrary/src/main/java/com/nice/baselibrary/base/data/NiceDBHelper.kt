package com.nice.baselibrary.base.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * 数据库操作基类
 * @author Jplsu
 * @date 2019/2/16.
 */
abstract class NiceDBHelper<T> :SQLiteOpenHelper {

    constructor(context: Context, dataName:String, factory: SQLiteDatabase.CursorFactory, dataVersion:Int):super(context, dataName, factory, dataVersion){

    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    abstract fun queryAll(mDatabase: SQLiteDatabase?): ArrayList<T>

    abstract fun queryByAny(any: Any, mDatabase: SQLiteDatabase?): T?

    abstract fun add(item: T, mDatabase: SQLiteDatabase?): T?

    abstract fun update(item: T, mDatabase: SQLiteDatabase?): T?

    abstract fun remove(item: T, mDatabase: SQLiteDatabase?): T?


}