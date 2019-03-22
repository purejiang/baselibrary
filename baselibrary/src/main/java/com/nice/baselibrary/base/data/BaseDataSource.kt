package com.nice.baselibrary.base.data

/**
 * 数据处理基类
 * @author JPlus
 * @date 2019/2/14.
 */
abstract class BaseDataSource<T> {
    /**
     * 添加数据
     * @param data
     */
    abstract fun addData(data:T)
    /**
     * 添加数据通过list
     * @param dataList
     */
    abstract fun addDatas(dataList:ArrayList<T>)
    /**
     * 删除数据
     * @param data
     */
    abstract fun removeData(data:T)
    /**
     * 删除数据通过list
     * @param dataList
     * @return
     */
    abstract fun removeData(dataList:ArrayList<T>)
    /**
     * 删除所有
     */
    abstract fun  deleteAll()
    /**
     * 修改数据
     * @param data
     */
    abstract fun modifyData(data:T)
    /**
     * 刷新数据
     * @return
     */
    abstract fun refreshData():ArrayList<T>
    /**
     * 查询数据
     * @param data
     * @return
     */
    abstract fun getData(data:T):T?
    /**
     * 查询所有数据
     * @return
     */
    abstract fun getAllData():ArrayList<T>
}