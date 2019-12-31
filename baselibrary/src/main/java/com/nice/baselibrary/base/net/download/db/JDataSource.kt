package com.nice.baselibrary.base.net.download.db


/**
 * 数据处理基类
 * @author JPlus
 * @date 2019/2/14.
 */
abstract class JDataSource<T> {
    /**
     * 添加数据
     * @param data
     * @return
     */
    abstract fun addData(data:T):T?
    /**
     * 添加数据通过list
     * @param dataList
     * @return
     */
    abstract fun addDataList(dataList:MutableList<T>):MutableList<T>?
    /**
     * 删除数据
     * @param data
     * @return
     */
    abstract fun removeData(data:T):Boolean
    /**
     * 删除数据通过list
     * @param dataList
     * @return
     */
    abstract fun removeDataList(dataList:MutableList<T>):Boolean
    /**
     * 删除所有
     * @return
     */
    abstract fun removeAllData():Boolean
    /**
     * 修改数据
     * @param data
     * @return
     */
    abstract fun modifyData(data:T):T?
    /**
     * 通过特定值查询数据
     * @param map
     * @return
     */
    abstract fun getData(map:MutableMap<String, out String>): MutableList<T>?
    /**
     * 通过id查询数据
     * @param id
     * @return
     */
    abstract fun getData(id:Int): T?
    /**
     * 分页查询
     * @param pages
     * @param limit
     * @return
     */
    abstract fun getDataList(pages:Int, limit:Int):MutableList<T>?
    /**
     * 查询所有数据
     * @return
     */
    abstract fun getAllData():MutableList<T>?
}