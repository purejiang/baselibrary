package com.nice.baselibrary.base.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * 数据库操作基类
 * @author Jplsu
 * @date 2019/2/16.
 */
abstract class NiceDBHelper :SQLiteOpenHelper {


    constructor(context: Context, dataName:String, factory: SQLiteDatabase.CursorFactory, dataVersion:Int):super(context, dataName, factory, dataVersion){

    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}